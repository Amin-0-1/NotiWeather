package com.example.forecast_mvvm.dataLayer
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.forecast_mvvm.dataLayer.remote.response.WeatherResponse
import com.example.forecast_mvvm.dataLayer.local.LocalDataSource
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteCoordination
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteWeatherResponse
import com.example.forecast_mvvm.dataLayer.remote.RemoteDataSource

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

        // get fav
//        getnot
        return localDataSource.getWeatherData()
    }



     fun saveFavouriteCoord(latitude: Double, longitude: Double, title: String?) {
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

    fun getAllFavourite(): List<FavouriteCoordination> {
        return localDataSource.getAllFavouriteCoord()
    }

    fun deleteFavourite(lat: Double, lon: Double) {
        localDataSource.deleteFavourite(lat,lon)
        localDataSource.deleteFavouriteWeather(lat,lon)
    }


    suspend fun getFavWeatherData(lat: Double=0.0 , lon: Double=0.0): FavouriteWeatherResponse {

        // get current
        if(lat != 0.0 && lon != 0.0){
            val data = remoteDataSource.getFavouriteWeatherData(lat.toString(),lon.toString()) // api call
            Log.i("TAG", "getfavWeatherData: inside api")
            if(data.isSuccessful){
                Log.i("TAG", "getFavWeatherData: onscucess ${data.body()}")
                localDataSource.insertfavouriteWeatherData(data.body()!!) // insert in room db
            }
        }

        return localDataSource.getFavouriteWeatherData(lat,lon)
    }

    fun getLocalFavouriteWeather(lat: Double, lon: Double): FavouriteWeatherResponse {
        return localDataSource.getLocalFavouriteWeather(lat,lon)
    }


}