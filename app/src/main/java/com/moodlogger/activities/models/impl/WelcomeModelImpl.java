package com.moodlogger.activities.models.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.moodlogger.activities.ActivityUtils;
import com.moodlogger.activities.models.intf.WelcomeModel;

public class WelcomeModelImpl implements WelcomeModel {

    private Context context;

    public WelcomeModelImpl(Context context) {
        this.context = context;
    }

    @Override
    public void finish(final String name, final OnWelcomeFinishedListener listener) {
        if (!ActivityUtils.textInputIsValid(name)) {
            listener.onValidationError();
            return;
        }
        saveNameAndMarkWelcomeGiven(name);
        listener.onSuccess();
    }

    void saveNameAndMarkWelcomeGiven(String name) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_name", name);
        editor.putBoolean("welcome_given", true);
        // commit as we want to save this immediately - can show up in immediate hint
        editor.commit();
    }
}
