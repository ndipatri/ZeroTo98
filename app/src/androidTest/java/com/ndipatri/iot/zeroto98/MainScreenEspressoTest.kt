package com.ndipatri.iot.zeroto98

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainScreenEspressoTest : TestCase() {

    @get:Rule
    val activityTestRule = ActivityTestRule(
        MainActivity::class.java,
        false,
        false
    )

    @Test
    fun showCurrentRedSirenState_off() {

        activityTestRule.launchActivity(Intent())

        run {
            MainScreen {
                // If the siren is actually on, this test will fail.
                // Kapsresso will catch this exception and try again.. each time
                // toggling the state of the siren (button.click()), until
                // the siren is in the correct state to make this step pass.
                step("Red siren is off and ready to turn on") {
                    flakySafely(timeoutMs = 60000) {
                        button.click()

                        Thread.sleep(5000)

                        textView {
                            hasText("RedSiren is off!")
                        }
                    }
                }
            }
        }

    }
}