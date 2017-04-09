package com.moodlogger.activities.presenters.intf;


public interface AddNewActivityPresenter {

    void validateNewActivity(String activityName, String selectedActivityResource);

    void onDestroy();
}
