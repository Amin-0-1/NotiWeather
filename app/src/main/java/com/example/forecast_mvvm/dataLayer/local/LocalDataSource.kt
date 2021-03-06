package com.example.forecast_mvvm.dataLayer.local

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteCoordination
import com.example.forecast_mvvm.dataLayer.local.Daos.FavouriteCoordDao
import com.example.forecast_mvvm.dataLayer.local.Daos.WeatherDao
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteWeatherResponse
import com.example.forecast_mvvm.dataLayer.remote.response.WeatherResponse

class LocalDataSource(application:Application) {

    private var weatherDao: WeatherDao = WeatherForecastDatabase.getInstanse(application).weatherDao()
    private var favCoordDao: FavouriteCoordDao = WeatherForecastDatabase.getInstanse(application).favCoordDao()
    private val favouriteWeatherDao = WeatherForecastDatabase.getInstanse(application).favouriteWeatherDao()

    // weather funcions
    fun insertWeatherData(body: WeatherResponse) {
        weatherDao.insert(body)
    }

    fun getWeatherData(): WeatherResponse {
        return weatherDao.getWeatherData()
    }


    // favourite functions
    fun insertFavouriteCoord(latitude: Double, longitude: Double, title: String?) {
        favCoordDao.insertFavCoord(FavouriteCoordination(latitude,longitude,title))
        Log.i("test", "insertFavouriteCoord: 1")
    }

    fun getAllFavouriteCoord(): List<FavouriteCoordination> {
        return favCoordDao.getAllFavouriteCoord()
    }
    fun getNullFavouriteLocations():List<FavouriteCoordination>{
        return favCoordDao.getNullFavouriteLocations()
    }

    fun getNotNullFavourite(): LiveData<List<FavouriteCoordination>> {
        return favCoordDao.getNotNullFavourite()

    }

    fun deleteFavourite(lat: Double, lon: Double) {
        favCoordDao.deleteFavourite(lat,lon)
    }

    fun insertfavouriteWeatherData(body: FavouriteWeatherResponse) {
        favouriteWeatherDao.insert(body)
    }

    fun deleteFavouriteWeather(lat: Double, lon: Double) {
        favouriteWeatherDao.deleteFavourite(lat,lon)
    }

    fun getFavouriteWeatherData(lat: Double, lon: Double): FavouriteWeatherResponse {
        return favouriteWeatherDao.getFavWeatherData(lat,lon)
    }

    fun getLocalFavouriteWeather(lat: Double, lon: Double): FavouriteWeatherResponse {
        return favouriteWeatherDao.getFavWeatherData(lat,lon)
    }

}