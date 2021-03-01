package com.example.forecast_mvvm.screens.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecast_mvvm.dataLayer.Repository
import com.example.forecast_mvvm.dataLayer.entities.WeatherResponse
import com.example.forecast_mvvm.utilities.SettingsSP
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

//    private var remoteDataSource: RemoteDataSource = RemoteDataSource()
    private var repository:Repository = Repository(application)
    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    private val loadingLiveData = MutableLiveData<Boolean>()
    private var errorLiveData = MutableLiveData<Boolean>()
    private var currentWeatherLiveData = MutableLiveData<WeatherResponse>()



    fun getWeather(
        requireActivity: FragmentActivity,
        fusedLocationClient: FusedLocationProviderClient
    ) {

        SettingsSP.loadSettings(context)
        locationPermission(requireActivity, fusedLocationClient)

//        Log.i("TAG", "getWeather: ${location.getLat()} ${location.getLon()}")
//        CoroutineScope(Dispatchers.IO).launch {
//            val deferred = async{ repository.getWeatherData("30.033333", "31.233334") }
//
//            withContext(Dispatchers.Main){
//                deferred.await().observeForever {
//                    currentWeatherLiveData.postValue(it)
//                }
//            }
//        }
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

    @SuppressLint("SimpleDateFormat")
    fun exactDay(dt: Long): String {
        return SimpleDateFormat("EEEE").format(Date(dt * 1000))
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(dt: Long) :String{
        return SimpleDateFormat("dd MMM yyyy").format(Date(dt * 1000))
    }

    fun checkWeatherStateDateValidation(weatherResponse: WeatherResponse): WeatherResponse? {
        val current = Calendar.getInstance().timeInMillis
        val timeStamp = weatherResponse.weatherState.dt
        return if(current+ 60*60*1000  <= timeStamp)
            weatherResponse
        else{
            null
        }
    }


    //Location methods
    fun locationPermission(
        activity: Activity,
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                requestNewLocationData(fusedLocationProviderClient)
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                ContextCompat.startActivity(context, intent, null)
            }
        } else {
            requestPermissions(activity)
        }

    }

    private fun checkPermission(): Boolean { // check permissions in run time
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 1
        )
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData(fusedLocationProviderClient: FusedLocationProviderClient) {
        val locationRequest = LocationRequest()
        locationRequest.interval = 0
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.numUpdates = 1
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    fun getCityName(lat: Double, lon: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val res:String
        if (lat != 0.0 && lon != 0.0) {
            try {
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                res = addresses[0]..toString()

                return res;
            } catch (e: IOException) {
                e.printStackTrace()
                return "null"
            }
        }else{
            Log.i("TAG", "getCityName: null")
            return "null"
        }
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val location = locationResult.lastLocation
            Log.i("TAG", "onLocationResult: ${location.latitude}")
            Log.i("TAG", "onLocationResult: ${location.longitude}")
            CoroutineScope(Dispatchers.IO).launch {
                val deferred = async{ repository.getWeatherData(
                    location.latitude.toString(),
                    location.longitude.toString()
                ) }

                withContext(Dispatchers.Main){
                    deferred.await().observeForever {
                        currentWeatherLiveData.postValue(it)
                    }
                }
            }
        }
    }

}