package com.moodlogger.activities.models.impl;


import android.content.Context;

import com.moodlogger.activities.presenters.impl.CustomisePresenterImpl;
import com.moodlogger.activities.views.intf.CustomiseView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CustomiseModelTest {

    @Mock
    Context context;
    @Mock
    CustomiseView view;

    private CustomisePresenterImpl listener;
    private CustomiseModelImpl model;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        listener = new CustomisePresenterImpl(view, context);
        model = spy(new CustomiseModelImpl(context));
    }

    @Test
    public void itDoesNothingWhenNameIsUnchanged() {
        testNameChange("Old Name", "Old Name", 0, 0);
    }

    @Test
    public void itTriggersValidationWhenNewNameIncorrect() {
        testNameChange("Old Name", "s+24$", 1, 0);
    }

    @Test
    public void itSavesNewNameWhenNameIsDifferent() {
        doNothing().when(model).saveNameChange(anyString());
        testNameChange("Old Name", "New Name", 0, 1);
    }

    private void testNameChange(String oldName, String newName,
                                int validationDialogTimes, int changesSavedTimes) {
        model.validateAndSaveName(oldName, newName, listener);
        verify(view, times(validationDialogTimes)).showValidationDialog();
        verify(view, times(changesSavedTimes)).showChangesSaved();
    }

    @Test
    public void itTriggersThemeChangeWhenThemeSaved() {
        doNothing().when(model).saveThemeChange(anyInt());
        model.setNewTheme(1, listener);
        verify(view, times(1)).changeTheme();
    }
}
