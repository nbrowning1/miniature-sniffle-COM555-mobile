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
import com.moodlogger.db.entities.Activity;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.helpers.MoodEntryDbHelper;
import com.moodlogger.enums.TimeRangeEnum;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class FetchInfoForMoodTask extends AsyncTask<Void, Void, Void> {

    private android.app.Activity contextActivity;
    private LinearLayout parentView;
    private Resources resources;
    private boolean isDarkTheme;

    private int moodRating;
    private TimeRangeEnum timeRange;
    private Set<Location> locationsForMoodRating = new LinkedHashSet<>();
    private Set<Activity> activitiesForMoodRating = new LinkedHashSet<>();
    private Set<String> locations = new LinkedHashSet<>();

    public FetchInfoForMoodTask(android.app.Activity contextActivity, LinearLayout parentView, Resources resources, boolean isDarkTheme) {
        this.contextActivity = contextActivity;
        this.parentView = parentView;
        this.resources = resources;
        this.isDarkTheme = isDarkTheme;
    }

    @Override
    protected void onPreExecute() {
        initialiseViewsToShowLoading();
        setValuesFromSpinners();
    }

    private void initialiseViewsToShowLoading() {
        parentView.findViewById(R.id.mood_info_progress_spinner).setVisibility(View.VISIBLE);
        ((LinearLayout) parentView.findViewById(R.id.mood_locations)).removeAllViews();
        ((LinearLayout) parentView.findViewById(R.id.mood_activities)).removeAllViews();
        ((TextView) parentView.findViewById(R.id.view_no_moods_text)).setText("");
    }

    private void setValuesFromSpinners() {
        moodRating = ((Spinner) parentView.findViewById(R.id.mood_spinner))
                .getSelectedItemPosition();
        String timeRangeValue = ((Spinner) parentView.findViewById(R.id.view_time_range_spinner))
                .getSelectedItem().toString();
        timeRange = TimeRangeEnum.getEnum(timeRangeValue);
    }

    @Override
    protected Void doInBackground(Void... params) {
        List<MoodEntry> moodEntriesForMoodRating =
                new MoodEntryDbHelper(contextActivity).getMoodEntries(moodRating, timeRange);

        for (MoodEntry moodEntry : moodEntriesForMoodRating) {
            locationsForMoodRating.add(moodEntry.getLocation());
            for (Activity activity : moodEntry.getActivities()) {
                activitiesForMoodRating.add(activity);
            }
        }

        buildLocations();
        return null;
    }

    /**
     * Builds location texts up using latitude / longitude to resolve location thoroughfares / street
     *  names
     */
    private void buildLocations() {
        Geocoder geocoder = new Geocoder(contextActivity, Locale.getDefault());
        for (Location location : locationsForMoodRating) {
            // default to unknown in case error occurs etc.
            String locationText = "Unknown location";
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (!addresses.isEmpty() && addresses.get(0).getThoroughfare() != null) {
                    locationText = addresses.get(0).getThoroughfare();
                }
            } catch (IOException e) {
                Log.w("mood", "IOException caught when handling location: " + e.getMessage());
            }
            locations.add(locationText);
        }
    }

    @Override
    protected void onPostExecute(Void params) {
        buildLocationsAndActivitiesForMood();
        ActivityUtils.setFontSizeIfLargeFont(resources, contextActivity, parentView.findViewById(R.id.mood_locations));
        ActivityUtils.setFontSizeIfLargeFont(resources, contextActivity, parentView.findViewById(R.id.mood_activities));
    }

    private void buildLocationsAndActivitiesForMood() {
        // hide progress spinner
        parentView.findViewById(R.id.mood_info_progress_spinner).setVisibility(View.GONE);
        ((LinearLayout) parentView.findViewById(R.id.mood_locations)).removeAllViews();
        ((LinearLayout) parentView.findViewById(R.id.mood_activities)).removeAllViews();

        LinearLayout locationsParent = (LinearLayout) parentView.findViewById(R.id.mood_locations);
        LinearLayout activitiesParent = (LinearLayout) parentView.findViewById(R.id.mood_activities);

        if (locations.isEmpty() && activitiesForMoodRating.isEmpty()) {
            ((TextView) parentView.findViewById(R.id.view_no_moods_text))
                    .setText(resources.getString(R.string.view_no_moods));
            return;
        }

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
        TextView activityText = new TextView(contextActivity);
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
        LinearLayout singleActivityLayout = new LinearLayout(contextActivity);
        LinearLayout.LayoutParams singleActivityLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        singleActivityLayoutParams.setMargins(0, 0, 0, ActivityUtils.dpToPixels(resources, 15));
        singleActivityLayout.setLayoutParams(singleActivityLayoutParams);
        singleActivityLayout.setGravity(Gravity.CENTER_VERTICAL);

        return singleActivityLayout;
    }

    private ImageButton createActivityImageButton(Activity activity) {
        LinearLayout.LayoutParams activityImageLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        activityImageLayoutParams.setMargins(0, 0, ActivityUtils.dpToPixels(resources, 15), 0);
        ImageButton activityImageBtn = new ImageButton(contextActivity);
        activityImageBtn.setLayoutParams(activityImageLayoutParams);

        // get correct icon to show for activity - choosing white one if dark theme is being used
        String activityImageKey = isDarkTheme ? activity.getImgKey() + "_white" : activity.getImgKey();
        int resId = resources.getIdentifier(activityImageKey, "drawable", contextActivity.getPackageName());
        activityImageBtn.setBackgroundResource(resId);

        return activityImageBtn;
    }

    private TextView createActivityText(String activityName) {
        LinearLayout.LayoutParams activityTextLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView activityText = new TextView(contextActivity);
        activityText.setLayoutParams(activityTextLayoutParams);
        activityText.setText(activityName);
        activityText.setTextSize(15f);

        return activityText;
    }
}
