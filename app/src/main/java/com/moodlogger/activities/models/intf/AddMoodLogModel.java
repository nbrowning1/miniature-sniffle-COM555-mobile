package com.moodlogger.activities.models.intf;

import java.util.ArrayList;
import java.util.List;

public interface AddMoodLogModel {

    interface OnAddMoodLogFinishedListener {

        void onValidationError();

        void onSuccess();
    }

    void finish(int selectedMood, List<Long> selectedActivities, AddMoodLogModel.OnAddMoodLogFinishedListener listener);
}
