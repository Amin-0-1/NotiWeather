package com.example.forecast_mvvm

import android.app.Application
import android.content.Context
import android.location.LocationManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.forecast_mvvm.dataLayer.entities.CurrentWeatherEntry
import com.example.forecast_mvvm.dataLayer.entities.WeatherResponse
import com.example.forecast_mvvm.dataLayer.remote.Network
import com.example.forecast_mvvm.dataLayer.remote.RemoteDataSource
import kotlinx.coroutines.*

class MainViewModel :ViewModel(){


    private var remoteDataSource:RemoteDataSource = RemoteDataSource()

    private val loadingLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Boolean>()
    private val currentWeatherLiveData = MutableLiveData<WeatherResponse>()

    fun getWeather(){

        CoroutineScope(Dispatchers.IO).launch {
            val data = remoteDataSource.getCurrentWeatherData("30.033333","31.233334")
            withContext(Dispatchers.Main){
                if(data.isSuccessful){
                    println(data.body())
                    currentWeatherLiveData.postValue(data.body())
                }else
                    println("not success")

            }

        }

    }

    fun getLoading():LiveData<Boolean>{
        return loadingLiveData
    }
    fun getErrorState():LiveData<Boolean>{
        return errorLiveData
    }
    fun getCurrentWeather():LiveData<WeatherResponse>{
        return currentWeatherLiveData
    }


}