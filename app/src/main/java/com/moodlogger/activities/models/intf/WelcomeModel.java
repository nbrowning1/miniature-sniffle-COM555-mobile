package com.moodlogger.activities.models.intf;

public interface WelcomeModel {

    interface OnWelcomeFinishedListener {

        void onValidationError();

        void onSuccess();
    }

    void finish(String name, OnWelcomeFinishedListener listener);
}
