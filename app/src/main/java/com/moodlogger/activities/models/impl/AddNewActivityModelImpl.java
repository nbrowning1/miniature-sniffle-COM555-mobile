package com.moodlogger.activities.models.impl;

import android.content.Context;

import com.moodlogger.StringUtils;
import com.moodlogger.activities.ActivityUtils;
import com.moodlogger.activities.models.intf.AddNewActivityModel;
import com.moodlogger.db.entities.Activity;
import com.moodlogger.db.helpers.ActivityDbHelper;

public class AddNewActivityModelImpl implements AddNewActivityModel {

    private Context context;

    public AddNewActivityModelImpl(Context context) {
        this.context = context;
    }

    @Override
    public void finish(String activityName, String selectedActivityResource, OnAddNewActivityFinishedListener listener) {
        if (StringUtils.isEmpty(selectedActivityResource) || !ActivityUtils.textInputIsValid(activityName)) {
            listener.onGeneralValidationError();
            return;
        }

        ActivityDbHelper activityDbHelper = getActivityDbHelper();
        for (Activity activity : activityDbHelper.getActivities()) {
            if (activityName.equalsIgnoreCase(activity.getName())) {
                listener.onAlreadyExistsValidationError();
                return;
            }
        }

        Activity newActivity = new Activity(activityName, selectedActivityResource);
        activityDbHelper.create(newActivity);

        listener.onSuccess();
    }

    ActivityDbHelper getActivityDbHelper() {
        return new ActivityDbHelper(context);
    }
}
