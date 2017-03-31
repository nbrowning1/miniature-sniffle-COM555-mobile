package com.moodlogger.asyncTasks;

import android.content.Context;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.moodlogger.R;
import com.moodlogger.activities.ActivityUtils;
import com.moodlogger.charts.TimeRangeEnum;
import com.moodlogger.db.entities.Activity;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.helpers.MoodEntryDbHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchInfoForMoodTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private LinearLayout parentView;
    private Resources resources;

    private int moodRating;
    private TimeRangeEnum timeRange;
    private List<Location> locationsForMoodRating = new ArrayList<>();
    private List<Activity> activitiesForMoodRating = new ArrayList<>();
    private List<String> locations = new ArrayList<>();

    public FetchInfoForMoodTask(Context context, LinearLayout parentView, Resources resources){
        this.context = context;
        this.parentView = parentView;
        this.resources = resources;
    }

    @Override
    protected void onPreExecute() {
        setValuesFromSpinners();
    }

    private void setValuesFromSpinners() {
        moodRating = ((Spinner) parentView.findViewById(R.id.mood_spinner))
                .getSelectedItemPosition();

        String timeRangeValue = ((Spinner) parentView.findViewById(R.id.view_time_range_spinner))
                .getSelectedItem().toString();
        timeRange = TimeRangeEnum.getEnum(timeRangeValue);
    }

    @Override
    protected Void doInBackground(Void... args) {
        List<MoodEntry> moodEntriesForMoodRating =
                new MoodEntryDbHelper(context).getMoodEntries(moodRating, timeRange);

        for (MoodEntry moodEntry : moodEntriesForMoodRating) {
            locationsForMoodRating.add(moodEntry.getLocation());
            activitiesForMoodRating.addAll(moodEntry.getActivities());
        }

        buildLocations();
        return null;
    }

    private void buildLocations() {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        for (Location location : locationsForMoodRating) {
            String locationText = null;
            try {
                Address address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
                locationText = address.getThoroughfare() == null ?
                        "Unknown Location" :
                        address.getThoroughfare();
            } catch (IOException e) {
                Log.w("WARN", "IOException caught when handling location: " + e.getMessage());
            }
            if (locationText != null) {
                locations.add(locationText);
            }
        }
    }

    @Override
    protected void onPostExecute(Void args) {
        buildLocationsAndActivitiesForMood();
    }

    private void buildLocationsAndActivitiesForMood() {
        // hide progress spinner
        parentView.findViewById(R.id.mood_info_progress_spinner).setVisibility(View.GONE);
        LinearLayout locationsParent = (LinearLayout) parentView.findViewById(R.id.mood_locations);
        LinearLayout activitiesParent = (LinearLayout) parentView.findViewById(R.id.mood_activities);
        locationsParent.removeAllViews();
        activitiesParent.removeAllViews();

        for (String locationText : locations) {
            locationsParent.addView(createLocationText(locationText));
        }
        for (Activity activity : activitiesForMoodRating) {
            activitiesParent.addView(createSingleActivityView(activity));
        }
    }

    private TextView createLocationText(String text) {
        LinearLayout.LayoutParams activityTextLayoutParams = new LinearLayout.LayoutParams(
                ActivityUtils.dpToPixels(resources, 125),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int marginTopPx = ActivityUtils.dpToPixels(resources, 15);
        activityTextLayoutParams.setMargins(0, marginTopPx, 0, 0);
        TextView activityText = new TextView(context);
        activityText.setGravity(Gravity.CENTER_HORIZONTAL);
        activityText.setLayoutParams(activityTextLayoutParams);
        activityText.setText(text);
        activityText.setTextSize(15f);

        return activityText;
    }

    private LinearLayout createSingleActivityView(Activity activity) {
        LinearLayout singleActivityLayout = createSingleActivityView();

        if (activity != null) {
            singleActivityLayout.addView(createActivityImageButton(activity));
            singleActivityLayout.addView(createActivityText(activity.getName()));
        }

        return singleActivityLayout;
    }

    private LinearLayout createSingleActivityView() {
        LinearLayout singleActivityLayout = new LinearLayout(context);
        LinearLayout.LayoutParams singleActivityLayoutParams = new LinearLayout.LayoutParams(
                ActivityUtils.dpToPixels(resources, 125),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        singleActivityLayoutParams.setMargins(
                ActivityUtils.dpToPixels(resources, 20), 0,
                ActivityUtils.dpToPixels(resources, 20), ActivityUtils.dpToPixels(resources, 15));
        singleActivityLayout.setLayoutParams(singleActivityLayoutParams);
        singleActivityLayout.setGravity(Gravity.CENTER_VERTICAL);

        return singleActivityLayout;
    }

    private ImageButton createActivityImageButton(Activity activity) {
        LinearLayout.LayoutParams activityImageLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        activityImageLayoutParams.setMargins(0, 0, ActivityUtils.dpToPixels(resources, 10), 0);
        ImageButton activityImageBtn = new ImageButton(context);
        activityImageBtn.setLayoutParams(activityImageLayoutParams);
        int resId = resources.getIdentifier(activity.getImgKey(), "drawable", context.getPackageName());
        activityImageBtn.setBackgroundResource(resId);

        return activityImageBtn;
    }

    private TextView createActivityText(String activityName) {
        LinearLayout.LayoutParams activityTextLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView activityText = new TextView(context);
        activityText.setLayoutParams(activityTextLayoutParams);
        activityText.setText(activityName);
        activityText.setTextSize(15f);

        return activityText;
    }
}