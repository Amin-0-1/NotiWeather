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
    fun getAllFavouriteCoord(): LiveData<List<FavouriteCoordination>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavCoord(favCoord: FavouriteCoordination)

    @Query("Select * from favCoord where title is null")
    fun getNullFavouriteLocations(): LiveData<List<FavouriteCoordination>>

    @Query("select * from favCoord where title is not null")
    fun getNotNullFavourite(): LiveData<List<FavouriteCoordination>>
}