package com.moodlogger.activities.models.impl;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.moodlogger.activities.models.intf.AddMoodLogModel;
import com.moodlogger.db.entities.Activity;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.helpers.ActivityDbHelper;
import com.moodlogger.db.helpers.MoodEntryDbHelper;

import java.util.ArrayList;
import java.util.List;

public class AddMoodLogModelImpl implements AddMoodLogModel {

    private Context context;

    public AddMoodLogModelImpl(Context context) {
        this.context = context;
    }

    @Override
    public void finish(int selectedMood, List<Long> selectedActivities, OnAddMoodLogFinishedListener listener) {
        if (selectedMood == -1 || selectedActivities.isEmpty()) {
            listener.onValidationError();
            return;
        }
        saveMoodLog(selectedMood, selectedActivities);
        listener.onSuccess();
    }

    void saveMoodLog(int selectedMood, List<Long> selectedActivities) {
        ActivityDbHelper activityDbHelper = new ActivityDbHelper(context);
        List<Activity> activities = new ArrayList<>();
        for (long activityId : selectedActivities) {
            activities.add(activityDbHelper.getActivity(activityId));
        }

        float latitude = -1L;
        float longitude = -1L;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // should already have gained permissions in the activity - try-catch here just for compilation
        try {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = (float) location.getLatitude();
                longitude = (float) location.getLongitude();
            }
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }

        MoodEntry moodEntry = new MoodEntry(latitude, longitude, selectedMood, activities);
        new MoodEntryDbHelper(context).create(moodEntry);
    }
}
