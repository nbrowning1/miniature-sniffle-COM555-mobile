package com.moodlogger.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.moodlogger.R;

public class WelcomeActivity extends AbstractMoodActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.welcome;
    }

    public void finishWelcome(View view) {
        String name = ((EditText) findViewById(R.id.welcome_name_text)).getText().toString();
        if (name.isEmpty()) {
            ActivityUtils.showAlertDialog(this, "Please enter your name");
            return;
        }

        ActivityUtils.hideSoftKeyBoard(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("welcome_given", true);
        editor.putString("user_name", name);
        // commit as we want to save this immediately - can show up in immediate hint
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        // finish activity so user can't return
        finish();
    }
}
