package com.moodlogger.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.moodlogger.db.MoodDbContract.SQL_CREATE_ENTRIES;
import static com.moodlogger.db.MoodDbContract.SQL_DELETE_ENTRIES;
import static com.moodlogger.db.MoodDbContract.SQL_UPDATE_ENTRIES;

public class MoodDbHelper extends SQLiteOpenHelper {

    private static MoodDbHelper instance = null;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Mood.db";

    private Context context;

    public static MoodDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MoodDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    private MoodDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public Context getContext() {
        return context;
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
