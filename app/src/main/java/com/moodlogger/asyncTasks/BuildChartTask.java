package com.moodlogger.asyncTasks;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.moodlogger.R;
import com.moodlogger.activities.ActivityUtils;
import com.moodlogger.charts.BarChartHelper;
import com.moodlogger.charts.ChartTypeEnum;
import com.moodlogger.charts.LineChartHelper;
import com.moodlogger.charts.PieChartHelper;
import com.moodlogger.enums.TimeRangeEnum;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.helpers.MoodEntryDbHelper;

import java.util.List;

public class BuildChartTask extends AsyncTask<Void, Void, List<MoodEntry>> {

    private Context context;
    private LinearLayout parentView;
    private Resources resources;
    private boolean isDarkTheme;

    private TimeRangeEnum timeRange;
    private ChartTypeEnum chartType;

    public BuildChartTask(Context context, LinearLayout parentView, Resources resources) {
        this.context = context;
        this.parentView = parentView;
        this.resources = resources;
    }

    @Override
    protected void onPreExecute() {
        initialiseViewsToShowLoading();
        setValuesFromSpinners();
    }

    private void initialiseViewsToShowLoading() {
        parentView.findViewById(R.id.chart_parent).setVisibility(View.GONE);
        parentView.findViewById(R.id.chart_progress_spinner).setVisibility(View.VISIBLE);
    }

    private void setValuesFromSpinners() {
        String timeRangeValue = ((Spinner) parentView.findViewById(R.id.time_range_spinner))
                .getSelectedItem().toString();
        timeRange = TimeRangeEnum.getEnum(timeRangeValue);

        String chartTypeValue = ((Spinner) parentView.findViewById(R.id.chart_type_spinner))
                .getSelectedItem().toString();
        chartType = ChartTypeEnum.getChartType(chartTypeValue);
    }

    @Override
    protected List<MoodEntry> doInBackground(Void... params) {
        isDarkTheme = ActivityUtils.isDarkTheme(context);
        return new MoodEntryDbHelper(context).getMoodEntries(timeRange);
    }

    @Override
    protected void onPostExecute(List<MoodEntry> moodEntries) {
        buildChart(moodEntries);

        // hide progress spinner and show chart
        parentView.findViewById(R.id.chart_progress_spinner).setVisibility(View.GONE);
        parentView.findViewById(R.id.chart_parent).setVisibility(View.VISIBLE);
    }

    private void buildChart(List<MoodEntry> moodEntries) {
        String[] moodValues = resources.getStringArray(R.array.mood_values);

        buildChart(chartType);
        View chartView = parentView.findViewById(R.id.chart);

        if (chartType.equals(ChartTypeEnum.Line)) {
            new LineChartHelper(timeRange, moodValues, isDarkTheme, moodEntries).buildChart(chartView);
        } else if (chartType.equals(ChartTypeEnum.Bar)) {
            new BarChartHelper(moodValues, isDarkTheme, moodEntries).buildChart(chartView);
        } else if (chartType.equals(ChartTypeEnum.Pie)) {
            new PieChartHelper(moodValues, isDarkTheme, moodEntries).buildChart(chartView);
        }
    }

    private void buildChart(ChartTypeEnum chartType) {
        LinearLayout chartParent = (LinearLayout) parentView.findViewById(R.id.chart_parent);
        chartParent.removeAllViews();
        View chart;

        int widthPx = ActivityUtils.dpToPixels(resources, 350);
        int heightPx = ActivityUtils.dpToPixels(resources, 350);
        LinearLayout.LayoutParams chartLayoutParams = new LinearLayout.LayoutParams(widthPx, heightPx);

        if (chartType.equals(ChartTypeEnum.Line)) {
            chart = new LineChart(context);
            chart.setId(R.id.chart);
            chart.setLayoutParams(chartLayoutParams);
        } else if (chartType.equals(ChartTypeEnum.Bar)) {
            chart = new BarChart(context);
            chart.setId(R.id.chart);
            chart.setLayoutParams(chartLayoutParams);
        } else {
            chart = new PieChart(context);
            chart.setId(R.id.chart);
            chart.setLayoutParams(chartLayoutParams);
        }

        chartParent.addView(chart);
    }
}
