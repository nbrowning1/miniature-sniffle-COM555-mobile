package com.moodlogger.activities.models.impl;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.moodlogger.HourAndMinsTime;
import com.moodlogger.R;
import com.moodlogger.reminders.ReminderReceiver;
import com.moodlogger.SpreadsheetBuilder;
import com.moodlogger.activities.models.intf.SettingsModel;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.entities.Reminder;
import com.moodlogger.db.helpers.MoodEntryDbHelper;
import com.moodlogger.db.helpers.ReminderDbHelper;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class SettingsModelImpl implements SettingsModel {

    private Context context;

    public SettingsModelImpl(Context context) {
        this.context = context;
    }

    @Override
    public void exportMoodEntries(String storagePath, OnSettingsFinishedListener listener) {
        boolean exportSuccessful = saveTimestampedExportFile(storagePath);

        if (exportSuccessful) {
            listener.onSuccessfulExport();
        } else {
            listener.onFailedExport();
        }
    }

    boolean saveTimestampedExportFile(String storagePath) {
        HSSFWorkbook workbook = buildMoodEntriesSpreadsheet();

        boolean exportSuccessful = true;
        FileOutputStream fos = null;

        try {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            File storageFile = new File(storagePath);
            storageFile.mkdirs();

            File file = new File(storageFile, context.getString(R.string.app_name) + timeStamp + ".xls");
            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
            exportSuccessful = false;
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return exportSuccessful;
    }

    private HSSFWorkbook buildMoodEntriesSpreadsheet() {
        List<MoodEntry> moodEntries = new MoodEntryDbHelper(context).getAllMoodEntries();
        return SpreadsheetBuilder.buildMoodEntriesSpreadsheet(moodEntries);
    }

    @Override
    public void saveReminders(List<Reminder> reminders, OnSettingsFinishedListener listener) {
        ReminderDbHelper reminderDbHelper = new ReminderDbHelper(context);
        for (Reminder reminder : reminders) {
            reminderDbHelper.updateReminder(reminder);
        }
        setReminders(reminders);

        listener.onSavedReminders();
    }

    private void setReminders(List<Reminder> reminders) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);

        for (int i = 0; i < reminders.size(); i++) {
            Reminder reminder = reminders.get(i);
            HourAndMinsTime time = new HourAndMinsTime(reminder.getTime());

            /* loop iteration used as unique request code for pendingIntent so existing alarm can
                be cancelled if applicable */
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (!reminder.isEnabled()) {
                alarmManager.cancel(pendingIntent);
                // skip to next reminder
                continue;
            }

            Calendar alarmStartTime = Calendar.getInstance();
            alarmStartTime.set(Calendar.HOUR_OF_DAY, time.getHour());
            alarmStartTime.set(Calendar.MINUTE, time.getMinute());
            alarmStartTime.set(Calendar.SECOND, 0);

            Calendar now = Calendar.getInstance();
            // if we're already past the alarm time, forward by one day to avoid immediate alarm trigger
            if (now.after(alarmStartTime)) {
                alarmStartTime.add(Calendar.DATE, 1);
            }

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}
