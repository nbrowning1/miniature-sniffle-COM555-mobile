package com.moodlogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.moodlogger.R;
import com.moodlogger.db.entities.MoodEntry;
import com.moodlogger.db.helpers.MoodDbHelper;
import com.moodlogger.db.helpers.MoodEntryDbHelper;

public class AddMoodLogActivity extends AppCompatActivity {

    private static int selectedMood = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mood_log);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setMoodSelected(View view) {
        resetMoods();
        int moodId = view.getId();
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

    public void finishMoodLog(View view) {
        MoodEntryDbHelper dbHelper = new MoodEntryDbHelper(getBaseContext());
        MoodEntry moodEntry = new MoodEntry(23.2304f, 170.3290f, 3);
        dbHelper.create(moodEntry);

        Intent intent = new Intent(AddMoodLogActivity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(AddMoodLogActivity.this, "Mood log added!",
                Toast.LENGTH_LONG).show();
    }
}
