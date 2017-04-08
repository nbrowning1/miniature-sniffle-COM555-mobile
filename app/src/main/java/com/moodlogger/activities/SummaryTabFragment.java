package com.moodlogger.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.moodlogger.R;
import com.moodlogger.asyncTasks.BuildChartTask;

public class SummaryTabFragment extends Fragment {

    private static int debugHintsCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.summary_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        LinearLayout parentView = (LinearLayout) getView().findViewById(R.id.summary_fragment);
        new BuildChartTask(getContext(), parentView, getResources())
                .execute();
        setSpecificViewThemes();
        // TODO: change to sharedPreferences
        // TODO: move dialog to asyncTask
        if (false && debugHintsCount < 2) {
            debugHintsCount++;
            showHint();
        }
    }

    private void showHint() {
        String title = getResources().getString(R.string.summary_hint_1_title);
        String username = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("user_name", "");
        String message = String.format(getResources().getString(R.string.summary_hint_1_message), username);

        ActivityUtils.showHintDialog(getActivity(), title, message);
    }

    private void setSpecificViewThemes() {
        boolean isDarkTheme = ActivityUtils.isDarkTheme(getContext());
        setSpecificViewTheme(isDarkTheme, R.drawable.add_activity, R.drawable.add_activity_white,
                R.id.summary_add_mood_button);
    }

    private void setSpecificViewTheme(boolean isDarkTheme, int lightThemeResId, int darkThemeResId, int viewResId) {
        ActivityUtils.setSpecificViewTheme(getView(), isDarkTheme, lightThemeResId, darkThemeResId, viewResId);
    }
}
