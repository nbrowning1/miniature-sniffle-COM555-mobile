package com.moodlogger.activities.views.impl;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.moodlogger.R;
import com.moodlogger.activities.AbstractMoodActivity;
import com.moodlogger.activities.ActivityUtils;
import com.moodlogger.activities.presenters.impl.AddMoodLogPresenterImpl;
import com.moodlogger.activities.presenters.impl.AddNewActivityPresenterImpl;
import com.moodlogger.activities.presenters.intf.AddNewActivityPresenter;
import com.moodlogger.activities.views.intf.AddNewActivityView;
import com.moodlogger.db.entities.Activity;
import com.moodlogger.db.helpers.ActivityDbHelper;

import java.util.ArrayList;
import java.util.List;

public class AddNewActivityActivity extends AbstractMoodActivity implements AddNewActivityView {

    private String selectedActivityTagName;
    private boolean isDarkTheme;

    private EditText activityName;
    private AddNewActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDarkTheme = ActivityUtils.isDarkTheme(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSpecificViewThemes();
        activityName = (EditText) findViewById(R.id.edit_message);
        findViewById(R.id.add_new_activity_add_activity_button).setOnClickListener(onAddNewActivity());

        presenter = new AddNewActivityPresenterImpl(this, getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.add_new_activity;
    }

    @Override
    public void showGeneralValidationDialog() {
        ActivityUtils.showAlertDialog(this, "Select a mood and at least one activity");
    }

    @Override
    public void showAlreadyExistsValidationDialog() {
        ActivityUtils.showAlertDialog(this, "Activity name is already in use");
    }

    @Override
    public void returnToAddMood() {
        Intent intent = new Intent(this, AddMoodLogActivity.class);

        // pass intent extras back to mood creation activity
        intent.putExtra("mood_selected", getIntent().getIntExtra("mood_selected", -1));
        intent.putExtra("activities_selected", getIntent().getSerializableExtra("activities_selected"));
        startActivity(intent);

        Toast.makeText(AddNewActivityActivity.this, "New activity created!",
                Toast.LENGTH_LONG).show();

        finish();
    }

    private View.OnClickListener onAddNewActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.validateNewActivity(activityName.getText().toString(), selectedActivityTagName);
            }
        };
    }

    private void setSpecificViewThemes() {
        // will take care of any theme-related changes for activity icons
        resetActivities();
        View contextView = findViewById(android.R.id.content);
        ActivityUtils.setSpecificViewTheme(contextView, isDarkTheme,
                R.drawable.add_mood_finish, R.drawable.add_mood_finish_white, R.id.add_new_activity_add_activity_button);
    }

    public void setActivitySelected(View view) {
        resetActivities();
        selectActivity(view);
    }

    private void resetActivities() {
        List<View> allViews = ActivityUtils.getChildViews(findViewById(R.id.new_activity_layout), new ArrayList<View>());
        for (View view : allViews) {
            String tag =
                    view.getTag() == null ? "" : view.getTag().toString();
            if (tag.contains("activity")) {
                if (isDarkTheme) {
                    tag += "_white";
                }
                int defaultResourceId = getResources().getIdentifier(tag, "drawable", this.getPackageName());
                view.setBackgroundResource(defaultResourceId);
            }
        }
    }

    private void selectActivity(View activityView) {
        String tag = activityView.getTag().toString();
        // remove theme modifier if exists before marking as selected
        String selectedImgResource = tag.replace("_white", "") + "_selected";
        activityView.setBackgroundResource(getResources().getIdentifier(selectedImgResource, "drawable", this.getPackageName()));
        selectedActivityTagName = tag;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
