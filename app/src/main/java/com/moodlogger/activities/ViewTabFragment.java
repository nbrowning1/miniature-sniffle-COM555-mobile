package com.moodlogger.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.moodlogger.R;
import com.moodlogger.asyncTasks.FetchInfoForMoodTask;
import com.moodlogger.asyncTasks.FetchMoodsForActivityTask;
import com.moodlogger.db.helpers.ActivityDbHelper;

import java.util.List;

public class ViewTabFragment extends Fragment {

    private int timeSpinnerIndexSelected;
    private int moodSpinnerIndexSelected;
    private int activitySpinnerIndexSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupNestedScrollViews();
        setupSpinners();
    }

    private void setupNestedScrollViews() {
        NestedScrollView moodScrollView = (NestedScrollView) getView().findViewById(R.id.mood_scroll_view);
        NestedScrollView activityScrollView = (NestedScrollView) getView().findViewById(R.id.activity_scroll_view);

        View.OnTouchListener scrollOverrideListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        };

        // override parent scrollview's scrolling
        moodScrollView.setOnTouchListener(scrollOverrideListener);
        activityScrollView.setOnTouchListener(scrollOverrideListener);
    }

    private void setupSpinners() {
        initialiseSpinnerIndexesSelected();
        populateActivitiesSpinner();

        final Spinner timeRangeSpinner = (Spinner) getView().findViewById(R.id.view_time_range_spinner);
        final Spinner moodTypeSpinner = (Spinner) getView().findViewById(R.id.mood_spinner);
        final Spinner activityTypeSpinner = (Spinner) getView().findViewById(R.id.activity_spinner);

        timeRangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == timeSpinnerIndexSelected) {
                    return;
                }
                buildMoodsView();
                buildActivitiesView();
                timeSpinnerIndexSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        moodTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == moodSpinnerIndexSelected) {
                    return;
                }
                buildMoodsView();
                moodSpinnerIndexSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        activityTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == activitySpinnerIndexSelected) {
                    return;
                }
                buildActivitiesView();
                activitySpinnerIndexSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    private void initialiseSpinnerIndexesSelected() {
        /* time spinner is the only spinner where we want to trigger the
        onItemSelected handler during initialisation, as it affects
        both sections anyway */
        timeSpinnerIndexSelected = -1;
        moodSpinnerIndexSelected = 0;
        activitySpinnerIndexSelected = 0;
    }

    private void populateActivitiesSpinner() {
        List<String> activities = new ActivityDbHelper(getContext()).getActivityNames();

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, activities);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner) getView().findViewById(R.id.activity_spinner)).setAdapter(spinnerArrayAdapter);
    }

    private void buildMoodsView() {
        LinearLayout parentView = (LinearLayout) getView().findViewById(R.id.view_fragment);
        new FetchInfoForMoodTask(getContext(), parentView, getResources())
                .execute();
    }

    private void buildActivitiesView() {
        // TODO : keep track of selected option when changing tabs as activities are dynamically generated
        LinearLayout parentView = (LinearLayout) getView().findViewById(R.id.view_fragment);
        new FetchMoodsForActivityTask(getContext(), parentView, getResources())
                .execute();
    }
}
