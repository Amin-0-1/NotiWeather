package com.example.forecast_mvvm.dataLayer.local

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.forecast_mvvm.dataLayer.entities.FavouriteCoordination
import com.example.forecast_mvvm.dataLayer.local.Daos.FavouriteCoordDao
import com.example.forecast_mvvm.dataLayer.local.Daos.WeatherDao
import com.example.forecast_mvvm.dataLayer.remote.WeatherResponse

class LocalDataSource(application:Application) {

    private var weatherDao: WeatherDao = WeatherForecastDatabase.getInstanse(application).weatherDao()
    private var favCoordDao: FavouriteCoordDao = WeatherForecastDatabase.getInstanse(application).favCoordDao()

    // weather funcions
    fun insertWeatherData(body: WeatherResponse) {
        weatherDao.insert(body)
    }

    fun getWeatherData(): LiveData<WeatherResponse> {
        return weatherDao.getWeatherData()
    }


    // favourite functions
    fun insertFavouriteCoord(latitude: Double, longitude: Double) {
        favCoordDao.insertFavCoord(FavouriteCoordination(latitude,longitude))
    }

}