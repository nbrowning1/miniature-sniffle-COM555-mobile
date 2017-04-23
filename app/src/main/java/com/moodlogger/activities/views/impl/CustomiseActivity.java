package com.moodlogger.activities.views.impl;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.moodlogger.R;
import com.moodlogger.activities.AbstractMoodActivity;
import com.moodlogger.activities.ActivityUtils;
import com.moodlogger.activities.presenters.impl.CustomisePresenterImpl;
import com.moodlogger.activities.presenters.intf.CustomisePresenter;
import com.moodlogger.activities.views.intf.CustomiseView;

public class CustomiseActivity extends AbstractMoodActivity implements CustomiseView {

    /* for checking if any changes were made - determines whether we need to provide visual feedback
        to user upon exiting */
    private String initialName = "";
    private EditText nameView;

    private CustomisePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSpecificViewThemes();
        nameView = (EditText) findViewById(R.id.name_text);
        populateNameField();
        setupThemes();

        presenter = new CustomisePresenterImpl(this, this);

        if (!ActivityUtils.hintGiven(this)) {
            showHint();
            ActivityUtils.markHintAsGiven(this);
        }
    }

    private void showHint() {
        ActivityUtils.showHintDialog(this,
                getResources().getString(R.string.customise_hint_title),
                getResources().getString(R.string.customise_hint_message));
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.customise;
    }

    @Override
    protected void setSpecificViewThemes() {
        boolean isDarkTheme = isDarkTheme();

        final int settingsSectionResId = isDarkTheme ?
                R.drawable.dark_settings_section_bg :
                R.drawable.settings_section_bg;
        setViewThemes(2, settingsSectionResId, "customise_section_");

        final int nestedScrollResId = isDarkTheme ?
                R.drawable.dark_nested_scroll_view_bg :
                R.drawable.nested_scroll_view_bg;
        setViewThemes(1, nestedScrollResId, "name_section_");
        setViewThemes(4, nestedScrollResId, "theme_section_");
    }

    private void populateNameField() {
        String username = PreferenceManager.getDefaultSharedPreferences(this).getString("user_name", "");
        nameView.setText(username);
        initialName = username;
    }

    private void setupThemes() {
        findViewById(R.id.theme_section_1).setOnClickListener(themeOnClick(0));
        findViewById(R.id.theme_section_2).setOnClickListener(themeOnClick(1));
        findViewById(R.id.theme_section_3).setOnClickListener(themeOnClick(2));
        findViewById(R.id.theme_section_4).setOnClickListener(themeOnClick(3));
    }

    private View.OnClickListener themeOnClick(final int themeId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // try to save any changes, return if validation not met
                if (!saveName()) {
                    return;
                }
                presenter.setNewTheme(themeId);
            }
        };
    }

    @Override
    public void changeTheme() {
        // refresh activity
        finish();
        startActivity(getIntent());

        Toast.makeText(this, getString(R.string.customise_theme_change_toast), Toast.LENGTH_LONG).show();
    }

    private boolean saveName() {
        String name = nameView.getText().toString();
        return presenter.validateAndSaveName(initialName, name);
    }

    @Override
    public void showValidationDialog() {
        ActivityUtils.showAlertDialog(this, getString(R.string.customise_name_change_validation));
    }

    @Override
    public void showChangesSaved() {
        Toast.makeText(this, getString(R.string.customise_changes_saved_toast), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        saveName();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveName();
                break;
        }
        return (super.onOptionsItemSelected(item));
    }
}
