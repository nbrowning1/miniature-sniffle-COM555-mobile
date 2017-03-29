package com.moodlogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moodlogger.MoodEnum;
import com.moodlogger.R;
import com.moodlogger.db.entities.Activity;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.helpers.ActivityDbHelper;
import com.moodlogger.db.helpers.MoodEntryDbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMoodLogActivity extends AbstractActivity {

    private int selectedMood = -1;
    private long selectedActivity = -1L;
    private Map<Long, String> generatedActivitiesIdsAndResourceKeys = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mood_log);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buildActivities();
    }

    private void buildActivities() {
        LinearLayout activitiesLayout = (LinearLayout) findViewById(R.id.activities_root);
        List<Activity> activities = new ActivityDbHelper(getBaseContext()).getActivities();
        for (int i = 0; i < activities.size(); i += 2) {
            Activity leftActivity = activities.get(i);
            Activity rightActivity = (i + 1) >= activities.size() ?
                    null :
                    activities.get(i + 1);
            activitiesLayout.addView(createDualActivityView(leftActivity, rightActivity));
        }
    }

    private LinearLayout createDualActivityView(Activity leftActivity, Activity rightActivity) {
        LinearLayout horizontalDualActivityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams dualActivityLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dualActivityLayoutParams.setMargins(dpToPixels(25), 0, dpToPixels(25), dpToPixels(25));
        horizontalDualActivityLayout.setLayoutParams(dualActivityLayoutParams);
        horizontalDualActivityLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        horizontalDualActivityLayout.setOrientation(LinearLayout.HORIZONTAL);

        horizontalDualActivityLayout.addView(createSingleActivityView(leftActivity));
        horizontalDualActivityLayout.addView(createSingleActivityView(rightActivity));

        return horizontalDualActivityLayout;
    }

    private LinearLayout createSingleActivityView(Activity activity) {
        LinearLayout singleActivityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams singleActivityLayoutParams = new LinearLayout.LayoutParams(
                dpToPixels(125),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        singleActivityLayoutParams.setMargins(dpToPixels(20), 0, dpToPixels(20), 0);
        singleActivityLayout.setLayoutParams(singleActivityLayoutParams);
        singleActivityLayout.setGravity(Gravity.CENTER_VERTICAL);

        if (activity != null) {
            singleActivityLayout.addView(createActivityImageButton(activity));
            singleActivityLayout.addView(createActivityText(activity.getName()));
        }

        return singleActivityLayout;
    }

    private ImageButton createActivityImageButton(Activity activity) {
        LinearLayout.LayoutParams activityImageLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        activityImageLayoutParams.setMargins(0, 0, dpToPixels(10), 0);
        ImageButton activityImageBtn = new ImageButton(this);
        activityImageBtn.setLayoutParams(activityImageLayoutParams);
        activityImageBtn.setBackgroundResource(getResources().getIdentifier(activity.getImgKey(), "drawable", this.getPackageName()));

        String uniqueTag = "ACTIVITY_" + activity.getId();
        activityImageBtn.setTag(uniqueTag);
        generatedActivitiesIdsAndResourceKeys.put(activity.getId(), activity.getImgKey());

        activityImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActivitySelected(view);
            }
        });

        return activityImageBtn;
    }

    private TextView createActivityText(String activityName) {
        LinearLayout.LayoutParams activityTextLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView activityText = new TextView(this);
        activityText.setLayoutParams(activityTextLayoutParams);
        activityText.setText(activityName);
        activityText.setTextSize(15f);

        return activityText;
    }

    public void setMoodSelected(View view) {
        resetMoods();
        int moodId = view.getId();
        selectedMood = MoodEnum.getMoodRating(view.getTag().toString());
        String imgResource = getResources().getResourceEntryName(moodId);
        String imgSelectedResource = imgResource + "_selected";
        findViewById(moodId).setBackgroundResource(getResources().getIdentifier(imgSelectedResource, "drawable", this.getPackageName()));
    }

    private void resetMoods() {
        findViewById(R.id.mood_great).setBackgroundResource(R.drawable.mood_great);
        findViewById(R.id.mood_happy).setBackgroundResource(R.drawable.mood_happy);
        findViewById(R.id.mood_neutral).setBackgroundResource(R.drawable.mood_neutral);
        findViewById(R.id.mood_sad).setBackgroundResource(R.drawable.mood_sad);
        findViewById(R.id.mood_angry).setBackgroundResource(R.drawable.mood_angry);
    }

    public void setActivitySelected(View view) {
        resetActivities();
        // fetch activity id from DB
        long activityId = parseActivityIdFromView(view);
        String activitySelectedResource = generatedActivitiesIdsAndResourceKeys.get(activityId) + "_selected";
        view.setBackgroundResource(getResources().getIdentifier(activitySelectedResource, "drawable", this.getPackageName()));
        selectedActivity = activityId;
    }

    private void resetActivities() {
        ViewGroup activitiesRoot = (ViewGroup) findViewById(R.id.activities_root);
        List<View> allActivityViews = getChildViews(activitiesRoot, new ArrayList<View>());
        for (View activityView : allActivityViews) {
            if (activityView.getTag() != null && activityView.getTag().toString().contains("ACTIVITY")) {
                long activityId = parseActivityIdFromView(activityView);
                // set background resource back to un-selected resource
                String resetResourceKey = generatedActivitiesIdsAndResourceKeys.get(activityId);
                activityView.setBackgroundResource(getResources().getIdentifier(resetResourceKey, "drawable", this.getPackageName()));
            }
        }
    }

    private long parseActivityIdFromView(View view) {
        String activityTag = view.getTag().toString();
        // get unique activity ID from end of tag
        return Long.parseLong(activityTag.substring(activityTag.indexOf("_") + 1));
    }

    private List<View> getChildViews(View view, List<View> existingChildren) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int noOfChildren = viewGroup.getChildCount();
            for (int i = 0; i < noOfChildren; i++) {
                existingChildren = getChildViews(viewGroup.getChildAt(i), existingChildren);
            }
        } else {
            existingChildren.add(view);
        }
        return existingChildren;
    }

    public void finishMoodLog(View view) {
        MoodEntryDbHelper dbHelper = new MoodEntryDbHelper(getBaseContext());
        MoodEntry moodEntry = new MoodEntry(23.2304f, 170.3290f, selectedMood);
        dbHelper.create(moodEntry);

        Intent intent = new Intent(AddMoodLogActivity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(AddMoodLogActivity.this, "Mood log added!",
                Toast.LENGTH_LONG).show();
    }
}
