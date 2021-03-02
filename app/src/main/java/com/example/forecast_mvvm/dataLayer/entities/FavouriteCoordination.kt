package com.example.forecast_mvvm.dataLayer.entities

import androidx.room.Entity

@Entity(tableName = "favCoord", primaryKeys = ["lat", "lon"])
data class FavouriteCoordination (val lat:Double,val lon:Double)