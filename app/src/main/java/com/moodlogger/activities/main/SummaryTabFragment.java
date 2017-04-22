package com.moodlogger.activities.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.moodlogger.R;
import com.moodlogger.activities.AbstractMoodTabFragment;
import com.moodlogger.activities.ActivityUtils;
import com.moodlogger.asyncTasks.BuildChartTask;

public class SummaryTabFragment extends AbstractMoodTabFragment {

    private static final String HINT_GIVEN_SHARED_PREF_KEY = "summary_hint_given";

    private int timeSpinnerIndexSelected;
    private int chartTypeSpinnerIndexSelected;

    private Spinner timeRangeSpinner;
    private Spinner chartTypeSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.summary_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setSpecificViewThemes();
        setupSpinners();
        if (getUserVisibleHint()) {
            performTasksForVisibleView();
        }
    }

    @Override
    protected void performTasksForVisibleView() {
        if (!ActivityUtils.hintGiven(getActivity(), HINT_GIVEN_SHARED_PREF_KEY)) {
            showHint();
            ActivityUtils.markHintAsGiven(getActivity(), HINT_GIVEN_SHARED_PREF_KEY);
        }
    }

    private void showHint() {
        String title = getResources().getString(R.string.summary_hint_title);
        String username = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("user_name", "");
        String message = String.format(getResources().getString(R.string.summary_hint_message), username);

        ActivityUtils.showHintDialog(getActivity(), title, message);
    }

    private void setSpecificViewThemes() {
        boolean isDarkTheme = ActivityUtils.isDarkTheme(getContext());
        setSpecificViewTheme(isDarkTheme, R.drawable.add_mood_entry, R.drawable.add_mood_entry_white,
                R.id.summary_add_mood_button);
    }

    private void setSpecificViewTheme(boolean isDarkTheme, int lightThemeResId, int darkThemeResId, int viewResId) {
        ActivityUtils.setSpecificViewTheme(getView(), isDarkTheme, lightThemeResId, darkThemeResId, viewResId);
    }

    private void setupSpinners() {
        initialiseSpinnerIndexesSelected();

        timeRangeSpinner = (Spinner) getView().findViewById(R.id.time_range_spinner);
        chartTypeSpinner = (Spinner) getView().findViewById(R.id.chart_type_spinner);

        timeRangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == timeSpinnerIndexSelected) {
                    return;
                }
                buildChart();
                timeSpinnerIndexSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        chartTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == chartTypeSpinnerIndexSelected) {
                    return;
                }
                buildChart();
                chartTypeSpinnerIndexSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void initialiseSpinnerIndexesSelected() {
        /* chart type spinner is the only spinner where we want to trigger the
        onItemSelected handler during initialisation, as it affects
        both sections anyway */
        timeSpinnerIndexSelected = 0;
        chartTypeSpinnerIndexSelected = -1;
    }

    private void buildChart() {
        LinearLayout parentView = (LinearLayout) getView().findViewById(R.id.summary_fragment);
        new BuildChartTask(getContext(), parentView, getResources())
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
