package com.moodlogger.activities.models.impl;


import android.content.Context;

import com.moodlogger.activities.presenters.impl.SettingsPresenterImpl;
import com.moodlogger.activities.views.intf.SettingsView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SettingsModelTest {

    @Mock
    Context context;
    @Mock
    SettingsView view;

    private SettingsPresenterImpl listener;
    private SettingsModelImpl model;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        listener = new SettingsPresenterImpl(view, context);
        model = spy(new SettingsModelImpl(context));
    }

    @Test
    public void itFailsWithUnsuccessfulMoodExport() {
        doReturn(false).when(model).saveTimestampedExportFile(anyString());
        model.exportMoodEntries("path", listener);
        verify(view, times(1)).onFailedExport();
        verify(view, times(0)).onSuccessfulExport();
    }

    @Test
    public void itSucceedsWhenSuccessfulMoodExport() {
        doReturn(true).when(model).saveTimestampedExportFile(anyString());
        model.exportMoodEntries("path", listener);
        verify(view, times(0)).onFailedExport();
        verify(view, times(1)).onSuccessfulExport();
    }
}
