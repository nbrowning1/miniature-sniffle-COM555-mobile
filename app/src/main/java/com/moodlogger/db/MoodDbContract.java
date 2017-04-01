package com.moodlogger.db;

import com.moodlogger.db.entities.Activity;
import com.moodlogger.db.entities.Mood;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.entities.MoodEntryActivity;
import com.moodlogger.db.entities.Reminder;
import com.moodlogger.db.entities.Screen;
import com.moodlogger.db.entities.User;

import java.util.Locale;

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

    private static final String SQL_UPDATE_MOOD_TABLE =
            "INSERT INTO " + Mood.TABLE_NAME +
                    "(" + Mood.NAME + ", " + Mood.RATING + ") VALUES ('%s', %d);";

    private static final String SQL_CREATE_ACTIVITY_TABLE =
            "CREATE TABLE " + Activity.TABLE_NAME + " (" +
                    Activity._ID + " INTEGER PRIMARY KEY," +
                    Activity.NAME + " TEXT," +
                    Activity.IMG_KEY + " TEXT);";

    private static final String SQL_UPDATE_ACTIVITY_TABLE =
            "INSERT INTO " + Activity.TABLE_NAME +
                    "(" + Activity.NAME + ", " + Activity.IMG_KEY + ") VALUES ('%s', '%s');";

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

    private static final String SQL_CREATE_REMINDER_TABLE =
            "CREATE TABLE " + Reminder.TABLE_NAME + " (" +
                    Reminder._ID + " INTEGER PRIMARY KEY," +
                    Reminder.TIME + " TEXT," +
                    Reminder.IS_ENABLED + " BOOLEAN);";

    private static final String SQL_UPDATE_REMINDER_TABLE =
            "INSERT INTO " + Reminder.TABLE_NAME +
                    "(" + Reminder._ID + ", " + Reminder.TIME + ", " + Reminder.IS_ENABLED + ") " +
                    "VALUES (%d, '12:00', 0);";

    public static final String[] SQL_CREATE_ENTRIES = new String[]{
            SQL_CREATE_MOOD_ENTRY_TABLE,
            SQL_CREATE_MOOD_TABLE,
            SQL_CREATE_ACTIVITY_TABLE,
            SQL_CREATE_MOOD_ENTRY_ACTIVITY_TABLE,
            SQL_CREATE_SCREEN_TABLE,
            SQL_CREATE_USER_TABLE,
            SQL_CREATE_REMINDER_TABLE
    };

    public static final String[] SQL_UPDATE_ENTRIES = new String[]{
            String.format(Locale.UK, SQL_UPDATE_MOOD_TABLE, "Great", 4),
            String.format(Locale.UK, SQL_UPDATE_MOOD_TABLE, "Happy", 3),
            String.format(Locale.UK, SQL_UPDATE_MOOD_TABLE, "Neutral", 2),
            String.format(Locale.UK, SQL_UPDATE_MOOD_TABLE, "Sad", 1),
            String.format(Locale.UK, SQL_UPDATE_MOOD_TABLE, "Angry", 0),

            String.format(SQL_UPDATE_ACTIVITY_TABLE, "Work", "activity_work"),
            String.format(SQL_UPDATE_ACTIVITY_TABLE, "Study", "activity_study"),
            String.format(SQL_UPDATE_ACTIVITY_TABLE, "Relax", "activity_relax"),
            String.format(SQL_UPDATE_ACTIVITY_TABLE, "Exercise", "activity_exercise"),
            String.format(SQL_UPDATE_ACTIVITY_TABLE, "Travel", "activity_travel"),

            String.format(Locale.UK, SQL_UPDATE_REMINDER_TABLE, 1),
            String.format(Locale.UK, SQL_UPDATE_REMINDER_TABLE, 2),
            String.format(Locale.UK, SQL_UPDATE_REMINDER_TABLE, 3)
    };

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MoodEntry.TABLE_NAME + "; " +
                    "DROP TABLE IF EXISTS " + Mood.TABLE_NAME + "; " +
                    "DROP TABLE IF EXISTS " + Activity.TABLE_NAME + "; " +
                    "DROP TABLE IF EXISTS " + MoodEntryActivity.TABLE_NAME + "; " +
                    "DROP TABLE IF EXISTS " + Screen.TABLE_NAME + "; " +
                    "DROP TABLE IF EXISTS " + User.TABLE_NAME + "; " +
                    "DROP TABLE IF EXISTS " + Reminder.TABLE_NAME + ";";
}
