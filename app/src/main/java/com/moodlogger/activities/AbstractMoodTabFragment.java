package com.moodlogger.activities;

import android.support.v4.app.Fragment;

public abstract class AbstractMoodTabFragment extends Fragment {

    /**
     * Shows a dialog on the activity if this is the first time the user has seen this
     * activity, acting as a user guide. Once shown, it will be marked as having been given
     * in SharedPreferences so it's never shown again.
     */
    protected void showHintIfHintNotGiven() {
        if (!ActivityUtils.hintGiven(getActivity(), getHintGivenSharedPreferencesKey())) {
            showHint();
            ActivityUtils.markHintAsGiven(getActivity(), getHintGivenSharedPreferencesKey());
        }
    }

    /**
     * Forces implementation for fragments / tabs for supplying their own key for showing
     * hints. As the fragments are part of an activity, they must have separate keys for
     * SharedPreferences so they can keep track of whether a hint has been shown per fragment
     *
     * @return the SharedPreferences key for keeping track of hints given for a given fragment
     */
    protected abstract String getHintGivenSharedPreferencesKey();

    /**
     * For showing a hint to the user with some descriptive text of how to use this part of
     * the app
     */
    protected abstract void showHint();

    /**
     * For setting specific sections of the view to keep in line with the user's theme e.g.
     * nested scroll views with custom styling
     */
    protected abstract void setSpecificViewThemes();

    /**
     * For setting up spinners on the view to have their OnItemSelectedListener and
     * selected options initialised
     */
    protected abstract void setupSpinners();
}
