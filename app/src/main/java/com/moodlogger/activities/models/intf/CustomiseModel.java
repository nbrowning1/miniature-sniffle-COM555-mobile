package com.moodlogger.activities.models.intf;

public interface CustomiseModel {

    interface OnCustomiseFinishedListener {

        void onValidationError();

        void onNameChangesSaved();

        void onThemeSaved();
    }

    boolean validateAndSaveName(String oldName, String newName, CustomiseModel.OnCustomiseFinishedListener listener);

    void setNewTheme(int themeId, CustomiseModel.OnCustomiseFinishedListener listener);
}
