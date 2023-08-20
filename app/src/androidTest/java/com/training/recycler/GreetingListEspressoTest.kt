package com.training.recycler

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.training.recycler.presentation.CardViewHolder
import com.training.recycler.presentation.MainActivity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit


class GreetingListEspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val screenshotRule = ScreenshotRule()


    @Test
    fun testRecyclersInitialized() {

        TimeUnit.SECONDS.sleep(1)

        var isViewVisible = false
            ActivityScenario.launch(MainActivity::class.java).onActivity { activity ->
                val view = activity.findViewById<View>(R.id.recyclerViewLeft)
                isViewVisible = view.visibility == View.VISIBLE
            }

        //Checks if the Recycler view by the correct id is visible and has thus been correctly initialized.
        assertEquals(true, isViewVisible)
    }

    @Test
    fun testAddNewCard() {

        val expectedCardName = "Test Card"

        TimeUnit.SECONDS.sleep(1)

        onView(withId(R.id.addFAB)).perform(click())

        TimeUnit.SECONDS.sleep(1)

        onView(withId(com.training.recycler.R.id.nameEditText)).perform(typeText(expectedCardName))

        TimeUnit.SECONDS.sleep(1)

        onView(withText("Add Card")).perform(click())

        TimeUnit.SECONDS.sleep(1)

        //Checks if the configured expectedCardName matches the card name that is on screen.
        onView(withText(expectedCardName)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        TimeUnit.SECONDS.sleep(1)

    }

    //@Test
    fun testTakeScreenShot() {

        onView(withId(R.id.recyclerViewRight))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CardViewHolder>(11, click()))

    }
}