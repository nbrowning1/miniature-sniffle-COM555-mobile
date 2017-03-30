package com.moodlogger.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

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

    public static void showAlert(Context context, String msg) {
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
}
