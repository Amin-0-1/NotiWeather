package com.example.forecast_mvvm.dataLayer
import android.app.Application
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.forecast_mvvm.dataLayer.remote.response.WeatherResponse
import com.example.forecast_mvvm.dataLayer.local.LocalDataSource
import com.example.forecast_mvvm.dataLayer.local.response.Alarm
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteCoordination
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteWeatherResponse
import com.example.forecast_mvvm.dataLayer.remote.RemoteDataSource
import com.example.forecast_mvvm.utilities.SettingsSP
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.util.*

class Repository(private val application: Application) {

    private val remoteDataSource:RemoteDataSource = RemoteDataSource()
    private val localDataSource:LocalDataSource = LocalDataSource(application)


    fun getCityName(lat: Double, lon: Double): String {
        val geocoder = Geocoder(application.applicationContext, Locale.getDefault())
        Log.i("dddd", "getCityName: inside getcityname")
        var res = "null"
        if (lat != 0.0 && lon != 0.0) {

            try {
                Log.i("TAG", "latolon: lat: $lat , lon: $lon")
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                res = addresses[0].subAdminArea

                return res;
            } catch (e: IOException) {
                Log.i("TAG", "getCityName: catch")
                e.printStackTrace()
                return res
            }
            return res
        }else{
            Log.i("TAG", "getCityName: nullllllllllllllllllll")
            return res
        }
    }

    suspend fun getWeatherData(lat: String="", lon: String=""): WeatherResponse {

        SettingsSP.loadSettings(application.applicationContext)
        // get current
        if(lat != "" && lon != ""){
            val data = remoteDataSource.getCurrentWeatherData(lat,lon) // api call
            Log.i("TAG", "getWeatherData: inside api")
//            Toast.makeText(application.applicationContext,"inside function", Toast.LENGTH_LONG).show()

            if(data.isSuccessful){
//                data.body()?.lat = lat.toDouble()
//                data.body()?.lon = lon.toDouble()
                data.body()?.locality = getCityName(lat.toDouble(),lon.toDouble())
                localDataSource.insertWeatherData(data.body()!!) // insert in room db
            }
        }
        return localDataSource.getWeatherData()
    }



     fun saveFavouriteCoord(latitude: Double, longitude: Double, title: String?) {
         Log.i("TAG", "saveFavouriteCoord: on saving : $latitude")
        localDataSource.insertFavouriteCoord(latitude,longitude,title)
    }

    fun getLocalWeather(): WeatherResponse {
        return localDataSource.getWeatherData()
    }


    fun getNullFavouriteLocations(): List<FavouriteCoordination> {
        return localDataSource.getNullFavouriteLocations()
    }

    fun getNotNullFavourite(): LiveData<List<FavouriteCoordination>> {
        return localDataSource.getNotNullFavourite()
    }


    fun deleteFavourite(lat: Double, lon: Double) {
        Log.i("TAG", "deleteFavourite: $lat")
        localDataSource.deleteFavourite(lat,lon)
        localDataSource.deleteFavouriteWeather(lat,lon)
    }


    suspend fun getFavWeatherData(lat: Double=0.0 , lon: Double=0.0,state:Boolean): FavouriteWeatherResponse {
        // get current
        if(lat != 0.0 && lon != 0.0){
            val data = remoteDataSource.getFavouriteWeatherData(lat.toString(),lon.toString()) // api call
            Log.i("TAG", "getfavWeatherData: inside api")
            if(data.isSuccessful){
                Log.i("TAG", "getFavWeatherData: onscucess ${data.body()}")
                data.body()?.lat = lat
                data.body()?.lon = lon
                localDataSource.insertfavouriteWeatherData(data.body()!!) // insert in room db
            }
        }


        return localDataSource.getFavouriteWeatherData(lat,lon)
    }

    fun getLocalFavouriteWeather(lat: Double, lon: Double): LiveData<FavouriteWeatherResponse> {
        return localDataSource.getLocalFavouriteWeather(lat,lon)
    }



    fun getWeatherAlertStatus(lat: Double, lng: Double, type: String): Boolean {
        var key = false

//        Toast.makeText(application.applicationContext,"inside function",Toast.LENGTH_LONG).show()
        Log.i("TAG", "getWeatherAlertStatus: inside")
//        runBlocking {
            var local = localDataSource.getWeatherData()
            if (local.weatherState.weather[0].main.toUpperCase() == type.toUpperCase()) {
                key = true
            }
//        }
        return key
    }

    fun getCurrentLat(): String? {
        return localDataSource.getCurrentLat()
    }

    fun getCurrentLon(): String? {
        return localDataSource.getCurrentLon()
    }

    fun insertAlarm(
        currentLat: String,
        currentLon: String,
        type: String,
        date: String,
        time: String
    ): Long {
        return localDataSource.insertAlarm(currentLat,currentLon,type,date,time)
    }

    fun getAlarmLat(id: Long): Double {
        return localDataSource.getAlarmLat(id)
    }

    fun getAlarmLon(id: Long): Double {
        return localDataSource.getAlarmLon(id)
    }

    fun getAlarmType(id: Long): String? {
        return localDataSource.getAlarmType(id)
    }

    fun deleteAlarm(id: Long) {
        localDataSource.deleteAlarm(id)
    }

    fun getAllAlarms(): LiveData<List<Alarm>> {
        return localDataSource.getAllAlarms()
    }

    fun updateLocality(cityName: String) {
        return localDataSource.updateLocality(cityName)
    }


}