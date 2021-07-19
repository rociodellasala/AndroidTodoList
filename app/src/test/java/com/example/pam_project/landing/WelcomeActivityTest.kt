package com.example.pam_project.landing

import android.view.View
import com.example.pam_project.R
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController

@RunWith(RobolectricTestRunner::class)
class WelcomeActivityTest {
    private lateinit var activity: ActivityController<WelcomeActivity>
    @Before
    fun setup() {
        activity = Robolectric.buildActivity(WelcomeActivity::class.java)
    }

    @Test
    fun givenWelcomeWasConfirmedThenVerifyTheActivityIsFinished() {
        activity.create().resume()
        activity.get().findViewById<View>(R.id.welcome_button).performClick()
        Assert.assertTrue(activity.get().isFinishing)
    }
}