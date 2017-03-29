package com.moodlogger.activities;

import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

public abstract class AbstractActivity extends AppCompatActivity {

    /**
     * Used to generate a pixel value given a DP value
     * When creating views programmatically, measurements are done in pixels while building
     * views in XML uses DP units. Allows view creation to be more consistent between XMl and code
     *
     * @param dp units to convert to px
     * @return px value
     */
    protected int dpToPixels(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}
