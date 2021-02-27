package com.example.forecast_mvvm.ui.home

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecast_mvvm.dataLayer.Repository
import com.example.forecast_mvvm.dataLayer.entities.WeatherResponse
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

//    private var remoteDataSource: RemoteDataSource = RemoteDataSource()
    private var repository:Repository = Repository(application)

    private val loadingLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Boolean>()
    private var currentWeatherLiveData = MutableLiveData<WeatherResponse>()


    fun getWeather(){

        CoroutineScope(Dispatchers.IO).launch {
            //todo :: get user location
            val deferred = async{ repository.getWeatherData("30.033333", "31.233334") }

            withContext(Dispatchers.Main){
                deferred.await().observeForever {
                    currentWeatherLiveData.postValue(it)
                }
            }
        }
    }

    fun getLoading(): LiveData<Boolean> {
        return loadingLiveData
    }
    fun getErrorState(): LiveData<Boolean> {
        return errorLiveData
    }
    fun getCurrentWeatherLiveData(): LiveData<WeatherResponse> {
        return currentWeatherLiveData
    }

    @SuppressLint("SimpleDateFormat")
    fun extractTime(dt: Long): String {
        val transformedDate = SimpleDateFormat("hh a").format(Date(dt * 1000))

        if(transformedDate[0] == '0') {
            return transformedDate.substring(1)
        }

        return transformedDate
    }

    fun exactDay(dt: Long): String {
        return SimpleDateFormat("EEEE").format(Date(dt * 1000))
    }

    fun getCurrentDate(dt: Long) :String{
        return SimpleDateFormat("dd MMM yyyy").format(Date(dt * 1000))
    }
}