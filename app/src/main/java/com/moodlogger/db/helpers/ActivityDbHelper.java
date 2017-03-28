package com.moodlogger.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.moodlogger.db.entities.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityDbHelper extends MoodDbHelper implements DbHelperIntf<Activity> {

    public ActivityDbHelper(Context context) {
        super(context);
    }

    public List<Activity> getActivities() {
        SQLiteDatabase db = getReadableDatabase();

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

    @Override
    public long create(Activity activity) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Activity.NAME, activity.getName());
        values.put(Activity.IMG_KEY, activity.getImgKey());

        return db.insert(Activity.TABLE_NAME, null, values);
    }
}
