package com.example.forecast_mvvm.dataLayer.entities

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson

class Converter {
    @TypeConverter
    fun  hourlyToJson (value:List<WeatherState>) = Gson().toJson(value)

    @TypeConverter
    fun  dailyToJson (value:List<Daily>) = Gson().toJson(value)

    @TypeConverter
    fun  alertToJson (value:List<Alert>?) = Gson().toJson(value)

    @TypeConverter
    fun  weatherToJson (value: List<Weather>) = Gson().toJson(value)


    @TypeConverter
    fun jsonToWeatherList(value: String) = Gson().fromJson(value, Array<Weather>::class.java).toList()
    @TypeConverter
    fun jsonToHourlyList(value: String) = Gson().fromJson(value, Array<WeatherState>::class.java).toList()
    @TypeConverter
    fun jsonToDailyList(value: String) = Gson().fromJson(value, Array<Daily>::class.java).toList()
    @TypeConverter

    fun jsonToAlertList(value: String?): List<Alert> {

        Log.i("TAG", "jsonToAlertList: ++++++++++++++++++++++++++++++++++++++++++"+value.toString())

        return if(value.toString() != "null")
            Gson().fromJson(value, Array<Alert>::class.java).toList()
        else
            return emptyList()

    }
}