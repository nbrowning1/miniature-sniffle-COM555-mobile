package com.moodlogger.asyncTasks;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.LinearLayout;

import java.util.List;

public class FetchActivitiesForMoodTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private LinearLayout parentView;
    private Resources resources;

    public FetchActivitiesForMoodTask(Context context, LinearLayout parentView, Resources resources) {
        this.context = context;
        this.parentView = parentView;
        this.resources = resources;
    }

    @Override
    protected Void doInBackground(Void... args) {
        return null;
    }
}
