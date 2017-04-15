package com.moodlogger.evaluation;

import java.util.ArrayList;
import java.util.List;

public class ActivityEvaluator {

    private static final String GOOD_ACTIVITY_EVALUATION = "%s seems to be linked with good moods. Keep making time for this activity in the future";
    private static final String BAD_ACTIVITY_EVALUATION = "%s is linked with more negative moods than others. Consider evaluating why it makes you feel this way and any changes that can be made to create a healthier environment for this activity";
    private static final String SAD_ACTIVITY_EVALUATION = "%s invokes more gloomy feelings than other activities - are you able to do this activity less? If not, can you take any steps to make this a more positive activity?";
    private static final String ANGRY_ACTIVITY_EVALUATION = "%s has the most anger associated with it. Consider any changes that can be made to create a more relaxed environment for this activity, and don't let it over-stress you";
    private static final String AMBIGUOUS_ACTIVITY_EVALUATION = "%s is a polarising activity - what made this positive at some times and negative at others? Try to maximise the positive aspects of what this activity brings and mitigate the negatives";

    private SortedActivityBundle sortedActivityBundle;

    public ActivityEvaluator(SortedActivityBundle sortedActivityBundle) {
        this.sortedActivityBundle = sortedActivityBundle;
    }

    public List<String> getActivityEvaluations() {
        List<String> activityEvaluations = new ArrayList<>();
        if (sortedActivityBundle.getGoodRatingActivity().equals(sortedActivityBundle.getBadRatingActivity())) {
            activityEvaluations.add(String.format(AMBIGUOUS_ACTIVITY_EVALUATION, sortedActivityBundle.getGoodRatingActivity().getName()));
        } else {
            activityEvaluations.add(String.format(GOOD_ACTIVITY_EVALUATION, sortedActivityBundle.getGoodRatingActivity().getName()));
            activityEvaluations.add(String.format(BAD_ACTIVITY_EVALUATION, sortedActivityBundle.getBadRatingActivity().getName()));
        }
        activityEvaluations.add(String.format(SAD_ACTIVITY_EVALUATION, sortedActivityBundle.getSadRatingActivity().getName()));
        activityEvaluations.add(String.format(ANGRY_ACTIVITY_EVALUATION, sortedActivityBundle.getAngryRatingActivity().getName()));

        return activityEvaluations;
    }
}
