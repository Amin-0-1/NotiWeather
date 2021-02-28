package com.example.forecast_mvvm.utilities

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.forecast_mvvm.R

object SettingsSP {

    private lateinit var location:String
    private lateinit var units:String
    private lateinit var language:String

    fun setDefaultSettings(context:Context){
        PreferenceManager.setDefaultValues(context, R.xml.root_preferences,false)
    }

    fun loadSettings(context: Context){
        val sp =  PreferenceManager.getDefaultSharedPreferences(context)
        location = sp.getString("GPS", "")!!
        units = sp.getString("Kelvin","")!!
        language = sp.getString("English","")!!
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