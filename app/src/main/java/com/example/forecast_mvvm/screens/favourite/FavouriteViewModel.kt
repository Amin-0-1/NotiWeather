package com.example.forecast_mvvm.screens.favourite

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.forecast_mvvm.dataLayer.Repository
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteCoordination
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteWeatherResponse
import com.example.forecast_mvvm.utilities.IExactDay
import com.example.forecast_mvvm.utilities.IExactTime
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class FavouriteViewModel(application: Application) : AndroidViewModel(application) ,IExactDay,IExactTime{
    private var repository: Repository = Repository(application)

//    private var locations = MutableLiveData<List<FavouriteCoordination>>()
//    private var nullLocations = MutableLiveData<List<FavouriteCoordination>>()
    private var message = MutableLiveData<Boolean>()
    private var favLocationsData = MutableLiveData<FavouriteWeatherResponse>()
    private var geocoder:Geocoder = Geocoder(application.applicationContext, Locale.getDefault())


    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
//
//
    fun getFavLocationData(): LiveData<FavouriteWeatherResponse> {
        return favLocationsData
    }
//    fun getNullLocations():LiveData<List<FavouriteCoordination>>{
//        return nullLocations
//    }
    fun getMessage():LiveData<Boolean>{
        return message
    }

    fun saveFavouriteCoord(latitude: Double, longitude: Double, title:String? = null) {

        if(title == null && !isNetworkAvailable()){
            Log.i("TAG", "saveFavouriteCoord: meessage post false")
            message.postValue(false)
            repository.saveFavouriteCoord(latitude,longitude,title)
        }else if(title == null){ // network exist
            repository.saveFavouriteCoord(latitude,longitude,getCityName(latitude,longitude))
            getFavouriteItemDetails(FavouriteCoordination(latitude,longitude))
        }else{
            repository.saveFavouriteCoord(latitude,longitude,title)
        }

        getNotNullFavouriteLocation()
    }


    fun favouriteLocations() {

        // get local favourites
        getNotNullFavouriteLocation()

        if(isNetworkAvailable()){
            updateLocaility(repository.getNullFavouriteLocations()) // gets null title records from favCord in db
        }
    }

    fun getNotNullFavouriteLocation():LiveData<List<FavouriteCoordination>> {
//        viewModelScope.launch {
//            locations.postValue(repository.getNotNullFavourite())
//        }
        return repository.getNotNullFavourite()
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    private fun updateLocaility(it: List<FavouriteCoordination>) {
        if(it.isNotEmpty()){

            for (i in it){
                val c = getCityName(i.lat,i.lon)
                saveFavouriteCoord(i.lat,i.lon,c)
                getFavouriteItemDetails(FavouriteCoordination(i.lat,i.lon))
            }
        }
    }

    private fun getCityName(lat: Double, lon: Double): String {

        if (lat != 0.0 && lon != 0.0) {

            try {
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                val res = addresses[0].getAddressLine(0)
                return res
            }catch (e: IOException) {
                Log.i("TAG", "getCityName: catch")
                e.printStackTrace()
            }
            return "null"
        }else{
            Log.i("TAG", "getCityName: null")
            return "null"
        }
    }

    fun deleteFavourite(lat: Double, lon: Double) {
//        viewModelScope.launch {
            repository.deleteFavourite(lat,lon)
            getNotNullFavouriteLocation()
            message.postValue(true)
//        }
    }

    fun getFavouriteItemDetails(favouriteCoordination: FavouriteCoordination) {
        viewModelScope.launch {
            Log.i("lat", "getFavouriteItemDetails: lat is ${favouriteCoordination.lat}")
            repository.getFavWeatherData(favouriteCoordination.lat,favouriteCoordination.lon)
        }
    }
    fun getLocalFavouriteItemDetails(lat: Double, lon: Double): LiveData<FavouriteWeatherResponse> {
        Log.i("track", "getLocalFavouriteItemDetails: ")
        return repository.getLocalFavouriteWeather(lat,lon)
    }


}