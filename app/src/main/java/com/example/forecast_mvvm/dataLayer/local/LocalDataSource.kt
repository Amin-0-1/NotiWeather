package com.example.forecast_mvvm.dataLayer.local

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteCoordination
import com.example.forecast_mvvm.dataLayer.local.Daos.FavouriteCoordDao
import com.example.forecast_mvvm.dataLayer.local.Daos.WeatherDao
import com.example.forecast_mvvm.dataLayer.remote.response.WeatherResponse

class LocalDataSource(application:Application) {

    private var weatherDao: WeatherDao = WeatherForecastDatabase.getInstanse(application).weatherDao()
    private var favCoordDao: FavouriteCoordDao = WeatherForecastDatabase.getInstanse(application).favCoordDao()

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
    }

    fun getAllFavouriteCoord(): List<FavouriteCoordination> {
        return favCoordDao.getAllFavouriteCoord()
    }
    fun getNullFavouriteLocations():List<FavouriteCoordination>{
        return favCoordDao.getNullFavouriteLocations()
    }

    fun getNotNullFavourite(): List<FavouriteCoordination> {
        return favCoordDao.getNotNullFavourite()

    }

    fun deleteFavourite(lat: Double, lon: Double) {
        favCoordDao.deleteFavourite(lat,lon)
    }

}