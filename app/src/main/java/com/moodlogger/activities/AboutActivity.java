package com.moodlogger.activities;

import android.os.Bundle;

import com.moodlogger.R;

public class AboutActivity extends AbstractMoodActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSpecificViewThemes();
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.about;
    }

    private void setSpecificViewThemes() {
        boolean isDarkTheme = ActivityUtils.isDarkTheme(this);

        final int settingsSectionResId = isDarkTheme ?
                R.drawable.dark_settings_section_bg :
                R.drawable.settings_section_bg;
        setViewThemes(2, settingsSectionResId, "about_section_");

        final int nestedScrollResId = isDarkTheme ?
                R.drawable.dark_nested_scroll_view_bg :
                R.drawable.nested_scroll_view_bg;
        setViewThemes(2, nestedScrollResId, "about_nested_scroll_");
    }
}
