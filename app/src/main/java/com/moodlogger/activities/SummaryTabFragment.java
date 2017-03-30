package com.moodlogger.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.moodlogger.R;
import com.moodlogger.charts.BarChartHelper;
import com.moodlogger.charts.ChartTypeEnum;
import com.moodlogger.charts.LineChartHelper;
import com.moodlogger.charts.PieChartHelper;
import com.moodlogger.charts.TimeRangeEnum;

public class SummaryTabFragment extends Fragment {

    private Spinner timeRangeSpinner;
    private Spinner chartTypeSpinner;

    /* stuff to work around spinner's implementation quirks - known problem:
        http://stackoverflow.com/questions/5624825/spinner-onitemselected-executes-when-it-is-not-suppose-to/5918177#5918177 */
    private int noOfSpinnersInitialised;
    private static final int NO_OF_SPINNERS_TO_WAIT_FOR_INIT = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.summary_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // chart will be built via spinners' onClick handler after spinners are initialised
        setupSpinners();
    }

    private void setupSpinners() {
        noOfSpinnersInitialised = 0;
        timeRangeSpinner = (Spinner) getView().findViewById(R.id.time_range_spinner);
        chartTypeSpinner = (Spinner) getView().findViewById(R.id.chart_type_spinner);

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

    private void buildChart() {
        TimeRangeEnum timeRange = TimeRangeEnum.getEnum(timeRangeSpinner.getSelectedItem().toString());
        ChartTypeEnum chartType = ChartTypeEnum.getChartType(chartTypeSpinner.getSelectedItem().toString());
        String[] moodValues = getResources().getStringArray(R.array.mood_values);

        buildChart(chartType);
        View chartView = getView().findViewById(R.id.chart);

        if (chartType.equals(ChartTypeEnum.Line)) {
            new LineChartHelper(timeRange, moodValues, getContext()).buildChart(chartView);
        } else if (chartType.equals(ChartTypeEnum.Bar)) {
            new BarChartHelper(timeRange, moodValues, getContext()).buildChart(chartView);
        } else if (chartType.equals(ChartTypeEnum.Pie)) {
            new PieChartHelper(timeRange, moodValues, getContext()).buildChart(chartView);
        }
    }

    private void buildChart(ChartTypeEnum chartType) {
        LinearLayout chartParent = (LinearLayout) getView().findViewById(R.id.chart_parent);
        chartParent.removeAllViews();
        View chart;

        int widthPx = ActivityUtils.dpToPixels(getResources(), 350);
        int heightPx = ActivityUtils.dpToPixels(getResources(), 350);
        LinearLayout.LayoutParams chartLayoutParams = new LinearLayout.LayoutParams(widthPx, heightPx);

        if (chartType.equals(ChartTypeEnum.Line)) {
            chart = new LineChart(getContext());
            chart.setId(R.id.chart);
            chart.setLayoutParams(chartLayoutParams);
        } else if (chartType.equals(ChartTypeEnum.Bar)) {
            chart = new BarChart(getContext());
            chart.setId(R.id.chart);
            chart.setLayoutParams(chartLayoutParams);
        } else {
            chart = new PieChart(getContext());
            chart.setId(R.id.chart);
            chart.setLayoutParams(chartLayoutParams);
        }

        chartParent.addView(chart);
    }
}
