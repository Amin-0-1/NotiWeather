package com.example.forecast_mvvm.screens.favourite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.forecast_mvvm.dataLayer.Repository

class FavouriteViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: Repository = Repository(application)

    fun saveFavouriteCoord(latitude: Double, longitude: Double) {
        repository.saveFavouriteCoord(latitude,longitude)
    }

}