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

    /**
     * Forces implementation for activities for supplying their content view resource id so that
     * the content view is set after the theme is set up for the activity
     *
     * @return the resource id for this activity's content
     */
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
        return ActivityUtils.isDarkTheme(this);
    }

    /**
     * For setting specific sections of the view to keep in line with the user's theme e.g.
     * nested scroll views with custom styling
     */
    protected abstract void setSpecificViewThemes();

    /**
     * For setting the theme of specific custom-styled views e.g. nested scroll views with custom
     * styling.
     * Relies on the views being changed to be strictly named, such that they have the same
     * string prefix followed by their index in the activity, which is 1-indexed e.g.
     * 'settings_section_1' up to 'settings_section_3' - see implementation of Settings for some
     * examples
     *
     * @param noOfSectionViews The number of views to change, with the assumption that views will
     *                         have such views indexed in their 'id' attribute
     * @param resource         The resource to change the views to
     * @param identifierPrefix The string prefix for the view's 'id' attributes to identify which
     *                         views need updated
     */
    protected void setViewThemes(final int noOfSectionViews, final int resource, final String identifierPrefix) {
        for (int i = 1; i <= noOfSectionViews; i++) {
            findViewById(getResources().getIdentifier(identifierPrefix + i, "id", this.getPackageName()))
                    .setBackgroundResource(resource);
        }
    }
}
