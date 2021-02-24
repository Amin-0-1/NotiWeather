package com.example.forecast_mvvm.dataLayer.entities


import com.google.gson.annotations.SerializedName

data class FeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
)