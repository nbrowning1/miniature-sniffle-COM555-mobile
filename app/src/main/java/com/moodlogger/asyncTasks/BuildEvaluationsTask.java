package com.moodlogger.asyncTasks;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.moodlogger.R;
import com.moodlogger.TimeRangeEnum;
import com.moodlogger.activities.ActivityUtils;
import com.moodlogger.evaluation.MoodEntryEvaluator;

import java.util.List;

public class BuildEvaluationsTask extends AsyncTask<Void, Void, List<String>> {

    private Context context;
    private LinearLayout parentView;
    private Resources resources;

    private TimeRangeEnum timeRange;
    private LinearLayout evaluationsView;
    private ProgressBar progressBar;

    public BuildEvaluationsTask(Context context, LinearLayout parentView, Resources resources) {
        this.context = context;
        this.parentView = parentView;
        this.resources = resources;
    }

    @Override
    protected void onPreExecute() {
        evaluationsView = (LinearLayout) parentView.findViewById(R.id.evaluate_text_view);
        progressBar = (ProgressBar) parentView.findViewById(R.id.evaluations_progress_spinner);
        initialiseViewsToShowLoading();
        setValuesFromSpinners();
    }

    private void initialiseViewsToShowLoading() {
        progressBar.setVisibility(View.VISIBLE);
        evaluationsView.removeAllViews();
    }

    private void setValuesFromSpinners() {
        String timeRangeValue = ((Spinner) parentView.findViewById(R.id.evaluate_time_range_spinner))
                .getSelectedItem().toString();
        timeRange = TimeRangeEnum.getEnum(timeRangeValue);
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        return new MoodEntryEvaluator(context).getEvaluations(timeRange);
    }

    @Override
    protected void onPostExecute(List<String> evaluations) {
        buildEvaluationsView(evaluations);
    }

    private void buildEvaluationsView(List<String> evaluations) {
        // hide progress spinner
        progressBar.setVisibility(View.GONE);
        evaluationsView.removeAllViews();
        for (String evaluation : evaluations) {
            evaluationsView.addView(buildEvaluationTextView(evaluation));
        }
    }

    private TextView buildEvaluationTextView(String text) {
        LinearLayout.LayoutParams evaluationTextLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        evaluationTextLayoutParams.setMargins(0, ActivityUtils.dpToPixels(resources, 15), 0, ActivityUtils.dpToPixels(resources, 15));
        TextView evaluationText = new TextView(context);
        evaluationText.setLayoutParams(evaluationTextLayoutParams);
        evaluationText.setText(text);
        evaluationText.setTextSize(15f);
        evaluationText.setGravity(Gravity.CENTER);
        evaluationText.setTypeface(evaluationText.getTypeface(), Typeface.ITALIC);
        evaluationText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        return evaluationText;
    }
}
