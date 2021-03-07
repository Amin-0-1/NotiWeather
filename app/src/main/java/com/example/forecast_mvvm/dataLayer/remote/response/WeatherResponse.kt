package com.example.forecast_mvvm.dataLayer.remote.response


import androidx.room.*
import com.example.forecast_mvvm.dataLayer.entities.models.Alert
import com.example.forecast_mvvm.dataLayer.entities.models.Daily
import com.example.forecast_mvvm.dataLayer.entities.models.WeatherState
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

    var lat: Double,
    var lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int
){
    @PrimaryKey(autoGenerate = false)
    var id :Int = 0
}