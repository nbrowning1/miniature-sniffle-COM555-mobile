package com.moodlogger.activities.presenters.intf;

public interface CustomisePresenter {

    boolean validateAndSaveName(String oldName, String newName);

    void setNewTheme(int themeId);

    void onDestroy();
}
