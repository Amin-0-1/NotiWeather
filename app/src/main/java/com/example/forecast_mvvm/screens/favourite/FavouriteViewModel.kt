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
import com.example.forecast_mvvm.dataLayer.Repository
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteCoordination
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*

class FavouriteViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: Repository = Repository(application)

    private var locations = MutableLiveData<List<FavouriteCoordination>>()
    private var nullLocations = MutableLiveData<List<FavouriteCoordination>>()

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext


    fun getLocations(): LiveData<List<FavouriteCoordination>> {
        return locations
    }
    fun getNullLocations():LiveData<List<FavouriteCoordination>>{
        return nullLocations
    }

    fun saveFavouriteCoord(latitude: Double, longitude: Double,title:String? = null) {
        repository.saveFavouriteCoord(latitude,longitude,title)
    }


    fun favouriteLocations() {
        CoroutineScope(Dispatchers.IO).launch {
            val deferred = async { repository.getNotNullFavourite() }

            withContext(Dispatchers.Main){
                deferred.await().observeForever {
                    if(it.isNotEmpty())
                        locations.postValue(it)
                }
            }
        }

        if(isNetworkAvailable()){
            // TODO: 3/3/2021 retreive nulls and get its locality
            CoroutineScope(Dispatchers.IO).launch {
                val deferred = async { repository.getNullFavouriteLocations() }
                withContext(Dispatchers.Main){
                    deferred.await().observeForever {
                        if(it.isNotEmpty())
                            nullLocations.postValue(it)
                    }
                }
            }
        }
    }


    fun getNullFavouriteLocations() {
        CoroutineScope(Dispatchers.IO).launch {
            val deferred = async{ repository.getNullFavouriteLocations() }

            withContext(Dispatchers.Main){
                deferred.await().observeForever {
                    Log.i("TAG", "getNullFavouriteLocations: $it")
                    locations.postValue(it)
                }
            }
        }
    }

    fun getCityName(lat: Double, lon: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val res:String
        if (lat != 0.0 && lon != 0.0) {

            try {
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                res = addresses[0].getAddressLine(0)
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


    private fun isNetworkAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun updateLocaility(it: List<FavouriteCoordination>) {
        if(it.isNotEmpty()){

            for (i in it){
//                Log.i("TAG", "updateLocaility: ${i.lat}")
                val c = getCityName(i.lat,i.lon)
                saveFavouriteCoord(i.lat,i.lon,c)
            }
        }
    }

}