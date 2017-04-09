package com.moodlogger.activities.presenters.impl;

import android.content.Context;

import com.moodlogger.activities.models.impl.WelcomeModelImpl;
import com.moodlogger.activities.models.intf.WelcomeModel;
import com.moodlogger.activities.presenters.intf.WelcomePresenter;
import com.moodlogger.activities.views.intf.WelcomeView;

public class WelcomePresenterImpl implements WelcomePresenter, WelcomeModel.OnWelcomeFinishedListener {

    private WelcomeView welcomeView;
    private WelcomeModel welcomeModel;

    public WelcomePresenterImpl(WelcomeView welcomeView, Context context) {
        this.welcomeView = welcomeView;
        this.welcomeModel = new WelcomeModelImpl(context);
    }

    @Override
    public void validateName(String name) {
        welcomeModel.finish(name, this);
    }

    @Override
    public void onDestroy() {
        welcomeView = null;
    }

    @Override
    public void onValidationError() {
        if (welcomeView != null) {
            welcomeView.showValidationDialog();
        }
    }

    @Override
    public void onSuccess() {
        if (welcomeView != null) {
            welcomeView.navigateToMain();
        }
    }
}
