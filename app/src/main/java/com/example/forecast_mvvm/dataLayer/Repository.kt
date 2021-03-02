package com.example.forecast_mvvm.dataLayer
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.forecast_mvvm.dataLayer.remote.WeatherResponse
import com.example.forecast_mvvm.dataLayer.local.LocalDataSource
import com.example.forecast_mvvm.dataLayer.remote.RemoteDataSource

class Repository(private val application: Application) {

    private val remoteDataSource:RemoteDataSource = RemoteDataSource()
    private val localDataSource:LocalDataSource = LocalDataSource(application)


    suspend fun getWeatherData(lat: String="", lon: String=""): LiveData<WeatherResponse> {
        // TODO: 2/27/2021 check if there is internet access

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


//
//    fun testSaveFavCoord(){
//        localDataSource.insertFavourite()
//    }

    fun saveFavouriteCoord(latitude: Double, longitude: Double) {
        localDataSource.insertFavouriteCoord(latitude,longitude)
    }

    fun getLocal(): LiveData<WeatherResponse> {
        return localDataSource.getWeatherData()
    }


}