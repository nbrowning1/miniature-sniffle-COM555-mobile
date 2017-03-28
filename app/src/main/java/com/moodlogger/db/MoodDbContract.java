package com.moodlogger.db;

import com.moodlogger.db.entities.Activity;
import com.moodlogger.db.entities.Mood;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.entities.MoodEntryActivity;
import com.moodlogger.db.entities.Screen;
import com.moodlogger.db.entities.User;

public final class MoodDbContract {

    // private to prevent from accidentally instantiating the contract
    private MoodDbContract() {
    }

    private static final String SQL_CREATE_MOOD_ENTRY_TABLE =
            "CREATE TABLE " + MoodEntry.TABLE_NAME + " (" +
                    MoodEntry._ID + " INTEGER PRIMARY KEY," +
                    // format: ##.######
                    MoodEntry.LOCATION_LATITUDE + " DECIMAL(8,6)," +
                    // format: ###.######
                    MoodEntry.LOCATION_LONGITUDE + " DECIMAL(9,6)," +
                    MoodEntry.DATE_TIME + " DATE DEFAULT (datetime('now','localtime'))," +
                    MoodEntry.MOOD_ID + " INTEGER);";

    private static final String SQL_CREATE_MOOD_TABLE =
            "CREATE TABLE " + Mood.TABLE_NAME + " (" +
                    Mood._ID + " INTEGER PRIMARY KEY," +
                    Mood.NAME + " TEXT," +
                    Mood.RATING + " TINYINT);";

    private static final String SQL_CREATE_ACTIVITY_TABLE =
            "CREATE TABLE " + Activity.TABLE_NAME + " (" +
                    Activity._ID + " INTEGER PRIMARY KEY," +
                    Activity.NAME + " TEXT," +
                    Activity.IMG_KEY + " TINYINT);";

    private static final String SQL_CREATE_MOOD_ENTRY_ACTIVITY_TABLE =
            "CREATE TABLE " + MoodEntryActivity.TABLE_NAME + " (" +
                    MoodEntryActivity.MOOD_ENTRY_ID + " INTEGER," +
                    MoodEntryActivity.ACTIVITY_ID + " INTEGER," +
                    "PRIMARY KEY (" + MoodEntryActivity.MOOD_ENTRY_ID + ", " + MoodEntryActivity.ACTIVITY_ID + "));";

    private static final String SQL_CREATE_SCREEN_TABLE =
            "CREATE TABLE " + Screen.TABLE_NAME + " (" +
                    Screen._ID + " INTEGER PRIMARY KEY," +
                    Screen.NAME + " TEXT," +
                    Screen.GUIDE_GIVEN + " BOOLEAN);";

    private static final String SQL_CREATE_USER_TABLE =
            "CREATE TABLE " + User.TABLE_NAME + " (" +
                    User._ID + " INTEGER PRIMARY KEY," +
                    User.NAME + " TEXT);";

    public static final String[] SQL_CREATE_ENTRIES = new String[]{
            SQL_CREATE_MOOD_ENTRY_TABLE,
            SQL_CREATE_MOOD_TABLE,
            SQL_CREATE_ACTIVITY_TABLE,
            SQL_CREATE_MOOD_ENTRY_ACTIVITY_TABLE,
            SQL_CREATE_SCREEN_TABLE,
            SQL_CREATE_USER_TABLE
    };

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MoodEntry.TABLE_NAME + "; " +
                    "DROP TABLE IF EXISTS " + Mood.TABLE_NAME + "; " +
                    "DROP TABLE IF EXISTS " + Activity.TABLE_NAME + "; " +
                    "DROP TABLE IF EXISTS " + MoodEntryActivity.TABLE_NAME + "; " +
                    "DROP TABLE IF EXISTS " + Screen.TABLE_NAME + "; " +
                    "DROP TABLE IF EXISTS " + User.TABLE_NAME + ";";
}
