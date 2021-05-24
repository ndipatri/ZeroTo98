package com.ndipatri.iot.zeroto98

import com.ndipatri.iot.zeroto98.api.ParticleAPI
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class])
interface ApplicationComponent {
    fun inject(firstFragment: FirstFragment?)

    @Component.Builder
    interface Builder {
        fun apiModule(apiModule: ApiModule): Builder
        fun build(): ApplicationComponent
    }

    companion object {
        lateinit var component: ApplicationComponent

        fun createIfNecessary(): ApplicationComponent {
            if (!::component.isInitialized) {

                component = DaggerApplicationComponent.builder()
                    .apiModule(ApiModule())
                    .build()
            }

            return component
        }
    }
}

@Module
class ApiModule() {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideParticleAPI(okHttpClient: OkHttpClient): ParticleAPI {
        return ParticleAPI(okHttpClient)
    }
}
