package com.linhphan.lpcore.ui.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.linhphan.lpcore.R
import com.linhphan.lpcore.ui.twosidepannels.TwoSideScreenActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testActivityLaunch() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.btn_two_side_screen)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_two_side_screen)).check(matches(withText("Open Two Side Screen")))
    }

    @Test
    fun testNavigateToTwoSideScreen() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.btn_two_side_screen)).perform(click())

        intended(hasComponent(TwoSideScreenActivity::class.java.name))
    }
}