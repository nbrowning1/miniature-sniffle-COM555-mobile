package com.moodlogger.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.moodlogger.R;
import com.moodlogger.asyncTasks.BuildChartTask;

public class SummaryTabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.summary_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        LinearLayout parentView = (LinearLayout) getView().findViewById(R.id.summary_fragment);
        new BuildChartTask(getContext(), parentView, getResources())
                .execute();
    }
}
