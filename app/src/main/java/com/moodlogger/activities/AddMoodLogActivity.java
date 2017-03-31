package com.moodlogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class AddMoodLogActivity extends AppCompatActivity {

    private static final String ACTIVITY_TAG = "ACTIVITY_id-";
    private static final String SELECTED_ACTIVITY_TAG_PREFIX = "SELECTED_";

    private int selectedMood = -1;
    /* defined explicitly as an ArrayList as ArrayList implements Serializable - used to pass
        in intent to Add Activity activity */
    private ArrayList<Long> selectedActivities = new ArrayList<>();
    private Map<Long, String> generatedActivitiesIdsAndResourceKeys = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mood_log);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buildActivities();
        populateFromIntent();
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

        activitiesLayout.addView(createAddNewActivityView());
    }

    private LinearLayout createDualActivityView(Activity leftActivity, Activity rightActivity) {
        LinearLayout horizontalDualActivityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams dualActivityLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = ActivityUtils.dpToPixels(getResources(), 25);
        dualActivityLayoutParams.setMargins(margin, 0, margin, margin);
        horizontalDualActivityLayout.setLayoutParams(dualActivityLayoutParams);
        horizontalDualActivityLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        horizontalDualActivityLayout.setOrientation(LinearLayout.HORIZONTAL);

        horizontalDualActivityLayout.addView(createSingleActivityView(leftActivity));
        horizontalDualActivityLayout.addView(createSingleActivityView(rightActivity));

        return horizontalDualActivityLayout;
    }

    private LinearLayout createSingleActivityView(Activity activity) {
        LinearLayout singleActivityLayout = createSingleActivityView();

        if (activity != null) {
            singleActivityLayout.addView(createActivityImageButton(activity));
            singleActivityLayout.addView(createActivityText(activity.getName()));
        }

        return singleActivityLayout;
    }

    private LinearLayout createAddNewActivityView() {
        LinearLayout singleActivityLayout = createSingleActivityView();

        singleActivityLayout.addView(createAddActivityImageButton());
        TextView addNewActivityText = createActivityText("");
        addNewActivityText.setText(R.string.activity_add_new);
        singleActivityLayout.addView(addNewActivityText);

        return singleActivityLayout;
    }

    private LinearLayout createSingleActivityView() {
        LinearLayout singleActivityLayout = new LinearLayout(this);
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
        ImageButton activityImageBtn = createActivityImageButton(
                getResources().getIdentifier(activity.getImgKey(), "drawable", this.getPackageName()),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setActivitySelected(view);
                    }
                }
        );

        String uniqueTag = ACTIVITY_TAG + activity.getId();
        activityImageBtn.setTag(uniqueTag);
        generatedActivitiesIdsAndResourceKeys.put(activity.getId(), activity.getImgKey());

        return activityImageBtn;
    }

    private ImageButton createAddActivityImageButton() {
        return createActivityImageButton(R.drawable.add_activity,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addNewActivity();
                    }
                }
        );
    }

    private ImageButton createActivityImageButton(int resId, View.OnClickListener onClickListener) {
        LinearLayout.LayoutParams activityImageLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        activityImageLayoutParams.setMargins(0, 0, ActivityUtils.dpToPixels(getResources(), 10), 0);
        ImageButton activityImageBtn = new ImageButton(this);
        activityImageBtn.setLayoutParams(activityImageLayoutParams);
        activityImageBtn.setBackgroundResource(resId);

        activityImageBtn.setOnClickListener(onClickListener);

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
        selectedMood = MoodEnum.getMoodRating(view.getTag().toString());
        String imgResource = getResources().getResourceEntryName(view.getId());
        String imgSelectedResource = imgResource + "_selected";
        findViewById(view.getId()).setBackgroundResource(getResources().getIdentifier(imgSelectedResource, "drawable", this.getPackageName()));
    }

    private void resetMoods() {
        findViewById(R.id.mood_great).setBackgroundResource(R.drawable.mood_great);
        findViewById(R.id.mood_happy).setBackgroundResource(R.drawable.mood_happy);
        findViewById(R.id.mood_neutral).setBackgroundResource(R.drawable.mood_neutral);
        findViewById(R.id.mood_sad).setBackgroundResource(R.drawable.mood_sad);
        findViewById(R.id.mood_angry).setBackgroundResource(R.drawable.mood_angry);
    }

    public void setActivitySelected(View view) {

        long activityId = parseActivityIdFromView(view);

        String newResourceName;
        String tag = view.getTag().toString();
        if (tag.contains(SELECTED_ACTIVITY_TAG_PREFIX)) {
            // set back to initial resource name
            newResourceName = generatedActivitiesIdsAndResourceKeys.get(activityId);
            selectedActivities.remove(activityId);
            view.setTag(tag.replace(SELECTED_ACTIVITY_TAG_PREFIX, ""));
        } else {
            newResourceName = generatedActivitiesIdsAndResourceKeys.get(activityId) + "_selected";
            selectedActivities.add(activityId);
            view.setTag(SELECTED_ACTIVITY_TAG_PREFIX + tag);
        }

        view.setBackgroundResource(getResources().getIdentifier(newResourceName, "drawable", this.getPackageName()));
    }

    private long parseActivityIdFromView(View view) {
        String activityTag = view.getTag().toString();
        // get unique activity ID from end of tag
        return Long.parseLong(activityTag.substring(activityTag.indexOf("id-") + 3));
    }

    private void populateFromIntent() {
        populateMoodFromIntent();
        populateActivitiesFromIntent();
    }

    private void populateMoodFromIntent() {
        int moodRating = getIntent().getIntExtra("mood_selected", -1);
        String targetTag = MoodEnum.getTagName(moodRating);
        if (targetTag.isEmpty()) {
            return;
        }

        List<View> moodChildViews = ActivityUtils.getChildViews(findViewById(R.id.mood_parent), new ArrayList<View>());
        for (View view : moodChildViews) {
            if (view.getTag() != null && view.getTag().toString().equals(targetTag)) {
                setMoodSelected(view);
                return;
            }
        }
    }

    private void populateActivitiesFromIntent() {
        ArrayList<Long> activityIds = (ArrayList) getIntent().getSerializableExtra("activities_selected");
        if (activityIds == null) {
            return;
        }

        List<View> activityChildViews = ActivityUtils.getChildViews(findViewById(R.id.activities_root), new ArrayList<View>());
        for (View view : activityChildViews) {
            for (long activityId : activityIds) {
                String targetTag = ACTIVITY_TAG + activityId;
                if (view.getTag() != null && view.getTag().toString().equals(targetTag)) {
                    setActivitySelected(view);
                }
            }
        }
    }

    private void addNewActivity() {
        Intent intent = new Intent(AddMoodLogActivity.this, AddActivityActivity.class);
        // pass extras to activity creation to restore later
        intent.putExtra("mood_selected", selectedMood);
        intent.putExtra("activities_selected", selectedActivities);
        startActivity(intent);
    }

    public void finishMoodLog(View view) {
        if (selectedMood == -1 || selectedActivities.isEmpty()) {
            ActivityUtils.showAlert(this, "Select a mood and at least one activity");
            return;
        }

        ActivityDbHelper activityDbHelper = new ActivityDbHelper(getBaseContext());
        List<Activity> activities = new ArrayList<>();
        for (long activityId : selectedActivities) {
            activities.add(activityDbHelper.getActivity(activityId));
        }

        MoodEntry moodEntry = new MoodEntry(54.607868f, -5.926437f, selectedMood, activities);
        new MoodEntryDbHelper(getBaseContext()).create(moodEntry);

        Intent intent = new Intent(AddMoodLogActivity.this, MainActivity.class);
        startActivity(intent);

        Toast.makeText(AddMoodLogActivity.this, "Mood log added!",
                Toast.LENGTH_LONG).show();
    }
}
