package com.example.forecast_mvvm.dataLayer.entities.models


import com.google.gson.annotations.SerializedName

data class Daily(
    val clouds: Int,
    val dt: Long,

    val humidity: Int,

    val pressure: Int,

    val temp: Temp,

    val weather: List<Weather>,

    @SerializedName("wind_speed")
    val windSpeed: Double
)