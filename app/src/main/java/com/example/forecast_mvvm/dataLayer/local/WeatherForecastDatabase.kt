package com.example.forecast_mvvm.dataLayer.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.forecast_mvvm.dataLayer.entities.FavouriteCoordination
import com.example.forecast_mvvm.dataLayer.entities.models.Converter
import com.example.forecast_mvvm.dataLayer.local.Daos.FavouriteCoordDao
import com.example.forecast_mvvm.dataLayer.local.Daos.WeatherDao
import com.example.forecast_mvvm.dataLayer.remote.WeatherResponse
// dt to long
@Database(entities = [WeatherResponse::class,FavouriteCoordination::class], version = 5)
@TypeConverters(Converter::class)

 abstract class WeatherForecastDatabase:RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun favCoordDao(): FavouriteCoordDao

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
