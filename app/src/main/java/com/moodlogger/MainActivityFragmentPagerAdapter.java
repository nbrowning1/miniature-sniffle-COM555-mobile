package com.moodlogger;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.moodlogger.activities.main.EvaluateTabFragment;
import com.moodlogger.activities.main.SummaryTabFragment;
import com.moodlogger.activities.main.ViewTabFragment;

public class MainActivityFragmentPagerAdapter extends FragmentPagerAdapter {
    public MainActivityFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new SummaryTabFragment();
        } else if (i == 1) {
            return new ViewTabFragment();
        } else {
            return new EvaluateTabFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
