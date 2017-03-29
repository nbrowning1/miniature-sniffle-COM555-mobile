package com.moodlogger.charts;

import android.content.Context;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.moodlogger.DateUtils;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.helpers.MoodEntryDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LineChartHelper implements ChartHelper {

    private TimeRangeEnum timeRange;
    private String[] moodValues;
    private Context context;

    public LineChartHelper(TimeRangeEnum timeRange, String[] moodValues, Context context) {
        this.timeRange = timeRange;
        this.moodValues = moodValues;
        this.context = context;
    }

    public void buildChart(View chartView) {
        LineChart chart = (LineChart) chartView;
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);

        Description emptyDescription = new Description();
        emptyDescription.setText("");
        chart.setDescription(emptyDescription);

        List<Entry> entries = getEntries();

        LineDataSet dataSet = new LineDataSet(entries, "Mood");
        dataSet.setDrawValues(false);
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
        xAxis.setAxisMaximum(timeRange.getTimeRangeValues().length);
        xAxis.setGranularity(1);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(timeRange.getTimeRangeValues()));
    }

    private void setYAxis(LineChart chart) {
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(4f);
        yAxis.setGranularity(1);
        yAxis.setValueFormatter(new IndexAxisValueFormatter(moodValues));

        // hide right axis - only want left
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private List<Entry> getEntries() {
        List<Entry> entries = new ArrayList<>();
        List<MoodEntry> moodEntries = new MoodEntryDbHelper(context).getMoodEntries(timeRange);

        for (MoodEntry moodEntry : moodEntries) {
            Calendar cal = Calendar.getInstance();
            long startPeriodTime;
            long endPeriodTime;

            if (timeRange.equals(TimeRangeEnum.Week)) {
                startPeriodTime = DateUtils.getStartOfWeek().getTimeInMillis();
                endPeriodTime = DateUtils.getEndOfWeek().getTimeInMillis();
            } else if (timeRange.equals(TimeRangeEnum.Month)) {
                startPeriodTime = DateUtils.getStartOfMonth().getTimeInMillis();
                endPeriodTime = DateUtils.getEndOfMonth().getTimeInMillis();
            } else {
                startPeriodTime = DateUtils.getStartOfYear().getTimeInMillis();
                endPeriodTime = DateUtils.getEndOfYear().getTimeInMillis();
            }

            // find out how far we are through the time period
            long moodEntryTime = moodEntry.getFormattedDate().getTimeInMillis();
            long timeThroughPeriod = moodEntryTime - startPeriodTime;
            long totalPeriodTime = endPeriodTime - startPeriodTime;

            // convert to plot on x-axis using range of available values
            float xAxisPlot = ((float) timeThroughPeriod / (float) totalPeriodTime) * timeRange.getTimeRangeValues().length;

            // add graph plot
            entries.add(new Entry(xAxisPlot, moodEntry.getMoodId()));
        }

        return entries;
    }
}
