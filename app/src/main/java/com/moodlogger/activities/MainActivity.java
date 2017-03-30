package com.moodlogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.moodlogger.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAB_ONE_NAME = "Summary";
    private static final String TAB_TWO_NAME = "View";
    private static final String TAB_THREE_NAME = "Evaluate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupTabs();
    }

    private void setupTabs() {
        FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.tabContent);

        tabHost.addTab(tabHost.newTabSpec(TAB_ONE_NAME).setIndicator(TAB_ONE_NAME),
                SummaryTabFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(TAB_TWO_NAME).setIndicator(TAB_TWO_NAME),
                ViewTabFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(TAB_THREE_NAME).setIndicator(TAB_THREE_NAME),
                EvaluateTabFragment.class, null);
    }

    public void addMoodLog(View view) {
        Intent intent = new Intent(this, AddMoodLogActivity.class);
        startActivity(intent);
    }
}
