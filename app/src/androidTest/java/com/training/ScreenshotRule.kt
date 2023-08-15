package com.training

import android.content.Context
import android.util.Log
import android.view.View
import androidx.test.espresso.Espresso.setFailureHandler
import androidx.test.espresso.FailureHandler
import androidx.test.espresso.base.DefaultFailureHandler
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.hamcrest.Matcher
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.File
import java.util.Locale

fun String.slug() : String {
    // Replace special characters with hyphens and convert to lowercase
    val normalized = this
        .replace("[^a-zA-Z0-9\\s-]".toRegex(), "")
        .replace("\\s+".toRegex(), " ")
        .trim()
        .capitalize(Locale.getDefault())
        .replace(" ", "-")

    return normalized
}

class ScreenshotRule : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        setFailureHandler(
            CustomFailureHandler(getInstrumentation().context, description)
        )
    }
}

class CustomFailureHandler(targetContext: Context, description: Description) : FailureHandler {

    private val delegate: FailureHandler
    private val device = UiDevice.getInstance(getInstrumentation())
    private val directory = "/storage/emulated/0/Android/data/${targetContext.packageName}/test_failures"
    private val testClassName = description.className
    private val testMethodName = description.methodName

    init {
        delegate = DefaultFailureHandler(targetContext)
    }

    override fun handle(error: Throwable, viewMatcher: Matcher<View>) {

        val input = viewMatcher.toString().split("\n\n")[0]
        var matchText: String? = null

        // androidx.test.espresso.NoMatchingViewException: No views in hierarchy found matching: an instance of android.widget.TextView and view.getText() with or without transformation to match: is "Nehow Ma"
        //androidx.test.espresso.NoMatchingViewException: No views in hierarchy found matching: an instance of android.widget.TextView and view.getText() with or without transformation to match: is "fail"

        if("view.getId() is <" in input) {
            //val regex = """an instance of ([^\s]+) .* with or without transformation to match: is "(.*)"""".toRegex()
            //val matchResult = regex.find(input)
            //val viewInfo = matchResult?.groupValues?.get(1)
            matchText = input.slug()//matchResult?.groupValues?.get(2)?.slug() // Nehow-Ma
        }

        // Chris: androidx.test.espresso.NoMatchingViewException: No views in hierarchy found matching: view.getId() is <15 (resource name not found)>

        // Tom: androidx.test.espresso.PerformException: Error performing 'androidx.test.espresso.contrib.RecyclerViewActions$ActionOnItemAtPositionViewAction@19647cd' on view 'Animations or transitions are enabled on the target device.
        // Caused by: java.lang.IllegalStateException: No view holder at position: 11

        // John: junit.framework.AssertionFailedError: Wanted to match 1 intents. Actually matched 0 intents.

        Log.d(javaClass.name, "viewMatcher: $viewMatcher")
        Log.d(javaClass.name, "error.message: ${error.message}" ?: "No message")



        val errorSlug = error.javaClass.name.slug()

        //Generates image in data/data instead of /emulator directory
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val filesDir = context.filesDir
        val snapshot = File(filesDir, "$errorSlug(${matchText ?: "Default"}).png")
        //

        //val snapshot = File("$directory/$testClassName", "$testMethodName-$errorSlug(${matchText ?: "Default"}).png")
        snapshot.parentFile!!.mkdirs()
        device.takeScreenshot(snapshot)

        delegate.handle(error, viewMatcher)

    }
}
