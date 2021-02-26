package com.example.forecast_mvvm.dataLayer.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecast_mvvm.dataLayer.entities.WeatherResponse

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather")
    fun getWeatherData(): LiveData<WeatherResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherState: WeatherResponse)
}
