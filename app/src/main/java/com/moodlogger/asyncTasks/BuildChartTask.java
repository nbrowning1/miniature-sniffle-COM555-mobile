package com.moodlogger.asyncTasks;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
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
import com.moodlogger.TimeRangeEnum;

public class BuildChartTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private LinearLayout parentView;
    private Resources resources;
    private boolean isDarkTheme;

    private Spinner timeRangeSpinner;
    private Spinner chartTypeSpinner;

    /* stuff to work around spinner's implementation quirks - known problem:
        http://stackoverflow.com/questions/5624825/spinner-onitemselected-executes-when-it-is-not-suppose-to/5918177#5918177 */
    private static final int NO_OF_SPINNERS_TO_WAIT_FOR_INIT = 1;
    private int noOfSpinnersInitialised;

    public BuildChartTask(Context context, LinearLayout parentView, Resources resources) {
        this.context = context;
        this.parentView = parentView;
        this.resources = resources;
        this.isDarkTheme = ActivityUtils.isDarkTheme(context);
    }

    @Override
    protected void onPreExecute() {
        parentView.findViewById(R.id.chart_parent).setVisibility(View.GONE);
        // chart will be built via spinners' onClick handler after spinners are initialised
        setupSpinners();
    }

    private void setupSpinners() {
        noOfSpinnersInitialised = 0;
        timeRangeSpinner = (Spinner) parentView.findViewById(R.id.time_range_spinner);
        chartTypeSpinner = (Spinner) parentView.findViewById(R.id.chart_type_spinner);

        AdapterView.OnItemSelectedListener valueSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                // won't actually run if method is called during initialisation of first spinner
                if (noOfSpinnersInitialised < NO_OF_SPINNERS_TO_WAIT_FOR_INIT) {
                    noOfSpinnersInitialised++;
                    return;
                }
                buildChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        };

        timeRangeSpinner.setOnItemSelectedListener(valueSelectedListener);
        chartTypeSpinner.setOnItemSelectedListener(valueSelectedListener);
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Void params) {
        // hide progress spinner and show chart
        parentView.findViewById(R.id.chart_progress_spinner).setVisibility(View.GONE);
        parentView.findViewById(R.id.chart_parent).setVisibility(View.VISIBLE);
    }

    private void buildChart() {
        TimeRangeEnum timeRange = TimeRangeEnum.getEnum(timeRangeSpinner.getSelectedItem().toString());
        ChartTypeEnum chartType = ChartTypeEnum.getChartType(chartTypeSpinner.getSelectedItem().toString());
        String[] moodValues = resources.getStringArray(R.array.mood_values);

        buildChart(chartType);
        View chartView = parentView.findViewById(R.id.chart);

        if (chartType.equals(ChartTypeEnum.Line)) {
            new LineChartHelper(timeRange, moodValues, context, isDarkTheme).buildChart(chartView);
        } else if (chartType.equals(ChartTypeEnum.Bar)) {
            new BarChartHelper(timeRange, moodValues, context, isDarkTheme).buildChart(chartView);
        } else if (chartType.equals(ChartTypeEnum.Pie)) {
            new PieChartHelper(timeRange, moodValues, context, isDarkTheme).buildChart(chartView);
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
