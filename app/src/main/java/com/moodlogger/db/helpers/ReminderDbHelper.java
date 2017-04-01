package com.moodlogger.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.moodlogger.db.entities.Reminder;

import java.util.ArrayList;
import java.util.List;

public class ReminderDbHelper implements DbHelperIntf<Reminder> {

    private MoodDbHelper dbHelper;

    public ReminderDbHelper(Context context) {
        dbHelper = MoodDbHelper.getInstance(context);
    }

    public List<Reminder> getReminders() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                Reminder._ID,
                Reminder.TIME,
                Reminder.IS_ENABLED
        };

        List<Reminder> reminders = new ArrayList<>();
        Cursor cursor = db.query(Reminder.TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(Reminder._ID));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(Reminder.TIME));
            int isEnabledInt = cursor.getInt(cursor.getColumnIndexOrThrow(Reminder.IS_ENABLED));
            boolean isEnabled = isEnabledInt != 0;
            reminders.add(new Reminder(id, time, isEnabled));
        }
        cursor.close();

        return reminders;
    }

    public void updateReminder(Reminder reminder) {
        long id = reminder.getId();
        if (!(id >= 1L && id <= 3L)) {
            throw new RuntimeException("Reminder attempting to be updated is out of bounds");
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String time = reminder.getTime();
        int isEnabled = reminder.getEnabled() ? 1 : 0;

        ContentValues values = new ContentValues();
        values.put(Reminder.TIME, time);
        values.put(Reminder.IS_ENABLED, isEnabled);

        String selection = Reminder._ID + " = ?";
        String[] selectionArgs = {Long.toString(id)};

        db.update(Reminder.TABLE_NAME, values, selection, selectionArgs);
    }

    @Override
    public long create(Reminder reminder) {
        // don't want to create reminders, only to update an existing static number of them
        return -1;
    }
}
