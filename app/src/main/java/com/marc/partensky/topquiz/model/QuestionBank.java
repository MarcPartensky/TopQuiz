package com.marc.partensky.topquiz.model;

import java.util.Collections;
import java.util.List;

public class QuestionBank {
    protected List<Question> mQuestionsList;
    protected int mNextQuestionIndex;

    public QuestionBank(List<Question> questionsList) {
        // Shuffle the question list
        mQuestionsList = questionsList;
        shuffle();
        mNextQuestionIndex = 0;
    }

    public Question getQuestion() {
        // Loop over the questions and return a new one at each call
        if (mNextQuestionIndex == mQuestionsList.size()) {
            mNextQuestionIndex = 0;
        }

        // Please note the post-incrementation
        return mQuestionsList.get(mNextQuestionIndex++);
    }

    public int getNumberQuestionsLeft() {
        return mQuestionsList.size() - mNextQuestionIndex;
    }

    protected void shuffle() {
        for (Question question: mQuestionsList ) {
            question.shuffle();
        }
        Collections.shuffle(mQuestionsList);
    }
}