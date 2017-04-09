package com.moodlogger.activities.models.impl;

import android.content.Context;

import com.moodlogger.activities.presenters.impl.AddNewActivityPresenterImpl;
import com.moodlogger.activities.views.intf.AddNewActivityView;
import com.moodlogger.db.entities.Activity;
import com.moodlogger.db.helpers.ActivityDbHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddNewActivityModelTest {

    @Mock
    Context context;
    @Mock
    AddNewActivityView view;
    @Mock
    ActivityDbHelper dbHelper;

    private AddNewActivityPresenterImpl listener;
    private AddNewActivityModelImpl model;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Activity mockActivity1 = new Activity("First Activity", "ResourceKey");
        Activity mockActivity2 = new Activity("Second Activity", "ResourceKey");
        when(dbHelper.getActivities()).thenReturn(Arrays.asList(mockActivity1, mockActivity2));
        when(dbHelper.create(any(Activity.class))).thenReturn(-1L);

        listener = new AddNewActivityPresenterImpl(view, context);
        model = spy(new AddNewActivityModelImpl(context));
        doReturn(dbHelper).when(model).getActivityDbHelper();
    }

    @Test
    public void itTriggersGeneralValidationForTooLongActivityName() {
        testAddNewActivity("Too Long Activity Name", "ResourceName", 1, 0, 0);
    }

    @Test
    public void itTriggersGeneralValidationForInvalidActivityName() {
        testAddNewActivity("Activity123", "ResourceName", 1, 0, 0);
    }

    @Test
    public void itTriggersGeneralValidationForNullActivityName() {
        testAddNewActivity(null, "ResourceName", 1, 0, 0);
    }

    @Test
    public void itTriggersGeneralValidationForEmptyActivityName() {
        testAddNewActivity("", "ResourceName", 1, 0, 0);
    }

    @Test
    public void itTriggersGeneralValidationForEmptyResourceName() {
        testAddNewActivity("New Activity", "", 1, 0, 0);
    }

    @Test
    public void itTriggersAlreadyExistsValidationForExistingActivity() {
        testAddNewActivity("First Activity", "ResourceName", 0, 1, 0);
    }

    @Test
    public void itSucceedsForValidNewActivity() {
        testAddNewActivity("New Activity", "ResourceName", 0, 0, 1);
    }

    private void testAddNewActivity(String activityName, String resourceName,
                                    int generalValidationTimesInvoked,
                                    int alreadyExistsValidationTimesInvoked,
                                    int successTimesInvoked) {

        model.finish(activityName, resourceName, listener);
        verify(view, times(generalValidationTimesInvoked))
                .showGeneralValidationDialog();
        verify(view, times(alreadyExistsValidationTimesInvoked))
                .showAlreadyExistsValidationDialog();
        verify(view, times(successTimesInvoked))
                .returnToAddMood();
    }
}
