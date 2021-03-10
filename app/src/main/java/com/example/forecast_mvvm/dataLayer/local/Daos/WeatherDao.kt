package com.example.forecast_mvvm.dataLayer.local.Daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.forecast_mvvm.dataLayer.remote.response.WeatherResponse

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather")
    fun getWeatherData(): WeatherResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherState: WeatherResponse)

    @Query("UPDATE weather SET locality=:cityName")
    fun updateLocality(cityName: String)

//    @Query("SELECT lat FROM weather")
//    fun getCurrentLat(): String?
//
//    @Query("SELECT lon FROM weather")
//    fun getCurrentLon(): String?
}
