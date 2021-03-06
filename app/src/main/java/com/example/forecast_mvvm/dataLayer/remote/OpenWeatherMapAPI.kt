package com.example.forecast_mvvm.dataLayer.remote

import com.example.forecast_mvvm.dataLayer.local.response.FavouriteWeatherResponse
import com.example.forecast_mvvm.dataLayer.remote.response.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapAPI {

    companion object{
        private const val API_KEY = "44e7795e552294d8d04c16e53577aab7"
    }

    @GET("onecall?appid=$API_KEY")

    suspend fun getCurrentWeatherData(
        @Query("lat") latitude:String,
        @Query("lon") longitude:String,
        @Query("units") unit:String="metric",
        @Query("lang") lang:String="en"

        ): Response<WeatherResponse>

    @GET("onecall?appid=$API_KEY")

    suspend fun getFavouriteWeatherData(
        @Query("lat") latitude:String,
        @Query("lon") longitude:String,
        @Query("units") unit:String="metric",
        @Query("lang") lang:String="en"
    ): Response<FavouriteWeatherResponse>
}