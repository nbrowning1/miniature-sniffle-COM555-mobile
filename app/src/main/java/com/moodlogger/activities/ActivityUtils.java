package com.moodlogger.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.moodlogger.utils.StringUtils;
import com.moodlogger.ThemeEnum;

import java.util.List;

public class ActivityUtils {

    private static final String DEFAULT_HINT_GIVEN_SHARED_PREF_KEY = "hint_given";

    /**
     * Used to generate a pixel value given a DP value
     * When creating views programmatically, measurements are done in pixels while building
     * views in XML uses DP units. Allows view creation to be more consistent between XMl and code
     *
     * @param dp units to convert to px
     * @return px value
     */
    public static int dpToPixels(Resources resources, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

    public static boolean hintGiven(Activity activity) {
        return hintGiven(activity, DEFAULT_HINT_GIVEN_SHARED_PREF_KEY);
    }

    public static boolean hintGiven(Activity activity, String sharedPreferencesKey) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(sharedPreferencesKey, false);
    }

    public static void markHintAsGiven(Activity activity) {
        markHintAsGiven(activity, DEFAULT_HINT_GIVEN_SHARED_PREF_KEY);
    }

    public static void markHintAsGiven(Activity activity, String sharedPreferencesKey) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(sharedPreferencesKey, true);
        editor.apply();
    }

    public static void showAlertDialog(Context context, String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public static void showHintDialog(Context context, String title, String msg) {
        AlertDialog hintDialog = new AlertDialog.Builder(context).create();
        hintDialog.setTitle(title);
        hintDialog.setMessage(msg);
        hintDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "Got it!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );

        // set dialog to show at bottom of screen for hint
        hintDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = hintDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;

        hintDialog.show();
    }

    /**
     * Get all child views for a given view. Returns all leaves in the branch AKA only
     * views that have no children of their own.
     *
     * @param view             to find child views for
     * @param existingChildren used to keep track of children when making recursive calls down the tree
     * @return list of child views for {@code view}
     */
    public static List<View> getChildViews(View view, List<View> existingChildren) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int noOfChildren = viewGroup.getChildCount();
            for (int i = 0; i < noOfChildren; i++) {
                existingChildren = getChildViews(viewGroup.getChildAt(i), existingChildren);
            }
        } else {
            existingChildren.add(view);
        }
        return existingChildren;
    }

    public static boolean textInputIsValid(String input) {
        input = input.trim();
        return !StringUtils.isEmpty(input) &&
                input.length() > 0 &&
                input.length() <= 15 &&
                // only allow alphabetical characters and spaces
                input.matches("[a-zA-Z\\s]+");
    }

    public static boolean isDarkTheme(Context context) {
        int themeId = PreferenceManager.getDefaultSharedPreferences(context).getInt("theme", 0);
        ThemeEnum theme = ThemeEnum.getTheme(themeId);
        switch (theme) {
            case Default:
            case Ocean:
                return false;
            case Dark:
            case Mint:
                return true;
            default:
                return false;
        }
    }

    public static void setSpecificViewTheme(View contextView, boolean isDarkTheme, int lightThemeResId, int darkThemeResId,
                                            int viewResId) {
        final int themeResId = isDarkTheme ? darkThemeResId : lightThemeResId;
        contextView.findViewById(viewResId).setBackgroundResource(themeResId);
    }
}
