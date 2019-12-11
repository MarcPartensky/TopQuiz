package com.marc.partensky.topquiz.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.marc.partensky.topquiz.R;
import com.marc.partensky.topquiz.model.Question;
import com.marc.partensky.topquiz.model.QuestionBank;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {
    protected TextView mScoreTextView;
    protected TextView mQuestionTextView;
    protected Button mAnswerButton1;
    protected Button mAnswerButton2;
    protected Button mAnswerButton3;
    protected Button mAnswerButton4;

    protected QuestionBank mQuestionBank;
    protected Question mQuestion;

    protected int mScore;

    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    public static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    public static final String BUNDLE_STATE_QUESTION = "BUNDLE_STATE_QUESTION";

    protected boolean mEnableTouchEvents;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.string.file)));//Specify asset file name
            System.out.println(reader);
            mQuestionBank = QuestionBank.buildFromCSV(reader);
        } catch (IOException e) {
            e.printStackTrace();
            Toasty.error(this, "The questions can't be loaded.",
                    Toast.LENGTH_LONG).show();
            mQuestionBank = new QuestionBank(new ArrayList());
        }

        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt(BUNDLE_EXTRA_SCORE);
            mQuestionBank.setIndex(savedInstanceState.getInt(BUNDLE_STATE_QUESTION));
        } else {
            mQuestionBank.shuffle();
        }

        mEnableTouchEvents = true;
        mScore = 0;

        mScoreTextView = findViewById(R.id.activity_question_score_text);
        mQuestionTextView = findViewById(R.id.activity_question_question_text);
        mAnswerButton1 = findViewById(R.id.activity_question_answer1_btn);
        mAnswerButton2 = findViewById(R.id.activity_question_answer2_btn);
        mAnswerButton3 = findViewById(R.id.activity_question_answer3_btn);
        mAnswerButton4 = findViewById(R.id.activity_question_answer4_btn);

        mAnswerButton1.setTag(0);
        mAnswerButton2.setTag(1);
        mAnswerButton3.setTag(2);
        mAnswerButton4.setTag(3);

        mAnswerButton1.setOnClickListener(this);
        mAnswerButton2.setOnClickListener(this);
        mAnswerButton3.setOnClickListener(this);
        mAnswerButton4.setOnClickListener(this);


        main();
    }
    public void main() {
        if (mQuestionBank.getNumberQuestionsLeft() > 0) {
            ask();
        } else {
            endGame();
        }
    }

    protected void endGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Well done")
                .setMessage("Your score is "+ mScore)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLE_EXTRA_SCORE,  mScore);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .create()
                .show();
    }

    protected void ask() {
        mQuestion = mQuestionBank.getQuestion();
        mScoreTextView.setText(String.valueOf(mScore));
        mQuestionTextView.setText(mQuestion.getQuestion());
        mAnswerButton1.setText(mQuestion.getChoiceList().get(0));
        mAnswerButton2.setText(mQuestion.getChoiceList().get(1));
        mAnswerButton3.setText(mQuestion.getChoiceList().get(2));
        mAnswerButton4.setText(mQuestion.getChoiceList().get(3));
    }

    @Override
    public void onClick(View v) {
        int userAnswerIndex = (int) v.getTag();
        if (mQuestion.getAnswerIndex() == userAnswerIndex) {
            Toasty.success(this, "Correct", Toast.LENGTH_SHORT).show();
            mScore++;
        } else {
            Toasty.error(this, "Wrong", Toast.LENGTH_SHORT).show();
        }

        mEnableTouchEvents = false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnableTouchEvents = true;
                mQuestionBank.next();
                main();
            }
        }, 1000);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION, mQuestionBank.getIndex());

        super.onSaveInstanceState(outState);
    }
}
