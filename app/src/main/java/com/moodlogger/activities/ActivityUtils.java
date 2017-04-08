package com.moodlogger.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.moodlogger.R;
import com.moodlogger.ThemeEnum;

import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ActivityUtils {

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
        hintDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
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
     * @param view to find child views for
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

    public static void hideSoftKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);

        // verify if the soft keyboard is open
        if (imm.isAcceptingText() && activity.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
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
