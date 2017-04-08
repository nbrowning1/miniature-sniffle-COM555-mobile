package com.moodlogger.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.moodlogger.HourAndMinsTime;
import com.moodlogger.MoodEnum;
import com.moodlogger.R;
import com.moodlogger.ReminderReceiver;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.entities.Reminder;
import com.moodlogger.db.helpers.MoodEntryDbHelper;
import com.moodlogger.db.helpers.ReminderDbHelper;

import org.apache.poi.hssf.usermodel.HSSFAnchor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.charts.AxisCrosses;
import org.apache.poi.ss.usermodel.charts.AxisPosition;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.ss.usermodel.charts.ChartLegend;
import org.apache.poi.ss.usermodel.charts.DataSources;
import org.apache.poi.ss.usermodel.charts.LegendPosition;
import org.apache.poi.ss.usermodel.charts.LineChartData;
import org.apache.poi.ss.usermodel.charts.ValueAxis;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private static final int NO_OF_REMINDERS = 3;

    private List<Reminder> initialReminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        populateReminders();
        initialReminders = getRemindersFromScreen();
    }

    public void showTimePicker(View view) {
        HourAndMinsTime time = new HourAndMinsTime(
                ((Button) view).getText().toString()
        );

        TimePickerDialog tpd = new TimePickerDialog(SettingsActivity.this, timePickerOnSetListener(view.getId()), time.getHour(), time.getMinute(), true);
        tpd.show();
    }

    public void exportEntries(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, PackageManager.PERMISSION_GRANTED);
            return;
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet firstSheet = workbook.createSheet("Moods");
        firstSheet.setColumnWidth(0, 4000);
        firstSheet.setColumnWidth(1, 4000);

        HSSFFont defaultFont = workbook.createFont();
        defaultFont.setFontHeightInPoints((short) 10);
        defaultFont.setFontName("Arial");
        defaultFont.setColor(IndexedColors.BLACK.getIndex());
        defaultFont.setBold(false);
        defaultFont.setItalic(false);

        HSSFFont titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 12);
        titleFont.setFontName("Arial");
        titleFont.setColor(IndexedColors.BLACK.getIndex());
        titleFont.setBold(true);
        titleFont.setItalic(false);

        HSSFCellStyle defaultCellStyle = workbook.createCellStyle();
        defaultCellStyle.setFont(defaultFont);
        defaultCellStyle.setWrapText(true);
        HSSFCellStyle titleCellStyle = workbook.createCellStyle();
        titleCellStyle.setFont(titleFont);

        HSSFRow titleRow = firstSheet.createRow(0);
        HSSFCell moodTypeCell = titleRow.createCell(0);
        moodTypeCell.setCellValue(new HSSFRichTextString("Mood Type"));
        moodTypeCell.setCellStyle(titleCellStyle);
        HSSFCell dateCell = titleRow.createCell(1);
        dateCell.setCellValue(new HSSFRichTextString("Date"));
        dateCell.setCellStyle(titleCellStyle);

        List<MoodEntry> moodEntries = new MoodEntryDbHelper(this).getAllMoodEntries();
        for (int i = 1; i <= moodEntries.size(); i++) {
            MoodEntry moodEntry = moodEntries.get(i - 1);
            HSSFRow row = firstSheet.createRow(i);
            HSSFCell moodEntryMoodTypeCell = row.createCell(0);
            moodEntryMoodTypeCell.setCellValue(new HSSFRichTextString(MoodEnum.getLabelName(moodEntry.getMoodId())));
            moodEntryMoodTypeCell.setCellStyle(defaultCellStyle);
            HSSFCell moodEntryDateTimeCell = row.createCell(1);
            moodEntryDateTimeCell.setCellValue(new HSSFRichTextString(moodEntry.getDateTime()));
            moodEntryDateTimeCell.setCellStyle(defaultCellStyle);
        }

        createPieChart(firstSheet, moodEntries.size() + 1);

        FileOutputStream fos = null;

        try {
            String storagePath = Environment.getExternalStorageDirectory().toString();
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            File file = new File(storagePath, getString(R.string.app_name) + timeStamp + ".xls");
            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(this, "Excel Sheet Generated", Toast.LENGTH_LONG).show();
        }
    }

    private void createPieChart(HSSFSheet sheet, int noOfRows) {
        final int noOfColumns = 2;
        final int rowStart = noOfRows + 2;

        HSSFPatriarch drawing = sheet.createDrawingPatriarch();
        HSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, rowStart, 10, rowStart + 10);

        Chart chart = drawing.createChart(anchor);
        ChartLegend legend = chart.getOrCreateLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);

        LineChartData data = chart.getChartDataFactory().createLineChartData();

        // Use a category axis for the bottom axis.
        ChartAxis bottomAxis = chart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);
        ValueAxis leftAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

        ChartDataSource<Number> xs = DataSources.fromNumericCellRange(sheet, new CellRangeAddress(1, 1, 0, noOfColumns - 1));
        ChartDataSource<Number> ys1 = DataSources.fromNumericCellRange(sheet, new CellRangeAddress(2, 2, 0, noOfColumns - 1));
        ChartDataSource<Number> ys2 = DataSources.fromNumericCellRange(sheet, new CellRangeAddress(3, 3, 0, noOfColumns - 1));


        data.addSeries(xs, ys1);
        data.addSeries(xs, ys2);

        chart.plot(data, bottomAxis, leftAxis);

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

        ReminderDbHelper reminderDbHelper = new ReminderDbHelper(this);

        List<Reminder> reminders = getRemindersFromScreen();
        for (Reminder reminder : getRemindersFromScreen()) {
            reminderDbHelper.updateReminder(reminder);
        }

        setReminders(reminders);

        Toast.makeText(this, "Changes saved.",
                Toast.LENGTH_LONG).show();
    }

    private void setReminders(List<Reminder> reminders) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);

        for (int i = 0; i < NO_OF_REMINDERS; i++) {
            Reminder reminder = reminders.get(i);
            HourAndMinsTime time = new HourAndMinsTime(reminder.getTime());

            /* loop iteration used as unique request code for pendingIntent so existing alarm can
                be cancelled if applicable */
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);

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
