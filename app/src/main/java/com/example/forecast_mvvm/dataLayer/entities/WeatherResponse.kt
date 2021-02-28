package com.example.forecast_mvvm.dataLayer.entities


import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather")
data class WeatherResponse(

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
){
    @PrimaryKey(autoGenerate = false)
    var id :Int = 0
}