package com.example.forecast_mvvm.dataLayer.entities


import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather")
data class WeatherResponse(

    @SerializedName("current")
    @Embedded(prefix = "condition_")
    val weatherState: WeatherState,

//    @TypeConverters(Converter::class)
    val daily: List<Daily>,

//    @TypeConverters(Converter::class)
    val hourly: List<WeatherState>,

//    @TypeConverters(Converter::class)
    val alerts: List<Alert>? = listOf(),

    val lat: Double,
    val lon: Double,
    val timezone: String,
//    @SerializedName("timezone_offset")
//    val timezoneOffset: Int
){
    @PrimaryKey(autoGenerate = false)
    var id :Int = 0
}