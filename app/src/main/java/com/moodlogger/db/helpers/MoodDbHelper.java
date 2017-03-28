package com.moodlogger.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.moodlogger.db.entities.User;

import static com.moodlogger.db.MoodDbContract.SQL_CREATE_ENTRIES;
import static com.moodlogger.db.MoodDbContract.SQL_DELETE_ENTRIES;
import static com.moodlogger.db.MoodDbContract.SQL_UPDATE_ENTRIES;

public class MoodDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Mood.db";

    public MoodDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        for (String createTableStmt : SQL_CREATE_ENTRIES) {
            db.execSQL(createTableStmt);
        }
        for (String updateStmt : SQL_UPDATE_ENTRIES) {
            db.execSQL(updateStmt);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: Better impl?
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
