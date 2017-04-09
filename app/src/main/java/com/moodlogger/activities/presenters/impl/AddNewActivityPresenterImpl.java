package com.moodlogger.activities.presenters.impl;


import android.content.Context;

import com.moodlogger.activities.models.impl.AddNewActivityModelImpl;
import com.moodlogger.activities.models.intf.AddNewActivityModel;
import com.moodlogger.activities.presenters.intf.AddNewActivityPresenter;
import com.moodlogger.activities.views.intf.AddNewActivityView;

public class AddNewActivityPresenterImpl implements AddNewActivityPresenter, AddNewActivityModel.OnAddNewActivityFinishedListener {

    private AddNewActivityView addNewActivityView;
    private AddNewActivityModel addNewActivityModel;

    public AddNewActivityPresenterImpl(AddNewActivityView addNewActivityView, Context context) {
        this.addNewActivityView = addNewActivityView;
        this.addNewActivityModel = new AddNewActivityModelImpl(context);
    }

    @Override
    public void onDestroy() {
        addNewActivityView = null;
    }

    @Override
    public void validateNewActivity(String activityName, String selectedActivityResource) {
        addNewActivityModel.finish(activityName, selectedActivityResource, this);
    }

    @Override
    public void onGeneralValidationError() {
        if (addNewActivityView != null) {
            addNewActivityView.showGeneralValidationDialog();
        }
    }

    @Override
    public void onAlreadyExistsValidationError() {
        if (addNewActivityView != null) {
            addNewActivityView.showAlreadyExistsValidationDialog();
        }
    }

    @Override
    public void onSuccess() {
        if (addNewActivityView != null) {
            addNewActivityView.returnToAddMood();
        }
    }
}
