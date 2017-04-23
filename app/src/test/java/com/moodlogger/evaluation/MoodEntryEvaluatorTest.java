package com.moodlogger.evaluation;

import android.content.Context;

import com.moodlogger.db.entities.Activity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;

public class MoodEntryEvaluatorTest {

    @Mock
    Context context;
    MoodEntryEvaluator moodEntryEvaluator;

    @Before
    public void setup() {
        moodEntryEvaluator = new MoodEntryEvaluator(context);
    }

    @Test
    public void itSortsActivitiesCorrectly() {
        List<Map.Entry<Activity, ActivityRating>> activityRatingEntries = new ArrayList<>();
        activityRatingEntries.add(createActivityRating("Good_Activity", 5, 0, 0));
        activityRatingEntries.add(createActivityRating("DoubleGood_Activity", 10, 0, 0));
        activityRatingEntries.add(createActivityRating("Sad_Activity", 0, 15, 10));
        activityRatingEntries.add(createActivityRating("Angry_Activity", 0, 10, 20));

        for (Map.Entry<Activity, ActivityRating> activityRatingEntry : activityRatingEntries) {
            moodEntryEvaluator.activityRatings.put(
                    activityRatingEntry.getKey(), activityRatingEntry.getValue());
        }

        SortedActivityBundle sortedActivityBundle = moodEntryEvaluator.getSortedActivityBundle();
        assertEquals(sortedActivityBundle.getGoodRatingActivity().getName(), "DoubleGood_Activity");
        assertEquals(sortedActivityBundle.getBadRatingActivity().getName(), "Angry_Activity");
        assertEquals(sortedActivityBundle.getSadRatingActivity().getName(), "Sad_Activity");
        assertEquals(sortedActivityBundle.getAngryRatingActivity().getName(), "Angry_Activity");
    }

    private Map.Entry<Activity, ActivityRating> createActivityRating(
            String activityName, int goodRating, int sadRating, int angryRating) {

        Activity activity = new Activity(activityName, "test_key");
        ActivityRating activityRating = new ActivityRating();
        for (int i = 0; i < goodRating; i++) {
            activityRating.addGoodRating();
        }
        for (int i = 0; i < sadRating; i++) {
            activityRating.addSadRating();
        }
        for (int i = 0; i < angryRating; i++) {
            activityRating.addAngryRating();
        }
        return new AbstractMap.SimpleEntry<>(activity, activityRating);
    }
}
