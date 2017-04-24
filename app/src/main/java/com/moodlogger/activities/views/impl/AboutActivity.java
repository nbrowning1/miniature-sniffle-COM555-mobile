package com.moodlogger.activities.views.impl;

import android.os.Bundle;

import com.moodlogger.R;
import com.moodlogger.activities.AbstractMoodActivity;
import com.moodlogger.activities.ActivityUtils;
import com.moodlogger.activities.views.intf.AboutView;

public class AboutActivity extends AbstractMoodActivity implements AboutView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSpecificViewThemes();
        ActivityUtils.setFontSizeIfLargeFont(getResources(), this, findViewById(R.id.about_root));
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.about;
    }

    @Override
    protected void setSpecificViewThemes() {
        boolean isDarkTheme = isDarkTheme();

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
