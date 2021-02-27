package com.example.forecast_mvvm.dataLayer.entities


import com.google.gson.annotations.SerializedName

data class WeatherState(
    val clouds: Int, // exist

    val dt: Long, // current time

    val humidity: Int, // exist
    val pressure: Int, // exist

    val temp: Double, // current temp

    val weather: List<Weather>,

    @SerializedName("wind_speed")
    val windSpeed: Double // exist
)