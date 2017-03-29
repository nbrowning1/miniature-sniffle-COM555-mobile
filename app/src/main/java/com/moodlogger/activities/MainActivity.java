package com.moodlogger.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TabHost;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.moodlogger.R;
import com.moodlogger.charts.BarChartHelper;
import com.moodlogger.charts.ChartTypeEnum;
import com.moodlogger.charts.LineChartHelper;
import com.moodlogger.charts.TimeRangeEnum;
import com.moodlogger.db.MoodDbContract;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.entities.User;
import com.moodlogger.db.helpers.MoodDbHelper;
import com.moodlogger.db.helpers.MoodEntryDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAB_ONE_NAME = "Summary";
    private static final String TAB_TWO_NAME = "View";
    private static final String TAB_THREE_NAME = "Evaluate";

    private Spinner timeRangeSpinner;
    private Spinner chartTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupTabs();
        setupSpinners();
        buildChart();
    }

    private void setupTabs() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        setupTab(tabHost, TAB_ONE_NAME, R.id.tab1);
        setupTab(tabHost, TAB_TWO_NAME, R.id.tab2);
        setupTab(tabHost, TAB_THREE_NAME, R.id.tab3);
    }

    private void setupTab(TabHost tabHost, String tabName, int id) {
        TabHost.TabSpec tabSpecTab = tabHost.newTabSpec(tabName);
        tabSpecTab.setContent(id);
        tabSpecTab.setIndicator(tabName);
        tabHost.addTab(tabSpecTab);
    }

    private void setupSpinners() {
        timeRangeSpinner = (Spinner) findViewById(R.id.time_range_spinner);
        chartTypeSpinner = (Spinner) findViewById(R.id.chart_type_spinner);

        AdapterView.OnItemSelectedListener valueSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                buildChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        };

        timeRangeSpinner.setOnItemSelectedListener(valueSelectedListener);
        chartTypeSpinner.setOnItemSelectedListener(valueSelectedListener);
    }

    private void buildChart() {
        View chartView = findViewById(R.id.chart);
        TimeRangeEnum timeRange = TimeRangeEnum.getEnum(timeRangeSpinner.getSelectedItem().toString());
        ChartTypeEnum chartType = ChartTypeEnum.getChartType(chartTypeSpinner.getSelectedItem().toString());
        String[] moodValues = getResources().getStringArray(R.array.graph_mood_values);

        if (chartType.equals(ChartTypeEnum.Line)) {
            new LineChartHelper(timeRange, moodValues, getBaseContext()).buildChart(chartView);
        } else if (chartType.equals(ChartTypeEnum.Bar)) {
            new BarChartHelper(timeRange, moodValues, getBaseContext()).buildChart(chartView);
        }
    }

    public void addMoodLog(View view) {
        Intent intent = new Intent(MainActivity.this, AddMoodLogActivity.class);
        startActivity(intent);
    }
}
