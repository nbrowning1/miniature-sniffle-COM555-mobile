package com.moodlogger.evaluation;

import com.moodlogger.db.entities.Activity;

public class SortedActivityBundle {

    private Activity goodRatingActivity;
    private Activity badRatingActivity;
    private Activity sadRatingActivity;
    private Activity angryRatingActivity;

    public SortedActivityBundle(Activity goodRatingActivity, Activity badRatingActivity,
                                Activity sadRatingActivity, Activity angryRatingActivity) {
        this.goodRatingActivity = goodRatingActivity;
        this.badRatingActivity = badRatingActivity;
        this.sadRatingActivity = sadRatingActivity;
        this.angryRatingActivity = angryRatingActivity;
    }

    public Activity getGoodRatingActivity() {
        return goodRatingActivity;
    }

    public Activity getBadRatingActivity() {
        return badRatingActivity;
    }

    public Activity getSadRatingActivity() {
        return sadRatingActivity;
    }

    public Activity getAngryRatingActivity() {
        return angryRatingActivity;
    }
}
