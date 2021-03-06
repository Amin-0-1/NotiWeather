package com.example.forecast_mvvm.dataLayer.local.response

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.forecast_mvvm.dataLayer.entities.models.Alert
import com.example.forecast_mvvm.dataLayer.entities.models.Daily
import com.example.forecast_mvvm.dataLayer.entities.models.WeatherState
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favouriteWeather", primaryKeys = ["lat", "lon"])

class FavouriteWeatherResponse (

    @SerializedName("current")
    @Embedded(prefix = "condition_")
    val weatherState: WeatherState,

    val daily: List<Daily>,

    @SerializedName("hourly")
    val hourly: List<WeatherState>,

    val alerts: List<Alert>? = listOf(),

    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int
)