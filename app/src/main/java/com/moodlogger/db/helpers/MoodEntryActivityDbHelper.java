package com.moodlogger.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.moodlogger.charts.TimeRangeEnum;
import com.moodlogger.db.entities.Activity;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.entities.MoodEntryActivity;

import java.util.ArrayList;
import java.util.List;

public class MoodEntryActivityDbHelper implements DbHelperIntf<MoodEntryActivity> {

    private MoodDbHelper dbHelper;

    public MoodEntryActivityDbHelper(Context context) {
        dbHelper = MoodDbHelper.getInstance(context);
    }

    public List<MoodEntry> getMoodEntriesForActivityId(long activityId, TimeRangeEnum timeRange) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                MoodEntryActivity.MOOD_ENTRY_ID
        };

        String selection = MoodEntryActivity.ACTIVITY_ID + " = ?";
        String[] selectionArgs = { Long.toString(activityId) };

        Cursor cursor = db.query(MoodEntryActivity.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        MoodEntryDbHelper moodEntryDbHelper = new MoodEntryDbHelper(dbHelper.getContext());
        List<MoodEntry> moodEntries = new ArrayList<>();
        while (cursor.moveToNext()) {
            long moodId = cursor.getLong(cursor.getColumnIndexOrThrow(MoodEntryActivity.MOOD_ENTRY_ID));
            MoodEntry moodEntry = moodEntryDbHelper.getMoodEntryIfPresent(moodId, timeRange);
            if (moodEntry != null) {
                moodEntries.add(moodEntry);
            }
        }
        cursor.close();

        return moodEntries;
    }

    public List<Activity> getActivitiesForMoodId(long moodId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                MoodEntryActivity.ACTIVITY_ID
        };

        String selection = MoodEntryActivity.MOOD_ENTRY_ID + " = ?";
        String[] selectionArgs = { Long.toString(moodId) };

        Cursor cursor = db.query(MoodEntryActivity.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        ActivityDbHelper activityDbHelper = new ActivityDbHelper(dbHelper.getContext());
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
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MoodEntryActivity.MOOD_ENTRY_ID, moodEntryActivity.getMoodEntryId());
        values.put(MoodEntryActivity.ACTIVITY_ID, moodEntryActivity.getActivityId());

        return db.insert(MoodEntryActivity.TABLE_NAME, null, values);
    }
}
