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



}