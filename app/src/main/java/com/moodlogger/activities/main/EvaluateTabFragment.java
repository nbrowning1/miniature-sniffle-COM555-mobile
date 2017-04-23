package com.moodlogger.activities.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.moodlogger.R;
import com.moodlogger.activities.AbstractMoodTabFragment;
import com.moodlogger.activities.ActivityUtils;
import com.moodlogger.asyncTasks.BuildEvaluationsTask;

public class EvaluateTabFragment extends AbstractMoodTabFragment {

    private static final String HINT_GIVEN_SHARED_PREF_KEY = "evaluate_hint_given";

    private int timeSpinnerIndexSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.evaluate_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setSpecificViewThemes();
        setupSpinners();
        buildEvaluations();
        if (getUserVisibleHint()) {
            showHintIfHintNotGiven();
        }
    }

    @Override
    protected void setSpecificViewThemes() {
        ActivityUtils.setSpecificViewTheme(getView(), ActivityUtils.isDarkTheme(getContext()),
                R.drawable.nested_scroll_view_bg, R.drawable.dark_nested_scroll_view_bg, R.id.evaluate_view);
    }

    @Override
    protected void setupSpinners() {
        /* we want to trigger the onItemSelected handler during view initialisation */
        timeSpinnerIndexSelected = -1;
        Spinner timeRangeSpinner = (Spinner) getView().findViewById(R.id.evaluate_time_range_spinner);

        timeRangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == timeSpinnerIndexSelected) {
                    return;
                }
                buildEvaluations();
                timeSpinnerIndexSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    @Override
    protected String getHintGivenSharedPreferencesKey() {
        return HINT_GIVEN_SHARED_PREF_KEY;
    }

    @Override
    protected void showHint() {
        ActivityUtils.showHintDialog(getActivity(),
                getResources().getString(R.string.evaluate_hint_title),
                getResources().getString(R.string.evaluate_hint_message));
    }

    private void buildEvaluations() {
        LinearLayout parentView = (LinearLayout) getView().findViewById(R.id.evaluate_fragment);
        new BuildEvaluationsTask(getContext(), parentView, getResources())
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
