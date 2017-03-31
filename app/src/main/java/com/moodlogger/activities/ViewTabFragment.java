package com.moodlogger.activities;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.moodlogger.R;
import com.moodlogger.asyncTasks.FetchLocationsTask;
import com.moodlogger.charts.TimeRangeEnum;
import com.moodlogger.db.entities.Activity;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.helpers.MoodEntryDbHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ViewTabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        buildLocationsAndActivitiesForMood();
    }

    private void buildLocationsAndActivitiesForMood() {
        int moodRating = ((Spinner) getView().findViewById(R.id.mood_spinner))
                .getSelectedItemPosition();

        String timeRangeValue = ((Spinner) getView().findViewById(R.id.view_time_range_spinner)).getSelectedItem().toString();
        TimeRangeEnum timeRange = TimeRangeEnum.getEnum(timeRangeValue);
        List<MoodEntry> moodEntriesForMoodRating =
                new MoodEntryDbHelper(getContext()).getMoodEntries(moodRating, timeRange);

        List<Location> locationsForMoodRating = new ArrayList<>();
        List<Activity> activitiesForMoodRating = new ArrayList<>();
        for (MoodEntry moodEntry : moodEntriesForMoodRating) {
            locationsForMoodRating.add(moodEntry.getLocation());
            activitiesForMoodRating.addAll(moodEntry.getActivities());
        }

        buildLocations(locationsForMoodRating);
        buildActivities(activitiesForMoodRating);
    }

    private void buildLocations(List<Location> locations) {
        LinearLayout parentView = (LinearLayout) getView().findViewById(R.id.mood_locations);
        parentView.removeAllViews();
        
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        for (Location location : locations) {
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
                parentView.addView(createLocationText(locationText));
            }
        }
    }

    private TextView createLocationText(String text) {
        LinearLayout.LayoutParams activityTextLayoutParams = new LinearLayout.LayoutParams(
                ActivityUtils.dpToPixels(getResources(), 125),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int marginTopPx = ActivityUtils.dpToPixels(getResources(), 15);
        activityTextLayoutParams.setMargins(0, marginTopPx, 0, 0);
        TextView activityText = new TextView(getContext());
        activityText.setGravity(Gravity.CENTER_HORIZONTAL);
        activityText.setLayoutParams(activityTextLayoutParams);
        activityText.setText(text);
        activityText.setTextSize(15f);

        return activityText;
    }

    private void buildActivities(List<Activity> activities) {
        LinearLayout parentView = (LinearLayout) getView().findViewById(R.id.mood_activities);
        parentView.removeAllViews();
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        for (Activity activity : activities) {
            parentView.addView(createSingleActivityView(activity));
        }
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
        LinearLayout singleActivityLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams singleActivityLayoutParams = new LinearLayout.LayoutParams(
                ActivityUtils.dpToPixels(getResources(), 125),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        singleActivityLayoutParams.setMargins(
                ActivityUtils.dpToPixels(getResources(), 20), 0,
                ActivityUtils.dpToPixels(getResources(), 20), 0);
        singleActivityLayout.setLayoutParams(singleActivityLayoutParams);
        singleActivityLayout.setGravity(Gravity.CENTER_VERTICAL);

        return singleActivityLayout;
    }

    private ImageButton createActivityImageButton(Activity activity) {
        LinearLayout.LayoutParams activityImageLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        activityImageLayoutParams.setMargins(0, 0, ActivityUtils.dpToPixels(getResources(), 10), 0);
        ImageButton activityImageBtn = new ImageButton(getContext());
        activityImageBtn.setLayoutParams(activityImageLayoutParams);
        int resId = getResources().getIdentifier(activity.getImgKey(), "drawable", getContext().getPackageName());
        activityImageBtn.setBackgroundResource(resId);

        return activityImageBtn;
    }

    private TextView createActivityText(String activityName) {
        LinearLayout.LayoutParams activityTextLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView activityText = new TextView(getContext());
        activityText.setLayoutParams(activityTextLayoutParams);
        activityText.setText(activityName);
        activityText.setTextSize(15f);

        return activityText;
    }
}
