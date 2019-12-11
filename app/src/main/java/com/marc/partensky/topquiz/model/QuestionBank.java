package com.marc.partensky.topquiz.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionBank {
    protected List<Question> mQuestionsList;
    protected int mIndex;

    public static QuestionBank buildFromCSV(BufferedReader reader) throws IOException {
        List<Question> questions = new ArrayList();
        String line;
        while ((line = reader.readLine()) != null) {
            Question question = Question.buildFromCSV(line.split(","));
            questions.add(question);
        }
        reader.close();
        return new QuestionBank(questions);
    }

    public QuestionBank(List<Question> questionsList) {
        mQuestionsList = questionsList;
        mIndex = 0;
    }

    public Question getQuestion() {
        return mQuestionsList.get(mIndex);
    }

    public int getNumberQuestionsLeft() {
        return mQuestionsList.size() - mIndex;
    }

    public void next() {
        mIndex++;
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public void shuffle() {
        for (Question question: mQuestionsList ) {
            question.shuffle();
        }
        Collections.shuffle(mQuestionsList);
    }
}