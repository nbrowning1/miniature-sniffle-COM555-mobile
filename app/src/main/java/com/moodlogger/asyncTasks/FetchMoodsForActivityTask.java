package com.moodlogger.asyncTasks;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.moodlogger.R;
import com.moodlogger.enums.TimeRangeEnum;
import com.moodlogger.db.entities.Activity;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.helpers.ActivityDbHelper;
import com.moodlogger.db.helpers.MoodEntryActivityDbHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchMoodsForActivityTask extends AsyncTask<Void, Void, Map<Integer, Integer>> {

    private Context context;
    private LinearLayout parentView;
    private Resources resources;
    private String activityName;
    private TimeRangeEnum timeRange;

    public FetchMoodsForActivityTask(Context context, LinearLayout parentView, Resources resources) {
        this.context = context;
        this.parentView = parentView;
        this.resources = resources;
    }

    @Override
    protected void onPreExecute() {
        initialiseViewsToShowLoading();

        Spinner activitySpinner = (Spinner) parentView.findViewById(R.id.activity_spinner);
        String timeRangeValue = ((Spinner) parentView.findViewById(R.id.view_time_range_spinner)).getSelectedItem().toString();
        timeRange = TimeRangeEnum.getEnum(timeRangeValue);
        activityName = activitySpinner.getSelectedItem().toString();
    }

    private void initialiseViewsToShowLoading() {
        parentView.findViewById(R.id.activity_moods_progress_spinner).setVisibility(View.VISIBLE);
        parentView.findViewById(R.id.mood_parent).setVisibility(View.GONE);
    }

    @Override
    protected Map<Integer, Integer> doInBackground(Void... params) {
        Activity activity = new ActivityDbHelper(context).getActivity(activityName);

        List<MoodEntry> moodEntriesForActivity = new MoodEntryActivityDbHelper(context)
                .getMoodEntriesForActivityId(activity.getId(), timeRange);

        Map<Integer, Integer> moodIdsAndCount = new HashMap<>();
        // initialise map with 0 count for moods
        for (int i = 0; i < resources.getStringArray(R.array.mood_values).length; i++) {
            moodIdsAndCount.put(i, 0);
        }

        for (MoodEntry moodEntry : moodEntriesForActivity) {
            int count = moodIdsAndCount.get(moodEntry.getMoodId());
            moodIdsAndCount.put(moodEntry.getMoodId(), count + 1);
        }

        return moodIdsAndCount;
    }

    @Override
    protected void onPostExecute(Map<Integer, Integer> moodIdsAndCount) {
        parentView.findViewById(R.id.activity_moods_progress_spinner).setVisibility(View.GONE);
        parentView.findViewById(R.id.mood_parent).setVisibility(View.VISIBLE);

        setMoodNumber(R.id.mood_angry_number, moodIdsAndCount.get(0));
        setMoodNumber(R.id.mood_sad_number, moodIdsAndCount.get(1));
        setMoodNumber(R.id.mood_neutral_number, moodIdsAndCount.get(2));
        setMoodNumber(R.id.mood_happy_number, moodIdsAndCount.get(3));
        setMoodNumber(R.id.mood_great_number, moodIdsAndCount.get(4));
    }

    private void setMoodNumber(int moodTextResourceId, int moodRating) {
        ((TextView) parentView.findViewById(moodTextResourceId)).setText(Integer.toString(moodRating));
    }
}
