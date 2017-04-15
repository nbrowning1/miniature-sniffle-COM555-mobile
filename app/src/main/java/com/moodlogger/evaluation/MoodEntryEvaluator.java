package com.moodlogger.evaluation;

import android.content.Context;

import com.moodlogger.MoodEnum;
import com.moodlogger.TimeRangeEnum;
import com.moodlogger.db.entities.Activity;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.helpers.MoodEntryDbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoodEntryEvaluator {

    private Context context;

    private Map<Activity, ActivityRating> activityRatings = new HashMap<>();

    public MoodEntryEvaluator(Context context) {
        this.context = context;
    }

    public List<String> getEvaluations(TimeRangeEnum timeRange) {
        List<MoodEntry> moodEntries = new MoodEntryDbHelper(context).getMoodEntries(timeRange);
        for (MoodEntry moodEntry : moodEntries) {
            int moodRating = moodEntry.getMoodId();
            for (Activity moodEntryActivity : moodEntry.getActivities()) {
                addActivityRating(moodRating, moodEntryActivity);
            }
        }

        if (timeRange.equals(TimeRangeEnum.Week)) {
            return new ActivityEvaluator(getSortedActivityBundle())
                    .getActivityEvaluations();
        }
        return new ArrayList<>();
    }

    private void addActivityRating(int moodRating, Activity moodEntryActivity) {
        if (!activityRatings.containsKey(moodEntryActivity)) {
            activityRatings.put(moodEntryActivity, new ActivityRating());
        }
        ActivityRating activityRatingForActivity = activityRatings.get(moodEntryActivity);
        String moodLabel = MoodEnum.getLabelName(moodRating);

        if (moodLabel.equals(MoodEnum.Great.getLabelName()) || moodLabel.equals(MoodEnum.Happy.getLabelName())) {
            activityRatingForActivity.addGoodRating();
        } else if (moodLabel.equals(MoodEnum.Sad.getLabelName())) {
            activityRatingForActivity.addSadRating();
        } else if (moodLabel.equals(MoodEnum.Angry.getLabelName())) {
            activityRatingForActivity.addAngryRating();
        }
    }

    private SortedActivityBundle getSortedActivityBundle() {
        Activity goodRatingActivity = null;
        Activity badRatingActivity = null;
        Activity sadRatingActivity = null;
        Activity angryRatingActivity = null;
        int highestGoodRating = -1;
        int highestBadRating = -1;
        int highestSadRating = -1;
        int highestAngryRating = -1;

        for (Map.Entry<Activity, ActivityRating> activityRatingEntry : activityRatings.entrySet()) {
            if (highestGoodRating < activityRatingEntry.getValue().getGoodRating()) {
                highestGoodRating = activityRatingEntry.getValue().getGoodRating();
                goodRatingActivity = activityRatingEntry.getKey();
            }

            if (highestBadRating < activityRatingEntry.getValue().getBadRating()) {
                highestBadRating = activityRatingEntry.getValue().getBadRating();
                badRatingActivity = activityRatingEntry.getKey();
            }

            if (highestSadRating < activityRatingEntry.getValue().getSadRating()) {
                highestSadRating = activityRatingEntry.getValue().getSadRating();
                sadRatingActivity = activityRatingEntry.getKey();
            }

            if (highestAngryRating < activityRatingEntry.getValue().getAngryRating()) {
                highestAngryRating = activityRatingEntry.getValue().getAngryRating();
                angryRatingActivity = activityRatingEntry.getKey();
            }
        }

        return new SortedActivityBundle(goodRatingActivity, badRatingActivity,
                sadRatingActivity, angryRatingActivity);
    }
}
