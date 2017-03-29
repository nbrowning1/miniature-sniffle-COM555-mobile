package com.moodlogger.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.moodlogger.db.entities.Activity;
import com.moodlogger.db.entities.MoodEntryActivity;

import java.util.ArrayList;
import java.util.List;

public class MoodEntryActivityDbHelper extends MoodDbHelper implements DbHelperIntf<MoodEntryActivity> {

    public MoodEntryActivityDbHelper(Context context) {
        super(context);
    }

    public List<Activity> getActivitiesForMoodId(long moodId) {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {
                MoodEntryActivity.ACTIVITY_ID
        };

        String selection = MoodEntryActivity.MOOD_ENTRY_ID + " = ?";
        String[] selectionArgs = { Long.toString(moodId) };

        Cursor cursor = db.query(MoodEntryActivity.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        ActivityDbHelper activityDbHelper = new ActivityDbHelper(context);
        List<Activity> activities = new ArrayList<>();
        while (cursor.moveToNext()) {
            long activityId = cursor.getLong(cursor.getColumnIndexOrThrow(MoodEntryActivity.ACTIVITY_ID));
            activities.add(activityDbHelper.getActivity(activityId));
        }
        cursor.close();

        return activities;
    }

    @Override
    public long create(MoodEntryActivity moodEntryActivity) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MoodEntryActivity.MOOD_ENTRY_ID, moodEntryActivity.getMoodEntryId());
        values.put(MoodEntryActivity.ACTIVITY_ID, moodEntryActivity.getActivityId());

        return db.insert(MoodEntryActivity.TABLE_NAME, null, values);
    }
}
