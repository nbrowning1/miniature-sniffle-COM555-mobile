package com.moodlogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TabHost;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.moodlogger.R;
import com.moodlogger.charts.BarChartHelper;
import com.moodlogger.charts.ChartTypeEnum;
import com.moodlogger.charts.LineChartHelper;
import com.moodlogger.charts.PieChartHelper;
import com.moodlogger.charts.TimeRangeEnum;

public class MainActivity extends AbstractActivity {

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
        TimeRangeEnum timeRange = TimeRangeEnum.getEnum(timeRangeSpinner.getSelectedItem().toString());
        ChartTypeEnum chartType = ChartTypeEnum.getChartType(chartTypeSpinner.getSelectedItem().toString());
        String[] moodValues = getResources().getStringArray(R.array.mood_values);

        buildChart(chartType);
        View chartView = findViewById(R.id.chart);

        if (chartType.equals(ChartTypeEnum.Line)) {
            new LineChartHelper(timeRange, moodValues, getBaseContext()).buildChart(chartView);
        } else if (chartType.equals(ChartTypeEnum.Bar)) {
            new BarChartHelper(timeRange, moodValues, getBaseContext()).buildChart(chartView);
        } else if (chartType.equals(ChartTypeEnum.Pie)) {
            new PieChartHelper(timeRange, moodValues, getBaseContext()).buildChart(chartView);
        }
    }

    private void buildChart(ChartTypeEnum chartType) {
        LinearLayout chartParent = (LinearLayout) findViewById(R.id.chart_parent);
        chartParent.removeAllViews();
        View chart;
        LinearLayout.LayoutParams chartLayoutParams = new LinearLayout.LayoutParams(dpToPixels(350), dpToPixels(350));

        if (chartType.equals(ChartTypeEnum.Line)) {
            chart = new LineChart(this);
            chart.setId(R.id.chart);
            chart.setLayoutParams(chartLayoutParams);
        } else if (chartType.equals(ChartTypeEnum.Bar)) {
            chart = new BarChart(this);
            chart.setId(R.id.chart);
            chart.setLayoutParams(chartLayoutParams);
        } else {
            chart = new PieChart(this);
            chart.setId(R.id.chart);
            chart.setLayoutParams(chartLayoutParams);
        }

        chartParent.addView(chart);
    }

    public void addMoodLog(View view) {
        Intent intent = new Intent(MainActivity.this, AddMoodLogActivity.class);
        startActivity(intent);
    }
}
