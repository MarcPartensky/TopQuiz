package com.marc.partensky.topquiz.model;

public class User {
    protected String mFirstName;
    protected int mScore = 0;

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    @Override
    public String toString() {
        return "User{" +
                "mFirstName='" + mFirstName + '\'' +
                '}';
    }

    public void win() {
        mScore++;
    }

    public int getScore() {
        return mScore;
    }

}
