package com.moodlogger.charts;

import android.content.Context;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.helpers.MoodEntryDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BarChartHelper implements ChartHelper {

    private TimeRangeEnum timeRange;
    private String[] moodValues;
    private Context context;

    public BarChartHelper(TimeRangeEnum timeRange, String[] moodValues, Context context) {
        this.timeRange = timeRange;
        this.moodValues = moodValues;
        this.context = context;
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
        dataSet.setDrawValues(false);
        BarData barData = new BarData(dataSet);
        chart.setData(barData);

        setXAxis(chart);
        setYAxis(chart);

        chart.invalidate(); // refresh chart
    }

    private void setXAxis(BarChart chart) {
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(6f);
        xAxis.setGranularity(1);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(TimeRangeEnum.Week.getTimeRangeValues()));
    }

    private void setYAxis(BarChart chart) {
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(4f);
        yAxis.setGranularity(1);
        yAxis.setValueFormatter(new IndexAxisValueFormatter(moodValues));

        // hide right axis - only want left
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private List<BarEntry> getEntries() {
        List<BarEntry> entries = new ArrayList<>();
        List<MoodEntry> moodEntries = new MoodEntryDbHelper(context).getMoodEntries();

        for (MoodEntry moodEntry : moodEntries) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
            long startWeekTime = cal.getTimeInMillis();
            cal.add(Calendar.WEEK_OF_YEAR, 1);
            long endWeekTime = cal.getTimeInMillis();

            // find out how far we are through the week
            long moodEntryTime = moodEntry.getFormattedDate().getTimeInMillis();
            long timeThroughWeek = moodEntryTime - startWeekTime;
            long totalWeekTime = endWeekTime - startWeekTime;

            // convert to plot on x-axis using range of available values
            float xAxisPlot = ((float) timeThroughWeek / (float) totalWeekTime) * TimeRangeEnum.Week.getTimeRangeValues().length;

            // add graph plot
            entries.add(new BarEntry(xAxisPlot, moodEntry.getMoodId()));
        }

        return entries;
    }
}
