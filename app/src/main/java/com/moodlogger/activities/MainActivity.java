package com.moodlogger.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.moodlogger.db.MoodDbContract;
import com.moodlogger.db.entities.User;
import com.moodlogger.db.helpers.MoodDbHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAB_ONE_NAME = "Summary";
    private static final String TAB_TWO_NAME = "View";
    private static final String TAB_THREE_NAME = "Evaluate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildChart();
        setupTabs();
    }

    private void setupTabs() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpecTab1 = tabHost.newTabSpec(TAB_ONE_NAME);
        tabSpecTab1.setContent(R.id.tab1);
        tabSpecTab1.setIndicator(TAB_ONE_NAME);
        tabHost.addTab(tabSpecTab1);

        TabHost.TabSpec tabSpecTab2 = tabHost.newTabSpec(TAB_TWO_NAME);
        tabSpecTab2.setContent(R.id.tab2);
        tabSpecTab2.setIndicator(TAB_TWO_NAME);
        tabHost.addTab(tabSpecTab2);

        TabHost.TabSpec tabSpecTab3 = tabHost.newTabSpec(TAB_THREE_NAME);
        tabSpecTab3.setContent(R.id.tab3);
        tabSpecTab3.setIndicator(TAB_THREE_NAME);
        tabHost.addTab(tabSpecTab3);
    }

    private void buildChart() {
        LineChart chart = (LineChart) findViewById(R.id.chart);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);

        Description emptyDescription = new Description();
        emptyDescription.setText("");
        chart.setDescription(emptyDescription);

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 4));
        entries.add(new Entry(1, 3));
        entries.add(new Entry(2, 1));
        entries.add(new Entry(3, 3));
        entries.add(new Entry(4, 2));

        LineDataSet dataSet = new LineDataSet(entries, "Mood");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        setXAxis(chart);
        setYAxis(chart);

        chart.invalidate(); // refresh chart
    }

    private void setXAxis(LineChart chart) {
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(6f);
        xAxis.setGranularity(1);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getResources().getStringArray(R.array.graph_daily_values)));
    }

    private void setYAxis(LineChart chart) {
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(4f);
        yAxis.setGranularity(1);
        yAxis.setValueFormatter(new IndexAxisValueFormatter(getResources().getStringArray(R.array.graph_mood_values)));

        // hide right axis - only want left
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    public void addMoodLog(View view) {
        Intent intent = new Intent(MainActivity.this, AddMoodLogActivity.class);
        startActivity(intent);
    }
}
