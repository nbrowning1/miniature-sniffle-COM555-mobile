package com.moodlogger.db.entities;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MoodEntry extends Entity {

    public static final String TABLE_NAME = "mood_entry";
    public static final String LOCATION_LATITUDE = "location_latitude";
    public static final String LOCATION_LONGITUDE = "location_longitude";
    public static final String DATE_TIME = "date_time";
    public static final String MOOD_ID = "mood_id";

    private float locationLatitude;
    private float locationLongitude;
    private String dateTime;
    private int moodId;

    public MoodEntry(float locationLatitude, float locationLongitude, int moodId) {
        this(locationLatitude, locationLongitude, null, moodId);
    }

    public MoodEntry(float locationLatitude, float locationLongitude, String dateTime, int moodId) {
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.dateTime = dateTime;
        this.moodId = moodId;
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
        try {
            Calendar calendar = new GregorianCalendar();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = dateFormat.parse(dateTime); //replace 4 with the column index
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            Log.e("ERROR", "Error occurred parsing mood entry date to java format", e);
            throw new RuntimeException(e);
        }
    }

    public int getMoodId() {
        return moodId;
    }
}
