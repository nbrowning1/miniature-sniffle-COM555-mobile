package com.moodlogger.activities.presenters.impl;

import android.content.Context;

import com.moodlogger.activities.models.impl.CustomiseModelImpl;
import com.moodlogger.activities.models.intf.CustomiseModel;
import com.moodlogger.activities.presenters.intf.CustomisePresenter;
import com.moodlogger.activities.views.intf.CustomiseView;

public class CustomisePresenterImpl implements CustomisePresenter, CustomiseModel.OnCustomiseFinishedListener {

    private CustomiseView customiseView;
    private CustomiseModel customiseModel;

    public CustomisePresenterImpl(CustomiseView customiseView, Context context) {
        this.customiseView = customiseView;
        this.customiseModel = new CustomiseModelImpl(context);
    }

    @Override
    public void onDestroy() {
        customiseView = null;
    }

    @Override
    public boolean validateAndSaveName(String oldName, String newName) {
        return customiseModel.validateAndSaveName(oldName, newName, this);
    }

    @Override
    public void setNewTheme(int themeId) {
        customiseModel.setNewTheme(themeId, this);
    }

    @Override
    public void setLargeFont(boolean isLargeFont) {
        customiseModel.setLargeFont(isLargeFont, this);
    }

    @Override
    public void onValidationError() {
        if (customiseView != null) {
            customiseView.showValidationDialog();
        }
    }

    @Override
    public void onNameChangesSaved() {
        if (customiseView != null) {
            customiseView.showChangesSaved();
        }
    }

    @Override
    public void onThemeSaved() {
        if (customiseView != null) {
            customiseView.changeTheme();
        }
    }

    @Override
    public void onFontChangeSaved() {
        if (customiseView != null) {
            customiseView.changeFont();
        }
    }
}
