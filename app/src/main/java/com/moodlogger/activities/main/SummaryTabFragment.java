package com.moodlogger.activities.main;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.moodlogger.R;
import com.moodlogger.activities.AbstractMoodTabFragment;
import com.moodlogger.activities.ActivityUtils;
import com.moodlogger.asyncTasks.BuildChartTask;

public class SummaryTabFragment extends AbstractMoodTabFragment {

    private static int debugHintsCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.summary_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setSpecificViewThemes();

        if (shouldPerformTasksOnViewCreated()) {
            performTasksForVisibleView();
        }
    }

    @Override
    protected void performTasksForVisibleView() {
        LinearLayout parentView = (LinearLayout) getView().findViewById(R.id.summary_fragment);
        new BuildChartTask(getContext(), parentView, getResources())
                .execute();

        // TODO: change to sharedPreferences
        if (debugHintsCount < 2) {
            debugHintsCount++;
            showHint();
        }
    }

    private void showHint() {
        String title = getResources().getString(R.string.summary_hint_title);
        String username = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("user_name", "");
        String message = String.format(getResources().getString(R.string.summary_hint_message), username);

        ActivityUtils.showHintDialog(getActivity(), title, message);
    }

    private void setSpecificViewThemes() {
        boolean isDarkTheme = ActivityUtils.isDarkTheme(getContext());
        setSpecificViewTheme(isDarkTheme, R.drawable.add_mood_entry, R.drawable.add_mood_entry_white,
                R.id.summary_add_mood_button);
    }

    private void setSpecificViewTheme(boolean isDarkTheme, int lightThemeResId, int darkThemeResId, int viewResId) {
        ActivityUtils.setSpecificViewTheme(getView(), isDarkTheme, lightThemeResId, darkThemeResId, viewResId);
    }
}
