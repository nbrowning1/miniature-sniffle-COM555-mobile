package com.moodlogger.activities.models.intf;

public interface CustomiseModel {

    interface OnCustomiseFinishedListener {

        void onValidationError();

        void onNameChangesSaved();

        void onThemeSaved();

        void onFontChangeSaved();
    }

    boolean validateAndSaveName(String oldName, String newName, CustomiseModel.OnCustomiseFinishedListener listener);

    void setNewTheme(int themeId, CustomiseModel.OnCustomiseFinishedListener listener);

    void setLargeFont(boolean isLargeFont, CustomiseModel.OnCustomiseFinishedListener listener);
}
