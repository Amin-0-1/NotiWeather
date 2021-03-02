package com.example.forecast_mvvm.dataLayer.local.Daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecast_mvvm.dataLayer.entities.FavouriteCoordination
import com.example.forecast_mvvm.dataLayer.remote.WeatherResponse

@Dao
interface FavouriteCoordDao {
    @Query("SELECT * FROM favCoord")
    fun getAllFavouriteCoord(): LiveData<FavouriteCoordination>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavCoord(favCoord: FavouriteCoordination)
}