package com.moodlogger.charts;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.moodlogger.db.entities.MoodEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarChartHelper implements ChartHelper {

    private String[] moodValues;
    private boolean isDarkTheme;
    private boolean isLargeFont;
    private List<MoodEntry> moodEntries;

    public BarChartHelper(String[] moodValues, boolean isDarkTheme, boolean isLargeFont, List<MoodEntry> moodEntries) {
        this.moodValues = moodValues;
        this.isDarkTheme = isDarkTheme;
        this.isLargeFont = isLargeFont;
        this.moodEntries = moodEntries;
    }

    public void buildChart(View chartView) {
        BarChart chart = (BarChart) chartView;
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);

        Description emptyDescription = new Description();
        emptyDescription.setText("");
        chart.setDescription(emptyDescription);

        List<BarEntry> entries = getEntries();

        BarDataSet dataSet = new BarDataSet(entries, "Mood");
        int lightGreen = Color.rgb(153, 255, 51);
        // angry -> great mood colours for bars
        int[] moodColours = new int[]{Color.RED, Color.BLUE, Color.GRAY, Color.GREEN, lightGreen};
        dataSet.setColors(moodColours);
        dataSet.setDrawValues(false);
        BarData barData = new BarData(dataSet);
        chart.setData(barData);

        setXAxis(chart);
        setYAxis(chart);
        if (isDarkTheme) {
            chart.getLegend().setTextColor(Color.WHITE);
        }
        if (isLargeFont) {
            chart.getLegend().setTextSize(16f);
        }

        chart.invalidate(); // refresh chart
    }

    private void setXAxis(BarChart chart) {
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // extra 0.5 allowance on each side so that bars are displayed at full-width
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(4.5f);
        xAxis.setGranularity(1);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(moodValues));
        if (isDarkTheme) {
            xAxis.setTextColor(Color.WHITE);
        }
        if (isLargeFont) {
            xAxis.setLabelRotationAngle(270);
            xAxis.setTextSize(16f);
        }
    }

    private void setYAxis(BarChart chart) {
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setGranularity(1);
        if (isDarkTheme) {
            yAxis.setTextColor(Color.WHITE);
        }
        if (isLargeFont) {
            yAxis.setTextSize(16f);
        }

        // hide right axis - only want left
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private List<BarEntry> getEntries() {
        List<BarEntry> entries = new ArrayList<>();
        Map<Integer, Integer> moodIdsAndCount = new HashMap<>();

        // initialise map with 0 count for moods
        for (int i = 0; i < moodValues.length; i++) {
            moodIdsAndCount.put(i, 0);
        }

        for (MoodEntry moodEntry : moodEntries) {
            int count = moodIdsAndCount.get(moodEntry.getMoodId());
            moodIdsAndCount.put(moodEntry.getMoodId(), count + 1);
        }

        for (int i = 0; i < moodValues.length; i++) {
            // add graph plot
            entries.add(new BarEntry(i, moodIdsAndCount.get(i)));
        }

        return entries;
    }
}
