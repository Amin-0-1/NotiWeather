package com.example.forecast_mvvm.dataLayer.remote

import androidx.lifecycle.MutableLiveData
import com.example.forecast_mvvm.dataLayer.entities.CurrentWeatherEntry
import com.example.forecast_mvvm.dataLayer.entities.WeatherResponse
import com.example.forecast_mvvm.ui.home.WeatherViewModel
import retrofit2.Call
import retrofit2.Response

class RemoteDataSource() {

    private var retrofit:OpenWeatherMapAPI = Network.createRetrofit()

    suspend fun getCurrentWeatherData(lat:String, long:String): Response<WeatherResponse> {
        return retrofit.getCurrentWeatherData(lat,long)
    }

//    fun getTasks(): Call<List<Task?>?>? {
//        return retrofit.getTasks()
//    }
//
//    fun getTask(id: Int): Call<Task?>? {
//        return retrofit.getTaskById(id)
//    }
}