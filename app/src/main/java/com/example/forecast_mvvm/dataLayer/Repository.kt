package com.example.forecast_mvvm.dataLayer
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.forecast_mvvm.dataLayer.remote.response.WeatherResponse
import com.example.forecast_mvvm.dataLayer.local.LocalDataSource
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteCoordination
import com.example.forecast_mvvm.dataLayer.remote.RemoteDataSource

class Repository(private val application: Application) {

    private val remoteDataSource:RemoteDataSource = RemoteDataSource()
    private val localDataSource:LocalDataSource = LocalDataSource(application)


    suspend fun getWeatherData(lat: String="", lon: String=""): WeatherResponse {

        if(lat != "" && lon != ""){
            val data = remoteDataSource.getCurrentWeatherData(lat,lon) // api call
            Log.i("TAG", "getWeatherData: inside api")
            if(data.isSuccessful){
                localDataSource.insertWeatherData(data.body()!!) // insert in room db
            }
//            else{
//
//            }
        }
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

    fun getNotNullFavourite(): List<FavouriteCoordination> {
        return localDataSource.getNotNullFavourite()
    }

    fun getAllFavourite(): List<FavouriteCoordination> {
        return localDataSource.getAllFavouriteCoord()
    }

    fun deleteFavourite(lat: Double, lon: Double) {
        localDataSource.deleteFavourite(lat,lon)
    }


}