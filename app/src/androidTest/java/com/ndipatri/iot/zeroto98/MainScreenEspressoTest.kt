package com.ndipatri.iot.zeroto98

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.google.gson.Gson
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.ndipatri.iot.zeroto98.api.ParticleAPI
import okhttp3.OkHttpClient
import okhttp3.mock.*

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import java.net.HttpURLConnection

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

        val response = ParticleAPI.ParticleRESTInterface.SirenStateResponse().apply {
            result = "off"
        }
        mockInterceptor.rule(
            get,
            path eq "/v1/devices/e00fce68c9dc54448dfe8f89/sirenState",
            times = anyTimes
        ) {
            respond(HttpURLConnection.HTTP_OK) {
                body(Gson().toJson(response), MediaTypes.MEDIATYPE_JSON)
            }
        }

        mockInterceptor.rule(
            post,
            path eq "/v1/devices/e00fce68c9dc54448dfe8f89/sirenOn",
            times = anyTimes
        ) {
            respond(HttpURLConnection.HTTP_OK) {
                body(Gson().toJson(response), MediaTypes.MEDIATYPE_JSON)
            }
        }

        activityTestRule.launchActivity(Intent())

        run {
            MainScreen {
                // Since we are now mocking our network response, we are guaranteed to
                // have the siren in the off state... No need to click the button and
                // retry until we're in the correct state, as we did previously.
                step("Red siren is off and ready to turn on") {
                    textView {
                        hasText("RedSiren is off!")
                    }
                }
            }
        }
    }
}