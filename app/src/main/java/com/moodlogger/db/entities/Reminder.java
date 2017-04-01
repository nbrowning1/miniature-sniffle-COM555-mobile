package com.moodlogger.db.entities;

public class Reminder extends Entity {

    public static final String TABLE_NAME = "reminder";
    public static final String TIME = "time";
    public static final String IS_ENABLED = "is_enabled";

    private long id;
    private String time;
    private boolean isEnabled;

    public Reminder(long id, String time, boolean isEnabled) {
        this.id = id;
        this.time = time;
        this.isEnabled = isEnabled;
    }

    public long getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public boolean getEnabled() {
        return isEnabled;
    }
}
