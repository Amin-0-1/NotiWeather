package com.example.forecast_mvvm.dataLayer.remote

import com.example.forecast_mvvm.dataLayer.local.response.FavouriteWeatherResponse
import com.example.forecast_mvvm.dataLayer.remote.response.WeatherResponse
import com.example.forecast_mvvm.utilities.SettingsSP
import retrofit2.Response

class RemoteDataSource() {

    private var retrofit:OpenWeatherMapAPI = Network.createRetrofit()

    suspend fun getCurrentWeatherData(lat:String, long:String): Response<WeatherResponse> {

        return retrofit.getCurrentWeatherData(lat,long,SettingsSP.getUnitSetting(),SettingsSP.getLanguageSetting())
    }

    suspend fun getFavouriteWeatherData(lat:String,long:String):Response<FavouriteWeatherResponse>{
        return retrofit.getFavouriteWeatherData(lat,long,SettingsSP.getUnitSetting(),SettingsSP.getLanguageSetting())
    }
}