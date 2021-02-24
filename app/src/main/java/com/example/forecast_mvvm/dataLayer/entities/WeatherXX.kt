package com.example.forecast_mvvm.dataLayer.entities


import com.google.gson.annotations.SerializedName

data class WeatherXX(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)