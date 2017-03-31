package com.moodlogger.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.moodlogger.R;
import com.moodlogger.asyncTasks.FetchInfoForMoodTask;
import com.moodlogger.asyncTasks.FetchMoodsForActivityTask;

public class ViewTabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupNestedScrollViews();
        buildMoodsView();
        buildActivitiesView();
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
