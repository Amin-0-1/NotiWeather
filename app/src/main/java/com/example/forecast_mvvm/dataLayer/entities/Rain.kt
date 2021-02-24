package com.example.forecast_mvvm.dataLayer.entities


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val h: Double
)