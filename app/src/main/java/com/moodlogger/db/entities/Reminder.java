package com.moodlogger.db.entities;

public class Reminder extends Entity {

    public static final String TABLE_NAME = "reminder";
    public static final String TIME = "time";
    public static final String IS_ENABLED = "is_enabled";

    private long id;
    private String time;
    private boolean enabled;

    public Reminder(long id, String time, boolean enabled) {
        this.id = id;
        this.time = time;
        this.enabled = enabled;
    }

    public long getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Reminder)) {
            return false;
        }

        final Reminder other = (Reminder) obj;

        return this.getId() == other.getId() &&
                this.getTime().equals(other.getTime()) &&
                this.isEnabled() == other.isEnabled();
    }
}
