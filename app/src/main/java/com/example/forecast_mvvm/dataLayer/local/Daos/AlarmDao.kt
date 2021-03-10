package com.example.forecast_mvvm.dataLayer.local.Daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecast_mvvm.dataLayer.local.response.Alarm
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteCoordination

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlarm(alarm:Alarm):Long

    @Query("select lat from alarm where id is :id")
    fun getAlarmLat(id: Long): Double

    @Query("select lon from alarm where id is :id")
    fun getAlarmLon(id: Long): Double

    @Query("select title from alarm where id is :id")
    fun getAlarmType(id: Long): String?

    @Query("delete from alarm where id is :id")
    fun deleteAlarm(id: Long)

    @Query("select * from alarm")
    fun getAllAlarms():LiveData<List<Alarm>>
}