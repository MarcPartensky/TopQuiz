package com.marc.partensky.topquiz.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.marc.partensky.topquiz.R;
import com.marc.partensky.topquiz.model.Question;
import com.marc.partensky.topquiz.model.QuestionBank;

import java.util.Arrays;

import es.dmoral.toasty.Toasty;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {
    protected TextView mScoreTextView;
    protected TextView mQuestionTextView;
    protected Button mAnswerButton1;
    protected Button mAnswerButton2;
    protected Button mAnswerButton3;
    protected Button mAnswerButton4;
    protected QuestionBank mQuestionBank;
    protected int mAnswerIndex;
    protected int mUserAnswerIndex;
    protected int mScore = 0;
    public static final String BUNDLE_EXTRA_SCORE = "EXTRA_SCORE";
    protected boolean mEnableTouchEvents;


    public QuestionBank generateQuestions() {
        Question question1 = new Question("Who is the creator of Android?",
                Arrays.asList("Andy Rubin",
                        "Steve Wozniak",
                        "Jake Wharton",
                        "Paul Smith"),
                0);

        Question question2 = new Question("When did the first man land on the moon?",
                Arrays.asList("1958",
                        "1962",
                        "1967",
                        "1969"),
                3);

        Question question3 = new Question("What is the house number of The Simpsons?",
                Arrays.asList("42",
                        "101",
                        "666",
                        "742"),
                3);

        return new QuestionBank(Arrays.asList(question1, question2, question3));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mQuestionBank = this.generateQuestions();
        mEnableTouchEvents = true;

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
        Question question = mQuestionBank.getQuestion();
        mAnswerIndex = question.getAnswerIndex();
        mScoreTextView.setText(String.valueOf(mScore));
        mQuestionTextView.setText(question.getQuestion());
        mAnswerButton1.setText(question.getChoiceList().get(0));
        mAnswerButton2.setText(question.getChoiceList().get(1));
        mAnswerButton3.setText(question.getChoiceList().get(2));
        mAnswerButton4.setText(question.getChoiceList().get(3));
    }

    @Override
    public void onClick(View v) {
        mUserAnswerIndex = (int) v.getTag();
        if (mAnswerIndex == mUserAnswerIndex) {
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
                main();
            }
        }, 1000);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }
}
