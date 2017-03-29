package com.moodlogger.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.moodlogger.DateUtils;
import com.moodlogger.charts.TimeRangeEnum;
import com.moodlogger.db.entities.MoodEntry;

import java.util.ArrayList;
import java.util.List;

public class MoodEntryDbHelper extends MoodDbHelper implements DbHelperIntf<MoodEntry> {

    public MoodEntryDbHelper(Context context) {
        super(context);
    }

    public List<MoodEntry> getMoodEntries(TimeRangeEnum timeRange) {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {
                MoodEntry.LOCATION_LATITUDE,
                MoodEntry.LOCATION_LONGITUDE,
                MoodEntry.DATE_TIME,
                MoodEntry.MOOD_ID
        };

        String selection = MoodEntry.DATE_TIME + " BETWEEN ? AND ?";
        String[] selectionArgs = new String[2];
        if (timeRange.equals(TimeRangeEnum.Week)) {
            selectionArgs[0] = DateUtils.getSqliteDateForQuery(DateUtils.getStartOfWeek());
            selectionArgs[1] = DateUtils.getSqliteDateForQuery(DateUtils.getEndOfWeek());
        } else if (timeRange.equals(TimeRangeEnum.Month)) {
            selectionArgs[0] = DateUtils.getSqliteDateForQuery(DateUtils.getStartOfMonth());
            selectionArgs[1] = DateUtils.getSqliteDateForQuery(DateUtils.getEndOfMonth());
        } else {
            selectionArgs[0] = DateUtils.getSqliteDateForQuery(DateUtils.getStartOfYear());
            selectionArgs[1] = DateUtils.getSqliteDateForQuery(DateUtils.getEndOfYear());
        }

        String sortOrder = MoodEntry.DATE_TIME + " ASC";

        List<MoodEntry> moodEntries = new ArrayList<>();
        Cursor cursor = db.query(MoodEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
        while(cursor.moveToNext()) {
            float locationLatitude = cursor.getFloat(cursor.getColumnIndexOrThrow(MoodEntry.LOCATION_LATITUDE));
            float locationLongitude = cursor.getFloat(cursor.getColumnIndexOrThrow(MoodEntry.LOCATION_LONGITUDE));
            String dateTime = cursor.getString(cursor.getColumnIndexOrThrow(MoodEntry.DATE_TIME));
            int moodId = cursor.getInt(cursor.getColumnIndexOrThrow(MoodEntry.MOOD_ID));
            moodEntries.add(new MoodEntry(locationLatitude, locationLongitude, dateTime, moodId));
        }
        cursor.close();

        return moodEntries;
    }

    @Override
    public long create(MoodEntry moodEntry) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MoodEntry.LOCATION_LATITUDE, moodEntry.getLocationLatitude());
        values.put(MoodEntry.LOCATION_LONGITUDE, moodEntry.getLocationLongitude());
        values.put(MoodEntry.MOOD_ID, moodEntry.getMoodId());

        return db.insert(MoodEntry.TABLE_NAME, null, values);
    }
}
