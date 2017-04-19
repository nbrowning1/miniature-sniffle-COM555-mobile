package com.moodlogger.activities.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    private static int debugHintsCount = 0;

    private int timeSpinnerIndexSelected;
    private boolean isDarkTheme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.evaluate_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        isDarkTheme = ActivityUtils.isDarkTheme(getContext());
        setSpecificViewThemes();
        setupSpinners();
        buildEvaluations();
        if (getUserVisibleHint()) {
            performTasksForVisibleView();
        }
    }

    @Override
    protected void performTasksForVisibleView() {
        if (debugHintsCount < 1) {
            debugHintsCount++;
            showHint();
        }
    }

    private void showHint() {
        ActivityUtils.showHintDialog(getActivity(),
                getResources().getString(R.string.evaluate_hint_title),
                getResources().getString(R.string.evaluate_hint_message));
    }

    private void setupSpinners() {
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

    private void setSpecificViewThemes() {
        ActivityUtils.setSpecificViewTheme(getView(), isDarkTheme,
                R.drawable.nested_scroll_view_bg, R.drawable.dark_nested_scroll_view_bg, R.id.evaluate_view);
    }

    private void buildEvaluations() {
        LinearLayout parentView = (LinearLayout) getView().findViewById(R.id.evaluate_fragment);
        new BuildEvaluationsTask(getContext(), parentView, getResources())
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
