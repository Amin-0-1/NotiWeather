package com.example.forecast_mvvm.dataLayer.local.Daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteWeatherResponse

@Dao
interface FavouriteWeatherDao {
    @Query("SELECT * FROM favouriteWeather where lat is :lat and lon is :lon")
    fun getFavWeatherData(lat: Double, lon: Double): LiveData<FavouriteWeatherResponse>

    @Query("SELECT * FROM favouriteWeather where lat is :lat and lon is :lon")
    fun getFavWeatherDataResponse(lat: Double, lon: Double): FavouriteWeatherResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherState: FavouriteWeatherResponse)

    @Query("delete from favouriteWeather where lat is :lat and lon is :lon ")
    fun deleteFavourite(lat: Double, lon: Double)

}