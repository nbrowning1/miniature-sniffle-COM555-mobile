package com.moodlogger.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.moodlogger.db.entities.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityDbHelper implements DbHelperIntf<Activity> {

    private MoodDbHelper dbHelper;

    public ActivityDbHelper(Context context) {
        dbHelper = MoodDbHelper.getInstance(context);
    }

    public List<String> getActivityNames() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                Activity.NAME
        };

        List<String> activityNames = new ArrayList<>();
        Cursor cursor = db.query(Activity.TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            activityNames.add(cursor.getString(cursor.getColumnIndexOrThrow(Activity.NAME)));
        }
        cursor.close();

        return activityNames;
    }

    public List<Activity> getActivities() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                Activity._ID,
                Activity.NAME,
                Activity.IMG_KEY
        };

        List<Activity> activities = new ArrayList<>();
        Cursor cursor = db.query(Activity.TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(Activity._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(Activity.NAME));
            String imgKey = cursor.getString(cursor.getColumnIndexOrThrow(Activity.IMG_KEY));
            activities.add(new Activity(id, name, imgKey));
        }
        cursor.close();

        return activities;
    }

    public Activity getActivity(String name) {
        String selection = Activity.NAME + " = ?";
        String[] selectionArgs = {name};
        return getActivity(selection, selectionArgs);
    }

    public Activity getActivity(long id) {
        String selection = Activity._ID + " = ?";
        String[] selectionArgs = {Long.toString(id)};
        return getActivity(selection, selectionArgs);
    }

    public Activity getActivity(String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                Activity._ID,
                Activity.NAME,
                Activity.IMG_KEY
        };

        Cursor cursor = db.query(Activity.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        cursor.moveToNext();

        long id = cursor.getLong(cursor.getColumnIndexOrThrow(Activity._ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(Activity.NAME));
        String imgKey = cursor.getString(cursor.getColumnIndexOrThrow(Activity.IMG_KEY));
        Activity activity = new Activity(id, name, imgKey);

        cursor.close();

        return activity;
    }

    @Override
    public long create(Activity activity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Activity.NAME, activity.getName());
        values.put(Activity.IMG_KEY, activity.getImgKey());

        return db.insert(Activity.TABLE_NAME, null, values);
    }
}
