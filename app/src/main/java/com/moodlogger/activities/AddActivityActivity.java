package com.moodlogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.moodlogger.R;
import com.moodlogger.db.entities.Activity;
import com.moodlogger.db.helpers.ActivityDbHelper;

import java.util.ArrayList;
import java.util.List;

public class AddActivityActivity extends AppCompatActivity {

    private String selectedActivityTagName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                int defaultResourceId = getResources().getIdentifier(tag, "drawable", this.getPackageName());
                view.setBackgroundResource(defaultResourceId);
            }
        }
    }

    private void selectActivity(View activityView) {
        String tag = activityView.getTag().toString();
        String selectedImgResource = tag + "_selected";
        activityView.setBackgroundResource(getResources().getIdentifier(selectedImgResource, "drawable", this.getPackageName()));
        selectedActivityTagName = tag;
    }

    public void finishActivity(View view) {
        String name = ((EditText) findViewById(R.id.edit_message)).getText().toString();

        if (selectedActivityTagName == null || selectedActivityTagName.isEmpty() || name.isEmpty()) {
            ActivityUtils.showAlertDialog(this, "Select an icon and enter a name for the new activity");
            return;
        }

        ActivityDbHelper activityDbHelper = new ActivityDbHelper(getBaseContext());
        for (Activity activity : activityDbHelper.getActivities()) {
            if (name.equalsIgnoreCase(activity.getName())) {
                ActivityUtils.showAlertDialog(this, "Activity name is already in use");
                return;
            }
        }

        Activity newActivity = new Activity(name, selectedActivityTagName);
        activityDbHelper.create(newActivity);

        Intent intent = new Intent(AddActivityActivity.this, AddMoodLogActivity.class);
        // pass intent extras back to mood creation activity
        intent.putExtra("mood_selected", getIntent().getIntExtra("mood_selected", -1));
        intent.putExtra("activities_selected", getIntent().getSerializableExtra("activities_selected"));
        startActivity(intent);

        Toast.makeText(AddActivityActivity.this, "New activity created!",
                Toast.LENGTH_LONG).show();
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
