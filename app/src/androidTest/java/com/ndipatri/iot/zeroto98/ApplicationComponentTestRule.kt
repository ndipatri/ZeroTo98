package com.ndipatri.iot.zeroto98

import it.cosenonjaviste.daggermock.DaggerMockRule
import okhttp3.OkHttpClient

class ApplicationComponentTestRule(okHttpClient: OkHttpClient)
    : DaggerMockRule<ApplicationComponent>(

    // This is the component whose dependencies we are mocking
    ApplicationComponent::class.java,

    // All Component Modules listed here can have their dependencies overridden
    ApiModule()
) {

    init {
        // This is how we override dependencies that will NOT be mocks
        provides(OkHttpClient::class.java, okHttpClient)

        set { daggerMockComponent ->

            // This is called immediately after DaggerMock creates our Component.

            // DaggerMock replaces dependencies with mocks, but it does not know where
            // to put this modified Component. Here we do the swap manually.
            ApplicationComponent.component = daggerMockComponent
        }
    }
}
