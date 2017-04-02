package com.moodlogger.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.moodlogger.R;

public class CustomiseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customise);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
