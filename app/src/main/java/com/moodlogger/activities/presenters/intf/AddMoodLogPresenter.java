package com.moodlogger.activities.presenters.intf;

import java.util.List;

public interface AddMoodLogPresenter {

    void validateMoodLog(int selectedMood, List<Long> selectedActivities);

    void onDestroy();
}
