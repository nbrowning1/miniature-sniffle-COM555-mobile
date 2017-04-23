package com.moodlogger.activities;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.moodlogger.R;
import com.moodlogger.enums.ThemeEnum;

public abstract class AbstractMoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupTheme();
        setContentView(getContentViewResId());
    }

    protected abstract int getContentViewResId();

    protected void setupTheme() {
        int themeId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("theme", 0);
        ThemeEnum theme = ThemeEnum.getTheme(themeId);
        switch (theme) {
            case Default:
                setTheme(R.style.AppTheme);
                break;
            case Dark:
                setTheme(R.style.DarkAppTheme);
                break;
            case Ocean:
                setTheme(R.style.OceanAppTheme);
                break;
            case Mint:
                setTheme(R.style.MintAppTheme);
                break;
        }
    }

    protected boolean isDarkTheme() {
        int themeId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("theme", 0);
        ThemeEnum theme = ThemeEnum.getTheme(themeId);
        switch (theme) {
            case Default:
            case Ocean:
                return false;
            case Dark:
            case Mint:
                return true;
            default:
                return false;
        }
    }

    protected void setViewThemes(final int noOfSectionViews, final int resource, final String identifierPrefix) {
        for (int i = 1; i <= noOfSectionViews; i++) {
            findViewById(getResources().getIdentifier(identifierPrefix + i, "id", this.getPackageName()))
                    .setBackgroundResource(resource);
        }
    }
}
