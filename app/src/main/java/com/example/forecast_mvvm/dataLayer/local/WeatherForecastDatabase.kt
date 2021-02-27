package com.example.forecast_mvvm.dataLayer.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.forecast_mvvm.dataLayer.entities.Converter
import com.example.forecast_mvvm.dataLayer.entities.WeatherResponse
// dt to long
@Database(entities = [WeatherResponse::class], version = 4)
@TypeConverters(Converter::class)

 abstract class WeatherForecastDatabase:RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object{
        var db:WeatherForecastDatabase? = null

        fun getInstanse(application: Application): WeatherForecastDatabase{
            if (db == null) {
                db = Room.databaseBuilder(
                    application.applicationContext,
                    WeatherForecastDatabase::class.java, "task_db"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return db as WeatherForecastDatabase
        }
    }



}
