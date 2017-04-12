package com.moodlogger.activities.models.intf;


import com.moodlogger.db.entities.Reminder;

import java.util.List;

public interface SettingsModel {

    interface OnSettingsFinishedListener {

        void onFailedExport();

        void onSuccessfulExport();

        void onSavedReminders();
    }

    void exportMoodEntries(String storagePath, SettingsModel.OnSettingsFinishedListener listener);

    void saveReminders(List<Reminder> reminders, SettingsModel.OnSettingsFinishedListener listener);
}
