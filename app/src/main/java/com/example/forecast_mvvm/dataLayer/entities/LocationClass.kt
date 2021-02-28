package com.example.forecast_mvvm.dataLayer.entities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

class LocationClass(private var context: Context,private var fusedlocation:FusedLocationProviderClient) {
    private var lat:Double = 0.0
    private var lon:Double = 0.0

    fun getLat():Double{
        return lat
    }
    fun getLon():Double{
        return lon
    }

    @SuppressLint("MissingPermission")
    fun requestNewLocationData(): LocationClass {
        val locationRequest = LocationRequest()
        locationRequest.interval = 0
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.numUpdates = 1
        Looper.prepare()
        fusedlocation.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        return this
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val location = locationResult.lastLocation
            lat = location.latitude
            lon = location.longitude
//            Log.i("TAG", "onLocationResult: $lon")
        }
    }

}