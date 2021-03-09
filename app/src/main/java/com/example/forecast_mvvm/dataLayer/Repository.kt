package com.example.forecast_mvvm.dataLayer
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.forecast_mvvm.dataLayer.remote.response.WeatherResponse
import com.example.forecast_mvvm.dataLayer.local.LocalDataSource
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteCoordination
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteWeatherResponse
import com.example.forecast_mvvm.dataLayer.remote.RemoteDataSource
import kotlinx.coroutines.runBlocking

class Repository(private val application: Application) {

    private val remoteDataSource:RemoteDataSource = RemoteDataSource()
    private val localDataSource:LocalDataSource = LocalDataSource(application)


    suspend fun getWeatherData(lat: String="", lon: String=""): WeatherResponse {

        // get current
        if(lat != "" && lon != ""){
            val data = remoteDataSource.getCurrentWeatherData(lat,lon) // api call
            Log.i("TAG", "getWeatherData: inside api")
            if(data.isSuccessful){
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

        if(state){
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
        }


        return localDataSource.getFavouriteWeatherData(lat,lon)
    }

    fun getLocalFavouriteWeather(lat: Double, lon: Double): LiveData<FavouriteWeatherResponse> {
        return localDataSource.getLocalFavouriteWeather(lat,lon)
    }



    fun getWeatherAlertStatus(lat: Double, lng: Double, type: String): Boolean {
        var key = false


        runBlocking {
            var local = localDataSource.getWeatherData()

            val data = remoteDataSource.getCurrentWeatherData(lat.toString(), lng.toString()) // api call
            Log.i("TAG", "getWeatherData: inside api")
            if (data.isSuccessful) {
                local = data.body()!!

                localDataSource.insertWeatherData(local) // insert in room db
                if (local.weatherState.weather[0].main.toUpperCase() == type.toUpperCase()) {
                    key = true
                }

            }
        }
        return key
    }


}