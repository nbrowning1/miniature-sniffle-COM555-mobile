package com.moodlogger.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.moodlogger.db.entities.User;

public class UserDbHelper implements DbHelperIntf<User> {

    private MoodDbHelper dbHelper;

    public UserDbHelper(Context context) {
        dbHelper = MoodDbHelper.getInstance(context);
    }

    @Override
    public long create(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.NAME, user.getName());

        return db.insert(User.TABLE_NAME, null, values);
    }
}
