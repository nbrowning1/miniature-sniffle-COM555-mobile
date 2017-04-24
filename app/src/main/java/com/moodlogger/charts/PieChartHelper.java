package com.moodlogger.charts;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.moodlogger.db.entities.MoodEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieChartHelper implements ChartHelper {

    private String[] moodValues;
    private boolean isDarkTheme;
    private boolean isLargeFont;
    private List<MoodEntry> moodEntries;

    public PieChartHelper(String[] moodValues, boolean isDarkTheme, boolean isLargeFont, List<MoodEntry> moodEntries) {
        this.moodValues = moodValues;
        this.isDarkTheme = isDarkTheme;
        this.isLargeFont = isLargeFont;
        this.moodEntries = moodEntries;
    }

    public void buildChart(View chartView) {
        PieChart chart = (PieChart) chartView;

        Description emptyDescription = new Description();
        emptyDescription.setText("");
        chart.setDescription(emptyDescription);

        List<PieEntry> entries = getEntries();

        PieDataSet dataSet = new PieDataSet(entries, "Mood");
        int lightGreen = Color.rgb(153, 255, 51);
        // angry -> great mood colours for pie slices
        final int[] moodColours = new int[]{Color.RED, Color.BLUE, Color.GRAY, Color.GREEN, lightGreen};
        // text colours to contrast against pie slice colours declared above
        List<Integer> textColours = Arrays.asList(Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, Color.BLACK);
        dataSet.setColors(moodColours);
        dataSet.setValueTextColors(textColours);
        if (isLargeFont) {
            dataSet.setValueTextSize(16f);
        } else {
            dataSet.setValueTextSize(10f);
        }
        // custom value formatter to display mood text instead of counts on pie chart slices
        dataSet.setValueFormatter(new IValueFormatter() {
            // keep track of index manually as pie chart only has one x-value (0)
            int pieChartIndex = 0;

            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                String moodText = value == 0f ? "" : moodValues[pieChartIndex];
                pieChartIndex++;

                /* each click on pie chart will cause it to be re-drawn after initial draw -
                    set index back to 0 for re-drawing each time the whole chart is drawn */
                if (pieChartIndex >= moodValues.length) {
                    pieChartIndex = 0;
                }
                return moodText;
            }
        });
        dataSet.setSliceSpace(10f);
        PieData pieData = new PieData(dataSet);
        chart.setData(pieData);
        chart.setHoleColor(Color.TRANSPARENT);
        if (isDarkTheme) {
            chart.getLegend().setTextColor(Color.WHITE);
        }
        if (isLargeFont) {
            chart.getLegend().setTextSize(16f);
        }

        chart.invalidate(); // refresh chart
    }

    private List<PieEntry> getEntries() {
        List<PieEntry> entries = new ArrayList<>();
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
            entries.add(new PieEntry(moodIdsAndCount.get(i), i));
        }

        return entries;
    }
}
