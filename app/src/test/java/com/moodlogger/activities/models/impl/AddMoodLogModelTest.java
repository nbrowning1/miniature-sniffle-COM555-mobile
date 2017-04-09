package com.moodlogger.activities.models.impl;

import android.content.Context;

import com.moodlogger.activities.presenters.impl.AddMoodLogPresenterImpl;
import com.moodlogger.activities.views.intf.AddMoodLogView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class AddMoodLogModelTest {

    @Mock
    Context context;
    @Mock
    AddMoodLogView view;

    private AddMoodLogPresenterImpl listener;
    private AddMoodLogModelImpl model;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        listener = new AddMoodLogPresenterImpl(view, context);
        model = Mockito.spy(new AddMoodLogModelImpl(context));
        doNothing().when(model).saveMoodLog(anyInt(), ArgumentMatchers.<Long>anyList());
    }

    private static final List<Long> VALID_ACTIVITY_LIST = Arrays.asList(1L);

    @Test
    public void itTriggersValidationForInvalidMood() {
        testFinish(-1, VALID_ACTIVITY_LIST, false);
    }

    @Test
    public void itTriggersValidationForInvalidActivities() {
        testFinish(1, new ArrayList<Long>(), false);
    }

    @Test
    public void itSucceedsForValidMoodAndActivitySelected() {
        testFinish(1, VALID_ACTIVITY_LIST, true);
    }

    private void testFinish(int selectedMood, List<Long> selectedActivities, boolean successExpected) {
        model.finish(selectedMood, selectedActivities, listener);
        if (successExpected) {
            verify(view, times(0)).showValidationDialog();
            verify(view, times(1)).navigateToMain();
        } else {
            verify(view, times(1)).showValidationDialog();
            verify(view, times(0)).navigateToMain();
        }
    }
}
