package com.example.forecast_mvvm.screens.alert

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.forecast_mvvm.dataLayer.Repository
import com.example.forecast_mvvm.dataLayer.local.response.Alarm

class AlertViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: Repository = Repository(application)

    fun getCurrentLat(): String?{
        return repository.getCurrentLat()
    }

    fun getCurrentLon(): String? {
        return repository.getCurrentLon()
    }

    fun insertAlarm(currentLat: String, currentLon: String, type: String,date:String,time:String): Long {
        return repository.insertAlarm(currentLat,currentLon,type,date,time)
    }

    fun getAllAlarms(): LiveData<List<Alarm>> {
        return repository.getAllAlarms()
    }

    fun deleteAlarm(id: Long) {
        repository.deleteAlarm(id)
    }
    // TODO: Implement the ViewModel
}