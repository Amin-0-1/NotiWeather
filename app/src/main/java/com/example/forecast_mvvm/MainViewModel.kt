package com.example.forecast_mvvm

import android.app.Application
import android.content.Context
import android.location.LocationManager
import androidx.core.content.ContextCompat.getSystemService

class MainViewModel {
    lateinit var locationManager:LocationManager
    var context:Context?

    constructor(context:Context){
        this.context = context
    }

//    fun prepareLocation() {
//
//        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager;
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//    }


}