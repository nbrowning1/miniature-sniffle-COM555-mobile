package com.moodlogger.activities.presenters.impl;

import android.content.Context;

import com.moodlogger.activities.models.impl.SettingsModelImpl;
import com.moodlogger.activities.models.intf.SettingsModel;
import com.moodlogger.activities.presenters.intf.SettingsPresenter;
import com.moodlogger.activities.views.intf.SettingsView;
import com.moodlogger.db.entities.Reminder;

import java.util.List;

public class SettingsPresenterImpl implements SettingsPresenter, SettingsModel.OnSettingsFinishedListener {

    private SettingsView settingsView;
    private SettingsModel settingsModel;

    public SettingsPresenterImpl(SettingsView settingsView, Context context) {
        this.settingsView = settingsView;
        this.settingsModel = new SettingsModelImpl(context);
    }

    @Override
    public void onDestroy() {
        settingsView = null;
    }

    @Override
    public void exportMoodEntries(String storagePath) {
        settingsModel.exportMoodEntries(storagePath, this);
    }

    @Override
    public void saveReminders(List<Reminder> reminders) {
        settingsModel.saveReminders(reminders, this);
    }

    @Override
    public void onFailedExport() {
        if (settingsView != null) {
            settingsView.onFailedExport();
        }
    }

    @Override
    public void onSuccessfulExport() {
        if (settingsView != null) {
            settingsView.onSuccessfulExport();
        }
    }

    @Override
    public void onSavedReminders() {
        if (settingsView != null) {
            settingsView.onRemindersSaved();
        }
    }
}
