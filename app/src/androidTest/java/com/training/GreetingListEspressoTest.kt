package com.training

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.Matcher
//import com.training.GreetingAdapter.ViewHolder
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

        onView(withId(com.training.R.id.nameEditText)).perform(typeText(expectedCardName))

        TimeUnit.SECONDS.sleep(1)

        onView(withText("Add Card")).perform(click())

        TimeUnit.SECONDS.sleep(1)

        //Checks if the configured expectedCardName matches the card name that is on screen.
        onView(withText(expectedCardName)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testTakeScreenShot() {

        //val viewHolderForIndex11 = recyclerViewRight.findViewHolderForAdapterPosition(11) as? CardViewHolder

        onView(withId(R.id.recyclerViewRight))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CardViewHolder>(11, click()))




    }






//    private fun validateDialog(message: String, language: String) {
//
//        TimeUnit.SECONDS.sleep(1)
//        onView(withText(message)).check(matches(isDisplayed()))
//        onView(withText("The language is $language")).check(matches(isDisplayed()))
//
//        // Perform the action on the dialog (e.g., click a button)
//        onView(withId(android.R.id.button1)).perform(click())
//        TimeUnit.SECONDS.sleep(1)
//
//    }

//    fun enterGreeting(message: String, language: String) {
//
//        TimeUnit.SECONDS.sleep(1)
//
//        // Click on the FloatingActionButton to open the dialog
//        onView(withId(R.id.fab)).perform(click())
//
//        TimeUnit.SECONDS.sleep(2)

        // Create the test ActivityResultRegistry
//        val testRegistry = object : ActivityResultRegistry() {
//            override fun <I, O> onLaunch(
//                requestCode: Int,
//                contract: ActivityResultContract<I, O>,
//                input: I,
//                options: ActivityOptionsCompat?
//            ) {
//
////                dispatchResult(requestCode, expectedResult)
//                Log.d(javaClass.name, "viewMatcher: $requestCode")
//            }
//        }

//        Intents.init()
//        val expectedIntent: Matcher<Intent> = allOf(hasAction(Intent.ACTION_VIEW))
//        intending(expectedIntent).respondWith(Instrumentation.ActivityResult(0, null))
//        intended(expectedIntent)
//        Intents.release()

//        // Check if the dialog is displayed
//        onView(withId(Greeter.et1ID)).check(matches(isDisplayed()))
//
//        // Enter text into the EditTexts
//        onView(withId(Greeter.et1ID)).perform(typeText(message))
//        onView(withId(Greeter.et2ID)).perform(typeText(language))
//
//        // Close the keyboard
//        closeSoftKeyboard()
//
//        TimeUnit.SECONDS.sleep(1)
//
//        // Perform the action on the dialog (e.g., click a button)
//        onView(withId(android.R.id.button1)).perform(click())
//
//        TimeUnit.SECONDS.sleep(1)
//    }



//    @Test
//    fun testGreetingItemClickShowsDialog() {
//
//        TimeUnit.SECONDS.sleep(2)
//
//        // Perform click on the first item in RecyclerView
//        onView(withId(R.id.recyclerView))
//            .perform(actionOnItemAtPosition<ViewHolder>(0, click()))
//
//        validateDialog("Konichiwa", "Japanese")
//
//        // Perform click on the second item in RecyclerView
//        onView(withId(R.id.recyclerView))
//            .perform(actionOnItemAtPosition<ViewHolder>(1, click()))
//
//        // Check if the dialog is displayed again
//        validateDialog("Aloha", "Hawaiian")
//
//        enterGreeting("Good Day", "Australian")
//        enterGreeting("What Go'on?!", "Jamaican")
//        enterGreeting("Nehow Ma", "Chinese")
//
//        // Perform click on the last item in RecyclerView
//        onView(withId(R.id.recyclerView))
//            .perform(actionOnItemAtPosition<ViewHolder>(8, click()))
//
//        // Check if the dialog is displayed again
//        validateDialog("Nehow Ma", "Chinese")
//
//    }
}