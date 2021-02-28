package com.example.forecast_mvvm.screens.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecast_mvvm.dataLayer.Repository
import com.example.forecast_mvvm.dataLayer.entities.LocationClass
import com.example.forecast_mvvm.dataLayer.entities.WeatherResponse
import com.example.forecast_mvvm.utilities.SettingsSP
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.*
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

    private lateinit var location:LocationClass



    fun getWeather() {

        SettingsSP.loadSettings(context)
//        Log.i("TAG", "getWeather: ${location.getLat()} ${location.getLon()}")
        //todo :: get user location
        CoroutineScope(Dispatchers.IO).launch {
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
    fun locationPermission(activity:Activity,fusedLocationProviderClient: FusedLocationProviderClient) {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                location =  LocationClass(context,fusedLocationProviderClient)
                CoroutineScope(Dispatchers.IO).launch {
                    val deferred = async { location.requestNewLocationData() }
                    Log.i("TAG", "locationPermission: ${deferred.await().getLat()}")
                }


            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                ContextCompat.startActivity(context, intent, null)
            }
        } else {
            requestPermissions(activity)
        }

    }

    private fun checkPermission(): Boolean { // check permissions in run time
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(activity,arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

}