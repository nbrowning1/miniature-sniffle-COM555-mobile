package com.moodlogger;

public enum MoodEnum {

    Great("MOOD_GREAT", "Great", 4),
    Happy("MOOD_HAPPY", "Happy", 3),
    Neutral("MOOD_NEUTRAL", "OK", 2),
    Sad("MOOD_SAD", "Sad", 1),
    Angry("MOOD_ANGRY", "Angry", 0);

    private String tagName;
    private String labelName;
    private int moodRating;

    MoodEnum(String tagName, String labelName, int moodRating) {
        this.tagName = tagName;
        this.labelName = labelName;
        this.moodRating = moodRating;
    }

    public String getLabelName() {
        return labelName;
    }

    public static int getMoodRating(String tagName) {
        for (MoodEnum mood : MoodEnum.values()) {
            if (tagName.equals(mood.tagName)) {
                return mood.moodRating;
            }
        }
        return -1;
    }

    public static String getTagName(int moodRating) {
        for (MoodEnum mood : MoodEnum.values()) {
            if (moodRating == mood.moodRating) {
                return mood.tagName;
            }
        }
        return "";
    }

    public static String getLabelName(int moodRating) {
        for (MoodEnum mood : MoodEnum.values()) {
            if (moodRating == mood.moodRating) {
                return mood.labelName;
            }
        }
        return "";
    }
}
