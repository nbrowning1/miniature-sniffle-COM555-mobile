package com.moodlogger.evaluation;

import com.moodlogger.db.entities.Activity;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ActivityEvaluatorTest {

    @Test
    public void itGetsRegularActivityEvaluations() {
        Activity goodActivity = new Activity(1L, "Activity1", "test_key");
        Activity badActivity = new Activity(2L, "Activity2", "test_key");
        Activity sadActivity = new Activity(3L, "Activity3", "test_key");
        Activity angryActivity = new Activity(4L, "Activity4", "test_key");
        SortedActivityBundle sortedActivityBundle =
                new SortedActivityBundle(goodActivity, badActivity, sadActivity, angryActivity);

        List<String> actualEvaluations = new ActivityEvaluator(sortedActivityBundle).getActivityEvaluations();
        List<String> expectedEvaluations = Arrays.asList(
                String.format(ActivityEvaluator.GOOD_ACTIVITY_EVALUATION, "Activity1"),
                String.format(ActivityEvaluator.BAD_ACTIVITY_EVALUATION, "Activity2"),
                String.format(ActivityEvaluator.SAD_ACTIVITY_EVALUATION, "Activity3"),
                String.format(ActivityEvaluator.ANGRY_ACTIVITY_EVALUATION, "Activity4"));

        assertEquals(expectedEvaluations, actualEvaluations);
    }

    @Test
    public void itGetsAmbiguousActivityEvaluations() {
        Activity goodBadActivity = new Activity(1L, "Activity1", "test_key");
        Activity sadActivity = new Activity(2L, "Activity2", "test_key");
        Activity angryActivity = new Activity(3L, "Activity3", "test_key");
        SortedActivityBundle sortedActivityBundle =
                new SortedActivityBundle(goodBadActivity, goodBadActivity, sadActivity, angryActivity);

        List<String> actualEvaluations = new ActivityEvaluator(sortedActivityBundle).getActivityEvaluations();
        List<String> expectedEvaluations = Arrays.asList(
                String.format(ActivityEvaluator.AMBIGUOUS_ACTIVITY_EVALUATION, "Activity1"),
                String.format(ActivityEvaluator.SAD_ACTIVITY_EVALUATION, "Activity2"),
                String.format(ActivityEvaluator.ANGRY_ACTIVITY_EVALUATION, "Activity3"));

        assertEquals(expectedEvaluations, actualEvaluations);
    }

    @Test
    public void itGetsPartialActivityEvaluations() {
        Activity goodActivity = null;
        Activity badActivity = new Activity(1L, "Activity1", "test_key");
        Activity sadActivity = null;
        Activity angryActivity = new Activity(2L, "Activity2", "test_key");
        SortedActivityBundle sortedActivityBundle =
                new SortedActivityBundle(goodActivity, badActivity, sadActivity, angryActivity);

        List<String> actualEvaluations = new ActivityEvaluator(sortedActivityBundle).getActivityEvaluations();
        List<String> expectedEvaluations = Arrays.asList(
                String.format(ActivityEvaluator.BAD_ACTIVITY_EVALUATION, "Activity1"),
                String.format(ActivityEvaluator.ANGRY_ACTIVITY_EVALUATION, "Activity2"));

        assertEquals(expectedEvaluations, actualEvaluations);
    }
}
