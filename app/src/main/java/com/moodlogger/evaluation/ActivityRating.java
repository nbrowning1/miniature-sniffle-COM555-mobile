package com.moodlogger.evaluation;

public class ActivityRating {

    private int goodRating = 0;
    private int sadRating = 0;
    private int angryRating = 0;

    public ActivityRating() {
    }

    public int getGoodRating() {
        return goodRating;
    }

    public void addGoodRating() {
        goodRating++;
    }

    public int getBadRating() {
        return sadRating + angryRating;
    }

    public int getSadRating() {
        return sadRating;
    }

    public void addSadRating() {
        sadRating++;
    }

    public int getAngryRating() {
        return angryRating;
    }

    public void addAngryRating() {
        angryRating++;
    }
}
