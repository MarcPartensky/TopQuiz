package com.marc.partensky.topquiz.model;

import java.util.List;

public class Question {
    protected String mQuestion;
    protected List<String> mChoiceList;
    protected int mAnswerIndex;
    
    public Question(String question, List<String> choiceList, int answerIndex) {
        this.setQuestion(question);
        this.setChoiceList(choiceList);
        this.setAnswerIndex(answerIndex);
    }

    private void setQuestion(String question) {

    }

    private void setChoiceList(List<String> choiceList) {

    }

    private void setAnswerIndex(int answerIndex) {

    }
}
