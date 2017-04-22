package com.moodlogger.activities.views.impl;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.moodlogger.HourAndMinsTime;
import com.moodlogger.R;
import com.moodlogger.activities.AbstractMoodActivity;
import com.moodlogger.activities.ActivityUtils;
import com.moodlogger.activities.presenters.impl.SettingsPresenterImpl;
import com.moodlogger.activities.presenters.intf.SettingsPresenter;
import com.moodlogger.activities.views.intf.SettingsView;
import com.moodlogger.db.entities.Reminder;
import com.moodlogger.db.helpers.ReminderDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SettingsActivity extends AbstractMoodActivity implements SettingsView {

    private static int debugHintsCount = 0;

    private static final int NO_OF_REMINDERS = 3;

    private List<Reminder> initialReminders;

    private SettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        populateReminders();
        initialReminders = getRemindersFromScreen();
        setSpecificViewThemes(isDarkTheme());

        presenter = new SettingsPresenterImpl(this, this);

        // TODO: change to sharedPreferences
        if (debugHintsCount < 1) {
            debugHintsCount++;
            showHint();
        }
    }

    private void showHint() {
        ActivityUtils.showHintDialog(this,
                getResources().getString(R.string.settings_hint_title),
                getResources().getString(R.string.settings_hint_message));
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.settings;
    }

    protected void setSpecificViewThemes(boolean isDarkTheme) {
        final int settingsSectionResId = isDarkTheme ?
                R.drawable.dark_settings_section_bg :
                R.drawable.settings_section_bg;
        setViewThemes(3, settingsSectionResId, "settings_section_");

        final int nestedScrollResId = isDarkTheme ?
                R.drawable.dark_nested_scroll_view_bg :
                R.drawable.nested_scroll_view_bg;
        setViewThemes(6, nestedScrollResId, "settings_nested_scroll_");
    }

    public void showTimePicker(View view) {
        HourAndMinsTime time = new HourAndMinsTime(
                ((Button) view).getText().toString()
        );

        TimePickerDialog tpd = new TimePickerDialog(SettingsActivity.this, timePickerOnSetListener(view.getId()), time.getHour(), time.getMinute(), true);
        tpd.setTitle("");
        tpd.show();
    }

    private TimePickerDialog.OnTimeSetListener timePickerOnSetListener(final int buttonResId) {
        return new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);

                String timeSet = getTimeWithLeadingZero(hourOfDay) + ":" + getTimeWithLeadingZero(minute);
                ((TextView) findViewById(buttonResId)).setText(timeSet);
            }
        };
    }

    private String getTimeWithLeadingZero(int time) {
        return time < 10 ?
                "0" + time :
                Integer.toString(time);
    }

    public void exportEntries(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, PackageManager.PERMISSION_GRANTED);
            return;
        }
        String storagePath = Environment.getExternalStorageDirectory().toString() + "/mood";

        presenter.exportMoodEntries(storagePath);
    }

    @Override
    public void onFailedExport() {
        Toast.makeText(this, "Something went wrong when trying to save export file", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessfulExport() {
        Toast.makeText(this, "Excel sheet generated in 'mood/' directory", Toast.LENGTH_LONG).show();
    }

    private void populateReminders() {
        List<Reminder> reminders = new ReminderDbHelper(this).getReminders();
        Reminder firstReminder = reminders.get(0);
        Reminder secondReminder = reminders.get(1);
        Reminder thirdReminder = reminders.get(2);
        populateReminder(firstReminder, R.id.time_picker_1, R.id.reminder_enabled_1);
        populateReminder(secondReminder, R.id.time_picker_2, R.id.reminder_enabled_2);
        populateReminder(thirdReminder, R.id.time_picker_3, R.id.reminder_enabled_3);
    }

    private void populateReminder(Reminder reminder, int timePickerResId, int switchResId) {
        ((Button) findViewById(timePickerResId)).setText(reminder.getTime());
        ((Switch) findViewById(switchResId)).setChecked(reminder.isEnabled());
    }

    private void saveReminders() {
        // no changes, no need to waste resources updating anything
        if (!changesMade()) {
            return;
        }

        presenter.saveReminders(getRemindersFromScreen());
    }

    @Override
    public void onRemindersSaved() {
        Toast.makeText(this, "Changes saved.", Toast.LENGTH_LONG).show();
    }

    private List<Reminder> getRemindersFromScreen() {
        List<Reminder> reminders = new ArrayList<>();
        reminders.add(getReminderFromScreen(1, R.id.time_picker_1, R.id.reminder_enabled_1));
        reminders.add(getReminderFromScreen(2, R.id.time_picker_2, R.id.reminder_enabled_2));
        reminders.add(getReminderFromScreen(3, R.id.time_picker_3, R.id.reminder_enabled_3));
        return reminders;
    }

    private Reminder getReminderFromScreen(int reminderInstance, int timePickerResId, int switchResId) {
        String time = ((Button) findViewById(timePickerResId)).getText().toString();
        boolean isEnabled = ((CompoundButton) findViewById(switchResId)).isChecked();
        return new Reminder(reminderInstance, time, isEnabled);
    }

    private boolean changesMade() {
        List<Reminder> updatedReminders = getRemindersFromScreen();
        for (int i = 0; i < NO_OF_REMINDERS; i++) {
            boolean remindersEqual = initialReminders.get(i).equals(updatedReminders.get(i));
            if (!remindersEqual) {
                return true;
            }
        }
        // if all reminders equal, no changes made
        return false;
    }

    public void goToCustomise(View view) {
        Intent intent = new Intent(this, CustomiseActivity.class);
        startActivity(intent);
    }

    public void goToAbout(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        saveReminders();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveReminders();
                break;
        }
        return (super.onOptionsItemSelected(item));
    }
}
