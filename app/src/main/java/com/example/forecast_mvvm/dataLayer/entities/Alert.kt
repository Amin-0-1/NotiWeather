package com.example.forecast_mvvm.dataLayer.entities

data class Alert (
    val event: String,
    val start: Long,
    val end: Long,
    val description: String,
)