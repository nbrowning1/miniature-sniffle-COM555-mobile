package com.moodlogger.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.moodlogger.R;

public class CustomiseActivity extends AbstractMoodActivity {

    /* for checking if any changes were made - determines whether we need to provide visual feedback
        to user upon exiting */
    private String initialName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSpecificViewThemes();
        populateNameField();
        setupThemes();
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.customise;
    }

    private void setSpecificViewThemes() {
        boolean isDarkTheme = ActivityUtils.isDarkTheme(this);

        final int settingsSectionResId = isDarkTheme ?
                R.drawable.dark_settings_section_bg :
                R.drawable.settings_section_bg;
        setViewThemes(2, settingsSectionResId, "customise_section_");

        final int nestedScrollResId = isDarkTheme ?
                R.drawable.dark_nested_scroll_view_bg :
                R.drawable.nested_scroll_view_bg;
        setViewThemes(5, nestedScrollResId, "customise_nested_scroll_");
    }

    private void populateNameField() {
        String username = PreferenceManager.getDefaultSharedPreferences(this).getString("user_name", "");
        EditText nameField = (EditText) findViewById(R.id.name_text);
        nameField.setText(username);
        initialName = username;
    }

    private void setupThemes() {
        findViewById(R.id.theme_0).setOnClickListener(themeOnClick(0));
        findViewById(R.id.theme_1).setOnClickListener(themeOnClick(1));
        findViewById(R.id.theme_2).setOnClickListener(themeOnClick(2));
        findViewById(R.id.theme_3).setOnClickListener(themeOnClick(3));
    }

    private View.OnClickListener themeOnClick(final int themeId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("theme", themeId);

                // commit as we want to change theme in SharedPrefs immediately - lots of visual feedback
                editor.commit();

                // save any changes..
                saveName();

                // and refresh
                finish();
                startActivity(getIntent());

                Toast.makeText(CustomiseActivity.this, "Theme changed.",
                        Toast.LENGTH_LONG).show();
            }
        };
    }

    private void saveName() {
        String name = ((EditText) findViewById(R.id.name_text)).getText().toString();
        boolean changesMade = !name.equals(initialName);
        if (!name.isEmpty() && changesMade) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_name", name);

            /* apply as we're unlikely to run into anything needing the name - doesn't need to be
                be immediate */
            editor.apply();

            Toast.makeText(this, "Changes saved.",
                    Toast.LENGTH_LONG).show();
        }
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
