package com.example.daniwebuiautomatortest

import android.view.KeyEvent
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.Switch
import android.widget.TextView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.*

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    /**
     * Check https://developer.android.com/training/testing/junit-rules#activity-test-rule
     */
    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testDarkModeSwitch(){
        //Obtains the instrumented device
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        device.pressHome() //Press the Home button

        val homeScreen = device.findObject(UiSelector() //Starts the findObject query
            .resourceId("android:id/content") //Tries to match the element resource id
            .className(FrameLayout::class.java) //Tries to match the element class name
            .packageName("com.google.android.apps.nexuslauncher")) //Tries to match the package name. UiDevice.getPackageName might be cleaner

        homeScreen.waitForExists(1_000) //this is one option to wait for the View to load

        homeScreen.swipeUp(10) //Swipes up to open the Launcher

        val settingsIcon = device.findObject(UiSelector()
            .resourceId("com.google.android.apps.nexuslauncher:id/icon")
            .className(TextView::class.java)
            .packageName("com.google.android.apps.nexuslauncher")
            .descriptionContains("Settings")
        )

        settingsIcon.waitForExists(1_000)

        settingsIcon.click()

        //UiScrollable provides better API to interact with the Settings RecyclerView
        val settingsView = UiScrollable(UiSelector()
            .resourceId("com.android.settings:id/main_content_scrollable_container")
            .className(ScrollView::class.java)
            .packageName("com.android.settings")
        )

        settingsView.waitForExists(1_000)

        val displayOption = device.findObject(UiSelector()
            .text("Display")
            .resourceId("android:id/title")
            .className(TextView::class.java)
            .packageName("com.android.settings")
        )

        settingsView.scrollIntoView(displayOption)

        displayOption.waitForExists(1_000)

        displayOption.click()

        val darkThemeSwitch = device.findObject(UiSelector()
            .resourceId("com.android.settings:id/switchWidget")
            .className(Switch::class.java)
            .packageName("com.android.settings")
            .descriptionContains("Dark theme")
        )

        darkThemeSwitch.waitForExists(1_000)

        darkThemeSwitch.click()

        device.pressRecentApps() //Recents is black box because uiautomatorviewer is unable to spy the Recents View

        device.pressKeyCode(KeyEvent.KEYCODE_APP_SWITCH)

        val app = device.findObject(UiSelector()
            .packageName("com.example.daniwebuiautomatortest")
        )

        app.waitForExists(1_000)

        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val isDark = context
            .resources
            .configuration.
            isNightModeActive

        assert(isDark)
    }

}