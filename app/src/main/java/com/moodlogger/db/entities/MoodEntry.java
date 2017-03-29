package com.moodlogger.db.entities;

import com.moodlogger.DateUtils;

import java.util.Calendar;
import java.util.List;

public class MoodEntry extends Entity {

    public static final String TABLE_NAME = "mood_entry";
    public static final String LOCATION_LATITUDE = "location_latitude";
    public static final String LOCATION_LONGITUDE = "location_longitude";
    public static final String DATE_TIME = "date_time";
    public static final String MOOD_ID = "mood_id";

    private long id;
    private float locationLatitude;
    private float locationLongitude;
    private String dateTime;
    private int moodId;
    private List<Activity> activities;

    public MoodEntry(float locationLatitude, float locationLongitude, int moodId, List<Activity> activities) {
        this(-1L, locationLatitude, locationLongitude, null, moodId, activities);
    }

    public MoodEntry(long id, float locationLatitude, float locationLongitude, String dateTime, int moodId, List<Activity> activities) {
        this.id = id;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.dateTime = dateTime;
        this.moodId = moodId;
        this.activities = activities;
    }

    public long getId() {
        return id;
    }

    public float getLocationLatitude() {
        return locationLatitude;
    }

    public float getLocationLongitude() {
        return locationLongitude;
    }

    public String getDateTime() {
        return dateTime;
    }

    public Calendar getFormattedDate() {
        return DateUtils.getDateFromSqliteDateTime(dateTime);
    }

    public int getMoodId() {
        return moodId;
    }

    public List<Activity> getActivities() {
        return activities;
    }
}
