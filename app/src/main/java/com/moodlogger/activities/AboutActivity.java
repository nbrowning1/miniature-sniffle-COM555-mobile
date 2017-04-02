package com.moodlogger.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.moodlogger.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
