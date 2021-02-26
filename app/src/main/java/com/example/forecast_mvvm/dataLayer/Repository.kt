package com.example.forecast_mvvm.dataLayer
import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.forecast_mvvm.dataLayer.entities.WeatherResponse
import com.example.forecast_mvvm.dataLayer.local.LocalDataSource
import com.example.forecast_mvvm.dataLayer.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class Repository(val application: Application) {

    private val remoteDataSource:RemoteDataSource = RemoteDataSource()
    private val localDataSource:LocalDataSource = LocalDataSource(application)

    suspend fun getWeatherData(lat: String, lon: String): LiveData<WeatherResponse> {
        var data = remoteDataSource.getCurrentWeatherData(lat,lon) // api call

        if(data.isSuccessful){
            localDataSource.insert(data.body()!!) // insert in room db
        }else{}


        return localDataSource.getWeatherData()
//         localDataSource.getWeatherData().observeForever {
//           return withContext(Dispatchers.Main){
//                return@withContext it
//            }
//         }

    }


}