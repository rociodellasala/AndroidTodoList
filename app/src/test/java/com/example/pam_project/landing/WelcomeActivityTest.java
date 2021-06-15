package com.example.pam_project.landing;

import com.example.pam_project.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class WelcomeActivityTest {

    private ActivityController<WelcomeActivity> activity;

    @Before
    public void setup() {
        activity = Robolectric.buildActivity(WelcomeActivity.class);
    }

    @Test
    public void givenWelcomeWasConfirmedThenVerifyTheActivityIsFinished() {
        activity.create().resume();

        activity.get().findViewById(R.id.welcome_button).performClick();

        assertTrue(activity.get().isFinishing());
    }
}
