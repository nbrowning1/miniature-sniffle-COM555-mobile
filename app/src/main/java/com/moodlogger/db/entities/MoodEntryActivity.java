package com.moodlogger.db.entities;

public class MoodEntryActivity extends Entity {

    public static final String TABLE_NAME = "mood_entry_activity";
    public static final String MOOD_ENTRY_ID = "mood_entry_id";
    public static final String ACTIVITY_ID = "activity_id";

    public int moodEntryId;
    public int activityId;

    public MoodEntryActivity(int moodEntryId, int activityId) {
        this.moodEntryId = moodEntryId;
        this.activityId = activityId;
    }
}
