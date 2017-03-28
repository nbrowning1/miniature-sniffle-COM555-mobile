package com.moodlogger.activities;

public enum MoodEnum {

    Great("MOOD_GREAT", 4),
    Happy("MOOD_HAPPY", 3),
    Neutral("MOOD_NEUTRAL", 2),
    Sad("MOOD_SAD", 1),
    Angry("MOOD_ANGRY", 0);

    private String tagName;
    private int moodRating;

    MoodEnum(String tagName, int moodRating) {
        this.tagName = tagName;
        this.moodRating = moodRating;
    }

    public static int getMoodRating(String tagName) {
        for (MoodEnum mood : MoodEnum.values()) {
            if (tagName.equals(mood.tagName)) {
                return mood.moodRating;
            }
        }
        return -1;
    }
}
