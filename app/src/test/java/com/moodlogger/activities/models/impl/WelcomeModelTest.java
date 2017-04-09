package com.moodlogger.activities.models.impl;

import android.content.Context;

import com.moodlogger.activities.presenters.impl.WelcomePresenterImpl;
import com.moodlogger.activities.views.intf.WelcomeView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class WelcomeModelTest {

    @Mock
    Context context;
    @Mock
    WelcomeView view;

    private WelcomePresenterImpl listener;
    private WelcomeModelImpl model;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        listener = new WelcomePresenterImpl(view, context);
        model = Mockito.spy(new WelcomeModelImpl(context));
        doNothing().when(model).saveNameAndMarkWelcomeGiven(anyString());
    }

    @Test
    public void itTriggersValidationForEmptyName() {
        testFinish("", false);
    }

    @Test
    public void itTriggersValidationForTooLongName() {
        testFinish("supercalifragilisticexpialidocious", false);
    }

    @Test
    public void itTriggersValidationForTooInvalidCharacters() {
        testFinish("Abc123", false);
    }

    @Test
    public void itSucceedsForValidName() {
        testFinish("Jack", true);
    }

    @Test
    public void itSucceedsForValidNameWithSpaces() {
        testFinish("The Boss", true);
    }

    private void testFinish(String name, boolean successExpected) {
        model.finish(name, listener);
        if (successExpected) {
            verify(view, times(0)).showValidationDialog();
            verify(view, times(1)).navigateToMain();
        } else {
            verify(view, times(1)).showValidationDialog();
            verify(view, times(0)).navigateToMain();
        }
    }
}
