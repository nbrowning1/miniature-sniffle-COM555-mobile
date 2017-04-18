package com.moodlogger.activities;


import android.support.v4.app.Fragment;

public abstract class AbstractMoodTabFragment extends Fragment {

    /* used for knowing when to perform processing-intensive tasks for display
        based on fragment loading because of pager adapter swiping between tabs.
        Lifecycle of fragment on Android seems messed up */
    // TODO : is needed?
    protected boolean isViewShown = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && getView() != null) {
            isViewShown = true;
            performTasksForVisibleView();
        } else {
            isViewShown = false;
        }
    }

    protected abstract void performTasksForVisibleView();
}
