package com.example.forecast_mvvm.dataLayer.local

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteCoordination
import com.example.forecast_mvvm.dataLayer.local.Daos.FavouriteCoordDao
import com.example.forecast_mvvm.dataLayer.local.Daos.WeatherDao
import com.example.forecast_mvvm.dataLayer.local.response.Alarm
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteWeatherResponse
import com.example.forecast_mvvm.dataLayer.remote.response.WeatherResponse

class LocalDataSource(application:Application) {

    private var weatherDao: WeatherDao = WeatherForecastDatabase.getInstanse(application).weatherDao()
    private var favCoordDao: FavouriteCoordDao = WeatherForecastDatabase.getInstanse(application).favCoordDao()
    private val favouriteWeatherDao = WeatherForecastDatabase.getInstanse(application).favouriteWeatherDao()
    private val alarmDao = WeatherForecastDatabase.getInstanse(application).alarmDao()

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
        return favouriteWeatherDao.getFavWeatherDataResponse(lat,lon)
    }

    fun getLocalFavouriteWeather(lat: Double, lon: Double): LiveData<FavouriteWeatherResponse> {
        return favouriteWeatherDao.getFavWeatherData(lat,lon)
    }

    fun getCurrentLon(): String? {
        return weatherDao.getWeatherData().lon.toString()
    }

    fun getCurrentLat(): String? {
        return weatherDao.getWeatherData().lat.toString()
    }

    fun insertAlarm(
        currentLat: String,
        currentLon: String,
        type: String,
        date: String,
        time: String,): Long {
        return alarmDao.insertAlarm(Alarm(currentLat.toDouble(),currentLon.toDouble(),type,date,time))
    }

    fun getAlarmLat(id: Long): Double {
        return alarmDao.getAlarmLat(id)
    }

    fun getAlarmLon(id: Long): Double {
        return alarmDao.getAlarmLon(id)
    }

    fun getAlarmType(id: Long): String? {
        return alarmDao.getAlarmType(id)
    }

    fun deleteAlarm(id: Long) {
        alarmDao.deleteAlarm(id)
    }

    fun getAllAlarms(): LiveData<List<Alarm>> {
        return alarmDao.getAllAlarms()
    }

    fun updateLocality(cityName: String) {
        return weatherDao.updateLocality(cityName)
    }

}