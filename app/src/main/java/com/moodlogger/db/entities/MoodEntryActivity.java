package com.moodlogger.db.entities;

public class MoodEntryActivity extends Entity {

    public static final String TABLE_NAME = "mood_entry_activity";
    public static final String MOOD_ENTRY_ID = "mood_entry_id";
    public static final String ACTIVITY_ID = "activity_id";

    private long moodEntryId;
    private long activityId;

    public MoodEntryActivity(long moodEntryId, long activityId) {
        this.moodEntryId = moodEntryId;
        this.activityId = activityId;
    }

    public long getMoodEntryId() {
        return moodEntryId;
    }

    public long getActivityId() {
        return activityId;
    }
}
