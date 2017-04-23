package com.moodlogger.activities.models.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.moodlogger.activities.ActivityUtils;
import com.moodlogger.activities.models.intf.CustomiseModel;

public class CustomiseModelImpl implements CustomiseModel {

    private Context context;

    public CustomiseModelImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean validateAndSaveName(String oldName, String newName, OnCustomiseFinishedListener listener) {
        boolean changesMade = !oldName.equals(newName);

        if (!ActivityUtils.textInputIsValid(newName)) {
            listener.onValidationError();
            return false;
        }

        if (changesMade) {
            saveNameChange(newName);
            listener.onNameChangesSaved();
        }

        return true;
    }

    void saveNameChange(String name) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_name", name);

        /* apply as we're unlikely to run into anything needing the name - doesn't need to be
                be immediate */
        editor.apply();
    }

    @Override
    public void setNewTheme(int themeId, OnCustomiseFinishedListener listener) {
        saveThemeChange(themeId);
        listener.onThemeSaved();
    }

    void saveThemeChange(int themeId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("theme", themeId);

        // commit as we want to change theme in SharedPrefs immediately - huge visual feedback expected
        editor.commit();
    }
}
