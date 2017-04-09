package com.moodlogger.activities.models.intf;

public interface AddNewActivityModel {

    interface OnAddNewActivityFinishedListener {

        void onGeneralValidationError();

        void onAlreadyExistsValidationError();

        void onSuccess();
    }

    void finish(String activityName, String selectedActivityResource, OnAddNewActivityFinishedListener listener);
}
