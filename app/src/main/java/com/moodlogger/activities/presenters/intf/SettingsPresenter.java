package com.moodlogger.activities.presenters.intf;

import com.moodlogger.db.entities.Reminder;

import java.util.List;

public interface SettingsPresenter {

    void exportMoodEntries(String storagePath);

    void saveReminders(List<Reminder> reminders);

    void onDestroy();
}
