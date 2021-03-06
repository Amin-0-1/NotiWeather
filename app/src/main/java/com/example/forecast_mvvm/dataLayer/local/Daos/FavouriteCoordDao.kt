package com.example.forecast_mvvm.dataLayer.local.Daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteCoordination

@Dao
interface FavouriteCoordDao {
    @Query("SELECT * FROM favCoord")
    fun getAllFavouriteCoord(): List<FavouriteCoordination>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavCoord(favCoord: FavouriteCoordination)

    @Query("Select * from favCoord where title is null")
    fun getNullFavouriteLocations(): List<FavouriteCoordination>

    @Query("select * from favCoord where title is not null")
    fun getNotNullFavourite(): LiveData<List<FavouriteCoordination>>

    @Query("delete from favCoord where lat is :latitude and lon is :longitude ")
    fun deleteFavourite(latitude: Double, longitude: Double)
}