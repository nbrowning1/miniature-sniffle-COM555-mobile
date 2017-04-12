package com.moodlogger.activities.views.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.moodlogger.R;
import com.moodlogger.activities.ActivityUtils;
import com.moodlogger.activities.main.MainActivity;
import com.moodlogger.activities.presenters.impl.WelcomePresenterImpl;
import com.moodlogger.activities.presenters.intf.WelcomePresenter;
import com.moodlogger.activities.views.intf.WelcomeView;

public class WelcomeActivity extends AppCompatActivity implements WelcomeView, View.OnClickListener {

    private EditText name;
    private WelcomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        name = (EditText) findViewById(R.id.welcome_name_text);
        findViewById(R.id.welcome_finish).setOnClickListener(this);

        presenter = new WelcomePresenterImpl(this, getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void navigateToMain() {
        hideSoftKeyBoard();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        // finish activity so user can't return
        finish();
    }

    @Override
    public void onClick(View v) {
        presenter.validateName(name.getText().toString());
    }

    @Override
    public void showValidationDialog() {
        ActivityUtils.showAlertDialog(this, "Please enter a valid name (up to 15 alphabetical characters only)");
    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        // verify if the soft keyboard is open
        if (imm.isAcceptingText() && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
