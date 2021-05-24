package com.ndipatri.iot.zeroto98

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import okhttp3.OkHttpClient
import okhttp3.mock.Behavior
import okhttp3.mock.MockInterceptor
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainScreenEspressoTest : TestCase() {

    // This can be used to inject 'fake' OkHttpClient responses for testing
    // purposes.  In this way, the system can make real network calls but will result
    // in mocked network responses.
    private val mockInterceptor = MockInterceptor(Behavior.UNORDERED)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(mockInterceptor)
        .build()

    @get:Rule
    val componentRule = ApplicationComponentTestRule(okHttpClient)

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