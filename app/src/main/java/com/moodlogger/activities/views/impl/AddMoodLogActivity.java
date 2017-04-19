package com.moodlogger.activities.views.impl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moodlogger.MoodEnum;
import com.moodlogger.R;
import com.moodlogger.activities.AbstractMoodActivity;
import com.moodlogger.activities.ActivityUtils;
import com.moodlogger.activities.main.MainActivity;
import com.moodlogger.activities.presenters.impl.AddMoodLogPresenterImpl;
import com.moodlogger.activities.presenters.intf.AddMoodLogPresenter;
import com.moodlogger.activities.views.intf.AddMoodLogView;
import com.moodlogger.db.entities.Activity;
import com.moodlogger.db.helpers.ActivityDbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMoodLogActivity extends AbstractMoodActivity implements AddMoodLogView {

    private static int debugHintsCount = 0;

    private static final int ADD_ACTIVITY_REQUEST_CODE = 1;
    private static final String ACTIVITY_VIEW_TAG = "ACTIVITY_id-";
    private static final String SELECTED_ACTIVITY_VIEW_TAG_PREFIX = "SELECTED_";

    private static final String MOOD_RESTORE_KEY = "mood_selected";
    private static final String ACTIVITIES_RESTORE_KEY = "activities_selected";

    private static final String DARK_THEME_ACTIVITY_RESOURCE_SUFFIX = "_white";
    private static final String SELECTED_ACTIVITY_RESOURCE_SUFFIX = "_selected";

    private int selectedMood = -1;
    /* defined explicitly as an ArrayList as ArrayList implements Serializable - used to pass
        in intent to Add Activity activity */
    private ArrayList<Long> selectedActivities = new ArrayList<>();
    private Map<Long, String> generatedActivitiesIdsAndResourceKeys = new HashMap<>();

    private boolean isDarkTheme;

    private AddMoodLogPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isDarkTheme = ActivityUtils.isDarkTheme(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSpecificViewThemes();
        buildActivities();
        restoreView(savedInstanceState);
        findViewById(R.id.add_mood_log_add_mood_button).setOnClickListener(onAddMoodLog());

        presenter = new AddMoodLogPresenterImpl(this, this);

        // TODO: change to sharedPreferences
        if (debugHintsCount < 1) {
            debugHintsCount++;
            showHint();
        }
    }

    private void showHint() {
        ActivityUtils.showHintDialog(this,
                getResources().getString(R.string.add_mood_log_hint_title),
                getResources().getString(R.string.add_mood_log_hint_message));
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.add_mood_log;
    }

    @Override
    public void showValidationDialog() {
        ActivityUtils.showAlertDialog(this, "Select a mood and at least one activity");
    }

    @Override
    public void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        Toast.makeText(AddMoodLogActivity.this, "Mood log added!",
                Toast.LENGTH_LONG).show();

        finish();
    }

    private View.OnClickListener onAddMoodLog() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // need location permission for saving latitude / longitude
                if (ActivityCompat.checkSelfPermission(AddMoodLogActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                    ActivityCompat.requestPermissions(AddMoodLogActivity.this, permissions, PackageManager.PERMISSION_GRANTED);
                    return;
                }
                presenter.validateMoodLog(selectedMood, selectedActivities);
            }
        };
    }

    private void setSpecificViewThemes() {
        // will take care of any theme-related changes for mood icons
        resetMoods();
        View contextView = findViewById(android.R.id.content);
        ActivityUtils.setSpecificViewTheme(contextView, isDarkTheme,
                R.drawable.add_mood_finish, R.drawable.add_mood_finish_white, R.id.add_mood_log_add_mood_button);
    }

    private void resetMoods() {
        View contextView = findViewById(android.R.id.content);
        ActivityUtils.setSpecificViewTheme(contextView, isDarkTheme(),
                R.drawable.mood_great, R.drawable.mood_great_white, R.id.mood_great);
        ActivityUtils.setSpecificViewTheme(contextView, isDarkTheme(),
                R.drawable.mood_happy, R.drawable.mood_happy_white, R.id.mood_happy);
        ActivityUtils.setSpecificViewTheme(contextView, isDarkTheme(),
                R.drawable.mood_neutral, R.drawable.mood_neutral_white, R.id.mood_neutral);
        ActivityUtils.setSpecificViewTheme(contextView, isDarkTheme(),
                R.drawable.mood_sad, R.drawable.mood_sad_white, R.id.mood_sad);
        ActivityUtils.setSpecificViewTheme(contextView, isDarkTheme(),
                R.drawable.mood_angry, R.drawable.mood_angry_white, R.id.mood_angry);
    }

    private void buildActivities() {
        LinearLayout activitiesLayout = (LinearLayout) findViewById(R.id.activities_root);
        List<Activity> activities = new ActivityDbHelper(getApplicationContext()).getActivities();

        addActivitiesToActivityPanes(activities);

        activitiesLayout.addView(createAddNewActivityView());
    }

    /* building each pane of activities - should appear left to right, top to bottom from oldest
            to newest activity */
    private void addActivitiesToActivityPanes(List<Activity> activities) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // portrait orientation = space for 2 panes (left and right)
            for (int i = 0; i < activities.size(); i += 2) {
                Activity leftActivity = activities.get(i);
                ((LinearLayout) findViewById(R.id.activities_pane_left))
                        .addView(createSingleActivityView(leftActivity));

                if ((i + 1) < activities.size()) {
                    Activity rightActivity = activities.get(i + 1);
                    ((LinearLayout) findViewById(R.id.activities_pane_right))
                            .addView(createSingleActivityView(rightActivity));
                }
            }
        } else {
            // portrait orientation = space for 2 panes (left and right)
            for (int i = 0; i < activities.size(); i += 3) {
                Activity leftActivity = activities.get(i);
                ((LinearLayout) findViewById(R.id.activities_pane_left))
                        .addView(createSingleActivityView(leftActivity));

                if ((i + 1) < activities.size()) {
                    Activity midActivity = activities.get(i + 1);
                    ((LinearLayout) findViewById(R.id.activities_pane_mid))
                            .addView(createSingleActivityView(midActivity));
                }
                if ((i + 2) < activities.size()) {
                    Activity rightActivity = activities.get(i + 2);
                    ((LinearLayout) findViewById(R.id.activities_pane_right))
                            .addView(createSingleActivityView(rightActivity));
                }
            }
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
        LinearLayout singleActivityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams singleActivityLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                ActivityUtils.dpToPixels(getResources(), 50));
        singleActivityLayoutParams.setMargins(0, 0, 0, ActivityUtils.dpToPixels(getResources(), 25));

        singleActivityLayout.setLayoutParams(singleActivityLayoutParams);

        return singleActivityLayout;
    }

    private ImageButton createActivityImageButton(Activity activity) {
        String imageResource = getThemeDependentResource(activity.getImgKey());
        ImageButton activityImageBtn = createActivityImageButton(
                getResources().getIdentifier(imageResource, "drawable", this.getPackageName()),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setActivitySelected(view);
                    }
                }
        );

        String uniqueTag = ACTIVITY_VIEW_TAG + activity.getId();
        activityImageBtn.setTag(uniqueTag);
        generatedActivitiesIdsAndResourceKeys.put(activity.getId(), activity.getImgKey());

        return activityImageBtn;
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
        activityTextLayoutParams.gravity = Gravity.CENTER;

        TextView activityText = new TextView(this);
        activityText.setLayoutParams(activityTextLayoutParams);
        activityText.setText(activityName);
        activityText.setTextSize(15f);

        return activityText;
    }

    private LinearLayout createAddNewActivityView() {
        LinearLayout singleActivityLayout = createSingleAddNewActivityView();

        singleActivityLayout.addView(createAddActivityImageButton());
        TextView addNewActivityText = createActivityText("");
        addNewActivityText.setText(R.string.activity_add_new);
        singleActivityLayout.addView(addNewActivityText);

        return singleActivityLayout;
    }

    private LinearLayout createSingleAddNewActivityView() {
        LinearLayout singleActivityLayout = new LinearLayout(this);
        // width = 0 as width relies on the weight of the layout params (ensuring equal width activities side-by-side)
        LinearLayout.LayoutParams singleActivityLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        singleActivityLayoutParams.setMargins(0, 0, 0, ActivityUtils.dpToPixels(getResources(), 25));
        singleActivityLayout.setLayoutParams(singleActivityLayoutParams);
        singleActivityLayout.setGravity(Gravity.CENTER);

        return singleActivityLayout;
    }

    private ImageButton createAddActivityImageButton() {
        int resourceId = isDarkTheme ? R.drawable.add_activity_white : R.drawable.add_activity;
        return createActivityImageButton(resourceId,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addNewActivity();
                    }
                }
        );
    }

    public void setMoodSelected(View view) {
        resetMoods();
        selectedMood = MoodEnum.getMoodRating(view.getTag().toString());
        String imgResource = getResources().getResourceEntryName(view.getId());
        // remove theme modifier if present as selected icon is same for both themes anyway
        imgResource = imgResource.replace(DARK_THEME_ACTIVITY_RESOURCE_SUFFIX, "");
        String imgSelectedResource = imgResource + SELECTED_ACTIVITY_RESOURCE_SUFFIX;
        findViewById(view.getId()).setBackgroundResource(getResources().getIdentifier(imgSelectedResource, "drawable", this.getPackageName()));
    }

    public void setActivitySelected(View view) {

        long activityId = parseActivityIdFromView(view);

        String newResourceName;
        String tag = view.getTag().toString();
        if (tag.contains(SELECTED_ACTIVITY_VIEW_TAG_PREFIX)) {
            // set back to initial resource name
            newResourceName = getThemeDependentResource(generatedActivitiesIdsAndResourceKeys.get(activityId));
            selectedActivities.remove(activityId);
            view.setTag(tag.replace(SELECTED_ACTIVITY_VIEW_TAG_PREFIX, ""));
        } else {
            newResourceName = generatedActivitiesIdsAndResourceKeys.get(activityId) + SELECTED_ACTIVITY_RESOURCE_SUFFIX;
            selectedActivities.add(activityId);
            view.setTag(SELECTED_ACTIVITY_VIEW_TAG_PREFIX + tag);
        }

        view.setBackgroundResource(getResources().getIdentifier(newResourceName, "drawable", this.getPackageName()));
    }

    private long parseActivityIdFromView(View view) {
        String activityTag = view.getTag().toString();
        // get unique activity ID from end of tag
        return Long.parseLong(activityTag.substring(activityTag.indexOf("id-") + 3));
    }

    private void restoreView(Bundle savedState) {
        restoreMoodView(savedState);
        restoreActivitiesView(savedState);
    }

    private void restoreMoodView(Bundle savedState) {
        int moodRating = getMoodRatingFromSavedStateOrIntent(savedState);
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

    private void restoreActivitiesView(Bundle savedState) {
        ArrayList<Long> activityIds = getActivitiesFromSavedStateOrIntent(savedState);
        if (activityIds == null) {
            return;
        }

        List<View> activityChildViews = ActivityUtils.getChildViews(findViewById(R.id.activities_root), new ArrayList<View>());
        for (View view : activityChildViews) {
            for (long activityId : activityIds) {
                String targetTag = ACTIVITY_VIEW_TAG + activityId;
                if (view.getTag() != null && view.getTag().toString().equals(targetTag)) {
                    setActivitySelected(view);
                }
            }
        }
    }

    /**
     * Try to find hook for restoring selected mood if applicable, by looking at bundle passed
     * from onCreate and falling back to getIntent() in case where we're coming from
     * a successful 'Add Activity' action
     */
    private int getMoodRatingFromSavedStateOrIntent(Bundle savedState) {
        if (savedState != null && savedState.getInt(MOOD_RESTORE_KEY, -1) != -1) {
            return savedState.getInt(MOOD_RESTORE_KEY, -1);
        } else {
            return getIntent().getIntExtra(MOOD_RESTORE_KEY, -1);
        }
    }

    /**
     * Try to find hook for restoring selected activities if applicable, by looking at bundle passed
     * from onCreate and falling back to getIntent() in case where we're coming from
     * a successful 'Add Activity' action
     */
    private ArrayList<Long> getActivitiesFromSavedStateOrIntent(Bundle savedState) {
        if (savedState != null && savedState.getSerializable(ACTIVITIES_RESTORE_KEY) != null) {
            return (ArrayList) savedState.getSerializable(ACTIVITIES_RESTORE_KEY);
        } else {
            return (ArrayList) getIntent().getSerializableExtra(ACTIVITIES_RESTORE_KEY);
        }
    }

    private void addNewActivity() {
        Intent intent = new Intent(this, AddNewActivityActivity.class);
        // pass extras to activity creation to restore later if new activity is created successfully
        intent.putExtra(MOOD_RESTORE_KEY, selectedMood);
        intent.putExtra(ACTIVITIES_RESTORE_KEY, selectedActivities);
        startActivityForResult(intent, ADD_ACTIVITY_REQUEST_CODE);
    }

    private String getThemeDependentResource(String originalResource) {
        if (isDarkTheme) {
            originalResource += DARK_THEME_ACTIVITY_RESOURCE_SUFFIX;
        }
        return originalResource;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save mood and activities to restore later
        outState.putInt(MOOD_RESTORE_KEY, selectedMood);
        outState.putSerializable(ACTIVITIES_RESTORE_KEY, selectedActivities);
    }
}
