package com.example.forecast_mvvm.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.forecast_mvvm.R

object SettingsSP {

    private lateinit var location:String
    private lateinit var units:String
    private lateinit var language:String
    private lateinit var sp:SharedPreferences

    fun setDefaultSettings(context:Context){
        PreferenceManager.setDefaultValues(context, R.xml.root_preferences,false)
    }

    fun loadSettings(context: Context){
        sp =  PreferenceManager.getDefaultSharedPreferences(context)
        location = sp.getString("GPS", "")!!
        units = sp.getString("Kelvin","")!!
        language = sp.getString("English","")!!
    }

    @SuppressLint("CommitPrefEdits")
    fun setLocation(){
        sp.edit().putString("GPS","setLocation").apply()
    }
    fun getLocationSetting():String{
        return this.location
    }
    fun getUnitSetting():String{
        return this.units
    }
    fun getLanguageSetting():String{
        return this.language
    }
}