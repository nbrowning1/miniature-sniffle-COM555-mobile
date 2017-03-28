package com.moodlogger.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.moodlogger.db.entities.Entity;
import com.moodlogger.db.entities.User;

public class UserDbHelper extends MoodDbHelper implements DbHelperIntf<User> {

    public UserDbHelper(Context context) {
        super(context);
    }

//    public User getById(long id) {
//        SQLiteDatabase db = getReadableDatabase();
//
//        String[] columns = {
//                User.NAME
//        };
//
//        String selection = User._ID + " = ?";
//        String[] selectionArgs = { Long.toString(id) };
//
//        Cursor cursor = db.query(User.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
//        cursor.moveToNext();
//        String username = cursor.getString(cursor.getColumnIndexOrThrow(User.NAME));
//        cursor.close();
//
//        return new User(username);
//    }

    @Override
    public long create(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.NAME, user.getName());

        return db.insert(User.TABLE_NAME, null, values);
    }

//    public void delete(long id) {
//        SQLiteDatabase db = getWritableDatabase();
//
//        String selection = User._ID + " LIKE ?";
//        String[] selectionArgs = { Long.toString(id) };
//        db.delete(User.TABLE_NAME, selection, selectionArgs);
//    }
}
