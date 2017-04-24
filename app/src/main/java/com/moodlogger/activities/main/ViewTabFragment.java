package com.moodlogger.activities.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.moodlogger.R;
import com.moodlogger.activities.AbstractMoodTabFragment;
import com.moodlogger.activities.ActivityUtils;
import com.moodlogger.asyncTasks.FetchInfoForMoodTask;
import com.moodlogger.asyncTasks.FetchMoodsForActivityTask;
import com.moodlogger.db.helpers.ActivityDbHelper;

import java.util.List;

public class ViewTabFragment extends AbstractMoodTabFragment {

    private static final String HINT_GIVEN_SHARED_PREF_KEY = "view_hint_given";

    private int timeSpinnerIndexSelected;
    private int moodSpinnerIndexSelected;
    private int activitySpinnerIndexSelected;
    private boolean isDarkTheme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        isDarkTheme = ActivityUtils.isDarkTheme(getContext());
        setSpecificViewThemes();
        setupSpinners();
        setupNestedScrollViews();
        if (getUserVisibleHint()) {
            showHintIfHintNotGiven();
        }
        ActivityUtils.setFontSizeIfLargeFont(getResources(), getActivity(), getView());
    }

    @Override
    protected void setSpecificViewThemes() {
        setSpecificViewTheme(R.drawable.nested_scroll_view_bg, R.drawable.dark_nested_scroll_view_bg,
                R.id.mood_scroll_view);
        setSpecificViewTheme(R.drawable.nested_scroll_view_bg, R.drawable.dark_nested_scroll_view_bg,
                R.id.activity_scroll_view);
        setSpecificViewTheme(R.drawable.mood_great, R.drawable.mood_great_white, R.id.view_mood_great);
        setSpecificViewTheme(R.drawable.mood_happy, R.drawable.mood_happy_white, R.id.view_mood_happy);
        setSpecificViewTheme(R.drawable.mood_neutral, R.drawable.mood_neutral_white, R.id.view_mood_neutral);
        setSpecificViewTheme(R.drawable.mood_sad, R.drawable.mood_sad_white, R.id.view_mood_sad);
        setSpecificViewTheme(R.drawable.mood_angry, R.drawable.mood_angry_white, R.id.view_mood_angry);
    }

    private void setSpecificViewTheme(int lightThemeResId, int darkThemeResId, int viewResId) {
        ActivityUtils.setSpecificViewTheme(getView(), isDarkTheme, lightThemeResId, darkThemeResId, viewResId);
    }

    @Override
    protected void setupSpinners() {
        initialiseSpinnerIndexesSelected();
        populateActivitiesSpinner();

        final LinearLayout fragmentView = (LinearLayout) getView().findViewById(R.id.view_fragment);
        final Spinner timeRangeSpinner = (Spinner) getView().findViewById(R.id.view_time_range_spinner);
        final Spinner moodTypeSpinner = (Spinner) getView().findViewById(R.id.mood_spinner);
        final Spinner activityTypeSpinner = (Spinner) getView().findViewById(R.id.activity_spinner);

        // time range spinner should refresh both moods and activities sections
        timeRangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // to stop unnecessarily re-invoking things if already selected e.g. view initialisation
                if (position == timeSpinnerIndexSelected) {
                    return;
                }
                buildMoodsView(fragmentView);
                buildActivitiesView(fragmentView);
                timeSpinnerIndexSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // mood type spinner should refresh only moods section
        moodTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // to stop unnecessarily re-invoking things if already selected e.g. view initialisation
                if (position == moodSpinnerIndexSelected) {
                    return;
                }
                buildMoodsView(fragmentView);
                moodSpinnerIndexSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // activity type spinner should refresh only activities section
        activityTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // to stop unnecessarily re-invoking things if already selected e.g. view initialisation
                if (position == activitySpinnerIndexSelected) {
                    return;
                }
                buildActivitiesView(fragmentView);
                activitySpinnerIndexSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void initialiseSpinnerIndexesSelected() {
        /* time spinner is the only spinner where we want to trigger the
            onItemSelected handler during initialisation, as it affects
            both sections anyway - set to -1 so it triggers chart build
            in spinner's OnItemSelectedListener */
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

    private void buildMoodsView(LinearLayout parentView) {
        new FetchInfoForMoodTask(getActivity(), parentView, getResources(), isDarkTheme)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void buildActivitiesView(LinearLayout parentView) {
        new FetchMoodsForActivityTask(getActivity(), parentView, getResources())
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setupNestedScrollViews() {
        NestedScrollView moodScrollView = (NestedScrollView) getView().findViewById(R.id.mood_scroll_view);
        NestedScrollView activityScrollView = (NestedScrollView) getView().findViewById(R.id.activity_scroll_view);
        moodScrollView.setNestedScrollingEnabled(true);
        activityScrollView.setNestedScrollingEnabled(true);
    }

    @Override
    protected String getHintGivenSharedPreferencesKey() {
        return HINT_GIVEN_SHARED_PREF_KEY;
    }

    @Override
    protected void showHint() {
        ActivityUtils.showHintDialog(getActivity(),
                getResources().getString(R.string.view_hint_title),
                getResources().getString(R.string.view_hint_message));
    }
}
