package com.marc.partensky.topquiz.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Question {

    protected String mQuestion;
    protected List<String> mAnswersList;
    protected int mAnswerIndex;

    public static Question buildFromCSV(String[] line) {
        String question = line[0];
        List<String> answers = Arrays.asList(line[1], line[2], line[3], line[4]);
        return new Question(question, answers, 0);
    }

    public Question(String question, List<String> answersList, int answerIndex) {
        mQuestion = question;
        mAnswersList = answersList;
        mAnswerIndex = answerIndex;
    }

    protected void shuffle() {
        String answer = mAnswersList.get(mAnswerIndex);
        Collections.shuffle(mAnswersList);
        mAnswerIndex = mAnswersList.indexOf(answer);

    }

    public String getQuestion() {
        return mQuestion;
    }

    public List<String> getChoiceList() {
        return mAnswersList;
    }

    public int getAnswerIndex() {
        return mAnswerIndex;
    }

    public String getAnswer() {
        return mAnswersList.get(mAnswerIndex);
    }

}
