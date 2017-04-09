package com.moodlogger.activities.presenters.impl;

import android.content.Context;

import com.moodlogger.activities.models.impl.AddMoodLogModelImpl;
import com.moodlogger.activities.models.intf.AddMoodLogModel;
import com.moodlogger.activities.presenters.intf.AddMoodLogPresenter;
import com.moodlogger.activities.views.intf.AddMoodLogView;

import java.util.List;

public class AddMoodLogPresenterImpl implements AddMoodLogPresenter, AddMoodLogModel.OnAddMoodLogFinishedListener {

    private AddMoodLogView addMoodLogView;
    private AddMoodLogModel addMoodLogModel;

    public AddMoodLogPresenterImpl(AddMoodLogView addMoodLogView, Context context) {
        this.addMoodLogView = addMoodLogView;
        this.addMoodLogModel = new AddMoodLogModelImpl(context);
    }

    @Override
    public void onDestroy() {
        addMoodLogView = null;
    }

    @Override
    public void validateMoodLog(int selectedMood, List<Long> selectedActivities) {
        addMoodLogModel.finish(selectedMood, selectedActivities, this);
    }

    @Override
    public void onValidationError() {
        if (addMoodLogView != null) {
            addMoodLogView.showValidationDialog();
        }
    }

    @Override
    public void onSuccess() {
        if (addMoodLogView != null) {
            addMoodLogView.navigateToMain();
        }
    }
}
