package com.example.forecast_mvvm.dataLayer.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    fun createRetrofit(): OpenWeatherMapAPI {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherMapAPI::class.java)
        }

}