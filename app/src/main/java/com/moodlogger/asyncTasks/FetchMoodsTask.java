package com.moodlogger.asyncTasks;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import com.moodlogger.charts.TimeRangeEnum;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.helpers.MoodEntryDbHelper;

import java.util.List;

public class FetchMoodsTask extends AsyncTask<Void, Void, List<MoodEntry>> {

    private Context context;
    private int moodRating;
    private TimeRangeEnum timeRange;

    public FetchMoodsTask(Context context, int moodRating, TimeRangeEnum timeRange){
        this.context = context;
        this.moodRating = moodRating;
        this.timeRange = timeRange;
    }

    @Override
    protected List<MoodEntry> doInBackground(Void... args) {
        return new MoodEntryDbHelper(context).getMoodEntries(moodRating, timeRange);
    }
}
