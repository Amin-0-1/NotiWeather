package com.example.forecast_mvvm.dataLayer.local.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm")

class Alarm(
    val lat: Double,
    val lon: Double,
    val title: String,
    val date: String,
    val time: String,

    ){
    @PrimaryKey(autoGenerate = true)
    var id :Long = 0
}
