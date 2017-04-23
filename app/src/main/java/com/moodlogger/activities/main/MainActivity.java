package com.moodlogger.activities.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

import com.moodlogger.MainActivityFragmentPagerAdapter;
import com.moodlogger.R;
import com.moodlogger.enums.ThemeEnum;
import com.moodlogger.activities.AbstractMoodActivity;
import com.moodlogger.activities.views.impl.AddMoodLogActivity;
import com.moodlogger.activities.views.impl.SettingsActivity;
import com.moodlogger.activities.views.impl.WelcomeActivity;

public class MainActivity extends AbstractMoodActivity {

    private static final String TAB_ONE_NAME = "Summary";
    private static final String TAB_TWO_NAME = "View";
    private static final String TAB_THREE_NAME = "Evaluate";

    private static int selectedTab = 0;

    private ViewPager viewPager;
    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean welcomeGiven = sharedPreferences.getBoolean("welcome_given", false);
        if (!welcomeGiven) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getToolbarColor());
        setSupportActionBar(toolbar);

        setupSwipeAbility();
        setupTabs();
    }

    @Override
    protected void setupTheme() {
        int themeId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("theme", 0);
        ThemeEnum theme = ThemeEnum.getTheme(themeId);
        switch (theme) {
            case Default:
                setTheme(R.style.MainTheme);
                break;
            case Dark:
                setTheme(R.style.DarkMainTheme);
                break;
            case Ocean:
                setTheme(R.style.OceanMainTheme);
                break;
            case Mint:
                setTheme(R.style.MintMainTheme);
                break;
        }
    }

    private int getToolbarColor() {
        int themeId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("theme", 0);
        ThemeEnum theme = ThemeEnum.getTheme(themeId);
        switch (theme) {
            case Default:
                return ContextCompat.getColor(this, R.color.colorPrimary);
            case Dark:
                return ContextCompat.getColor(this, R.color.darkColorPrimary);
            case Ocean:
                return ContextCompat.getColor(this, R.color.oceanColorPrimary);
            case Mint:
                return ContextCompat.getColor(this, R.color.mintColorPrimary);
            default:
                return ContextCompat.getColor(this, R.color.colorPrimary);
        }
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupSwipeAbility() {
        MainActivityFragmentPagerAdapter mainActivityFragmentPagerAdapter = new MainActivityFragmentPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(mainActivityFragmentPagerAdapter);
        viewPager.addOnPageChangeListener(onTabSwipeListener());
    }

    private void setupTabs() {
        tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.tabContent);

        tabHost.addTab(tabHost.newTabSpec(TAB_ONE_NAME).setIndicator(TAB_ONE_NAME),
                SummaryTabFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(TAB_TWO_NAME).setIndicator(TAB_TWO_NAME),
                ViewTabFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(TAB_THREE_NAME).setIndicator(TAB_THREE_NAME),
                EvaluateTabFragment.class, null);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                int currentTab = tabHost.getCurrentTab();
                selectedTab = currentTab;
                viewPager.setCurrentItem(currentTab);
            }
        });

        // restore state
        tabHost.setCurrentTab(selectedTab);
    }

    public void addMoodLog(View view) {
        Intent intent = new Intent(this, AddMoodLogActivity.class);
        startActivity(intent);
    }

    private ViewPager.OnPageChangeListener onTabSwipeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            // Manages the Page changes, synchronizing it with Tabs
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                int currentTab = viewPager.getCurrentItem();
                selectedTab = currentTab;
                tabHost.setCurrentTab(currentTab);
            }

            @Override
            public void onPageSelected(int arg0) {
            }
        };
    }
}
