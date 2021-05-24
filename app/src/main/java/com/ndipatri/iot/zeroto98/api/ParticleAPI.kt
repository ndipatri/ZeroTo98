package com.ndipatri.iot.zeroto98.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class ParticleAPI(okHttpClient: OkHttpClient) {

    val particleInterface: ParticleRESTInterface by lazy {

        Retrofit.Builder().apply {
            baseUrl("https://api.particle.io/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
        }
            .build()
            .create(ParticleRESTInterface::class.java)
    }

    interface ParticleRESTInterface {
        @FormUrlEncoded
        @POST("/v1/devices/{deviceId}/sirenOn")
        suspend fun turnOnRedSiren(
            @Path("deviceId") deviceId: String = "e00fce68c9dc54448dfe8f89",
            @Field("arg") updateState: String = "",
            @Field("access_token") accessToken: String = particleToken
        )

        @FormUrlEncoded
        @POST("/v1/devices/{deviceId}/sirenOff")
        suspend fun turnOffRedSiren(
            @Path("deviceId") deviceId: String = "e00fce68c9dc54448dfe8f89",
            @Field("arg") updateState: String = "",
            @Field("access_token") accessToken: String = particleToken
        )

        @GET("/v1/devices/{deviceId}/sirenState")
        suspend fun getSirenState(
            @Path("deviceId") deviceId: String = "e00fce68c9dc54448dfe8f89",
            @Query("access_token") accessToken: String = particleToken
        ): SirenStateResponse

        class SirenStateResponse {
            @SerializedName("result")
            @Expose
            var result: String? = null
        }
    }

    companion object {
        const val particleToken = "{particle access token goes here}"
    }
}