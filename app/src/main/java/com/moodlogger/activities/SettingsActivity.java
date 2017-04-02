package com.moodlogger.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.moodlogger.R;
import com.moodlogger.db.entities.Reminder;
import com.moodlogger.db.helpers.ReminderDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        populateReminders();
    }

    public void showTimePicker(View view) {
        Calendar cal = Calendar.getInstance();
        TimePickerDialog tp1 = new TimePickerDialog(SettingsActivity.this, timePickerOnSetListener(view.getId()), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        tp1.show();
    }

    private View.OnClickListener timeButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                TimePickerDialog tp1 = new TimePickerDialog(SettingsActivity.this, timePickerOnSetListener(v.getId()), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
                tp1.show();
            }
        };
    }

    private TimePickerDialog.OnTimeSetListener timePickerOnSetListener(final int buttonResId) {
        return new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                String timeSet = hourOfDay + ":" + minute;
                ((TextView) findViewById(buttonResId)).setText(timeSet);
            }
        };
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
        ((Switch) findViewById(switchResId)).setChecked(reminder.getEnabled());
    }

    private void saveReminders() {
        ReminderDbHelper reminderDbHelper = new ReminderDbHelper(this);
        List<Reminder> reminders = new ArrayList<>();
        reminders.add(getReminderFromScreen(1, R.id.time_picker_1, R.id.reminder_enabled_1));
        reminders.add(getReminderFromScreen(2, R.id.time_picker_2, R.id.reminder_enabled_2));
        reminders.add(getReminderFromScreen(3, R.id.time_picker_3, R.id.reminder_enabled_3));
        for (Reminder reminder : reminders) {
            reminderDbHelper.updateReminder(reminder);
        }
        Toast.makeText(this, "Changes saved.",
                Toast.LENGTH_LONG).show();
    }

    private Reminder getReminderFromScreen(int reminderInstance, int timePickerResId, int switchResId) {
        String time = ((Button) findViewById(timePickerResId)).getText().toString();
        boolean isEnabled = ((CompoundButton) findViewById(switchResId)).isChecked();
        return new Reminder(reminderInstance, time, isEnabled);
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
        return(super.onOptionsItemSelected(item));
    }
}
