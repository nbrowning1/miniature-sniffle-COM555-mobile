package com.moodlogger.activities.presenters.intf;

import java.util.ArrayList;

public interface AddMoodLogPresenter {

    void validateMoodLog(int selectedMood, ArrayList<Long> selectedActivities);

    void onDestroy();
}
