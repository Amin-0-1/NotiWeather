package com.example.forecast_mvvm.dataLayer.local

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.forecast_mvvm.dataLayer.entities.WeatherResponse

class LocalDataSource(application:Application) {

    private var dao: WeatherDao = WeatherForecastDatabase.getInstanse(application).weatherDao();

    fun insert(body: WeatherResponse) {
        dao.insert(body)
    }

    fun getWeatherData(): LiveData<WeatherResponse> {
        return dao.getWeatherData()
    }

}