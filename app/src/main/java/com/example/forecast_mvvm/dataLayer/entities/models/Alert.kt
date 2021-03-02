package com.example.forecast_mvvm.dataLayer.entities.models

data class Alert (
    val event: String,
    val start: Long,
    val end: Long,
    val description: String,
)