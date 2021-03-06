package com.example.forecast_mvvm.presentationLayer.screens.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.forecast_mvvm.dataLayer.Repository
import com.example.forecast_mvvm.dataLayer.remote.response.WeatherResponse
import com.example.forecast_mvvm.presentationLayer.other.MainActivity
import com.example.forecast_mvvm.utilities.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.*


@Suppress("DEPRECATION", "SENSELESS_COMPARISON")

class WeatherViewModel(application: Application) : AndroidViewModel(application),IExactDay ,IExactTime, IExactDate,INetwork{

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    private var repository:Repository = Repository(application)

    /**
     * start live data
     */
    private val loadingLiveData = MutableLiveData<Boolean>()
    val getLoading:LiveData<Boolean>get() = loadingLiveData

    private val requestPermissionLiveData = MutableLiveData<Boolean>();
    val getRequestPermissionLiveData:LiveData<Boolean>get() = requestPermissionLiveData

    private var internetStatLiveData = MutableLiveData<Boolean>()
    val getInternetState:LiveData<Boolean>get() = internetStatLiveData

    private var currentWeatherLiveData = MutableLiveData<WeatherResponse>()
    val getCurrentWeatherLiveData:LiveData<WeatherResponse>get() = currentWeatherLiveData

    /**
     * end live data
     */

//    private var geocoder:Geocoder = Geocoder(application.applicationContext, Locale.getDefault())
    private val mPreferences = context.getSharedPreferences("location", MODE_PRIVATE);


    fun getWeather(fusedLocationClient: FusedLocationProviderClient){
//        SettingsSP.loadSettings(context)

        getLocalWeatherDate()
        getRemoteWeatherData(fusedLocationClient)
    }

    //Location methods
    private fun getRemoteWeatherData(fusedLocationProviderClient: FusedLocationProviderClient) {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                if(isNetworkAvailable(context)){
                    Log.i("TAG", "isConnected: ")

                    if(checkLocationInSettings()){
                        val location = Location("") //provider name is unnecessary
                        location.latitude = mPreferences.getString("lat",null)?.toDouble() !!
                        location.longitude = mPreferences.getString("lon",null)?.toDouble() !!
                        Log.i("TAG", "checkLocationPermission: ${location.longitude}")
                        fetchRepoData(location)
                    }else {
                        Log.i("TAG", "elsesss: ")
                        requestNewLocationData(fusedLocationProviderClient)
                    }
                } else{
                    Log.i("TAG", "needed internet ")
//                    fetchRepoData()
                }

            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                ContextCompat.startActivity(context, intent, null)
            }
        } else {
            Log.i("TAG", "getRemoteWeatherData: else of permission")
            requestPermissionLiveData.value = true;
        }

    }

    private fun checkLocationInSettings(): Boolean {
        val mPreferences = context.getSharedPreferences("location", MODE_PRIVATE);
        return mPreferences.getString("lat", null) != null && SettingsSP.getLocationSetting() != "GPS"
    }

    fun checkUnit():Boolean{
        Log.i("TAG", "checkUnit: ${SettingsSP.getUnitSetting()}")

        return SettingsSP.getUnitSetting().equals("Imperial")
    }

//    fun isNetworkAvailable(context: Context): Boolean {
//        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
//        return activeNetwork?.isConnectedOrConnecting == true
//    }

    fun checkPermission(): Boolean { // check permissions in run time
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

//    private fun requestPermissions(activity: Activity) {
//        ActivityCompat.requestPermissions(
//                activity, arrayOf(
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION
//        ), 1
//        )
//    }
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
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.numUpdates = 1
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
        )
    }
    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val location = locationResult.lastLocation
            Log.i("TAG", "onLocationResult: ${location.latitude}")
            Log.i("TAG", "onLocationResult: ${location.longitude}")
            fetchRepoData(location)
        }
    }

    fun fetchRepoData(location: Location? = null){

        if(location == null){ // gps enabled in settings
            viewModelScope.launch {
                currentWeatherLiveData.postValue(repository.getWeatherData())
            }

        }else{ // set location in settings is enabled
            viewModelScope.launch {
                currentWeatherLiveData.postValue(repository.getWeatherData(location.latitude.toString(),location.longitude.toString()))
            }
        }
    }

    private fun getLocalWeatherDate() {

        viewModelScope.launch {
            val res = repository.getLocalWeather()
            if(res != null){
                currentWeatherLiveData.postValue(res)
            }
            else if(!isNetworkAvailable(context)){
                internetStatLiveData.postValue(true)
            }
        }
//        repository.getLocalWeather().observeForever {
//            if(it != null)
//                currentWeatherLiveData.postValue(it)
//        }
    }

}