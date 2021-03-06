package com.example.forecast_mvvm

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.forecast_mvvm.screens.favourite.FavouriteViewModel
import com.example.forecast_mvvm.utilities.SettingsSP
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MyMap : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private  var  mark: LatLng? =null
    private lateinit var state:String
    private lateinit var mPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_map)
        mPreferences = applicationContext.getSharedPreferences("location", MODE_PRIVATE)
        state = intent.getStringExtra("state").toString()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_HYBRID;
        SettingsSP.loadSettings(applicationContext)

        if(SettingsSP.getLocationSetting() != "GPS"){
            val lat = mPreferences.getString("lat","null")
            val lon = mPreferences.getString("lon","null")

            mark = LatLng(lat?.toDouble()!!, lon?.toDouble()!!)
            map.addMarker(MarkerOptions().position(mark!!).title("current"))
            map.moveCamera(CameraUpdateFactory.newLatLng(mark))
        }

        map.setOnMapClickListener {
            map.clear()
            mark = LatLng(it.latitude, it.longitude)
            map.addMarker(MarkerOptions().position(mark!!).title("mark"))
            map.moveCamera(CameraUpdateFactory.newLatLng(mark))

        }

    }

    private fun saveLocationOnSP() {
        if(state == "fav" && mark != null){
            val favouriteViewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
            favouriteViewModel.saveFavouriteCoord(mark!!.latitude, mark!!.longitude)

        }else if(state == "set" && mark != null){

            val sharedPref = getSharedPreferences("location", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPref.edit()

            if(mark != null){
                editor.putString("lat",mark?.latitude.toString())
                editor.putString("lon",mark?.longitude.toString())
                editor.apply()
            }

        }else{
            Log.i("TAG", "onDestroy: no mark")
        }
    }


    @SuppressLint("CommitPrefEdits")
    override fun onDestroy() {
        super.onDestroy()
        saveLocationOnSP()

    }

}