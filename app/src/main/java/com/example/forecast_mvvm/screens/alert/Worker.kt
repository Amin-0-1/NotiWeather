package com.example.forecast_mvvm.screens.alert

import android.R
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

import android.os.Build
import android.util.Log

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.forecast_mvvm.dataLayer.Repository
//import com.mohamedabdallah.weather.repo.HomeRepository
//import com.mohamedabdallah.weather.ui.navigation.home.HomeViewModel


class Worker(context: Context,workerParams: WorkerParameters) :Worker(context, workerParams){

    private val repo = Repository(Application())
    override fun doWork(): Result {

        // Log.i("TAG", "doWork: $i")
        val type=inputData.getString("type")
        val lat=inputData.getDouble("lat",0.0)
        val lng=inputData.getDouble("lng",0.0)

        val x= repo.getWeatherAlertStatus(lat,lng,type!! )
        if (x == true)
        {
            Log.i("TAG", "doWork: dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd")
            createNotificationChannels()
            sendOnChannel2("key!!")
        }
        else
            Log.i("TAG", "doWork: hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh")

        return Result.success()
    }

    private  fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                "1",
                "Channel 1",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "This is Channel 1"
            val channel2 = NotificationChannel(
                "2",
                "Channel 2",
                NotificationManager.IMPORTANCE_LOW
            )
            channel2.description = "This is Channel 2"
            val manager: NotificationManager =
                applicationContext.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel1)
            manager.createNotificationChannel(channel2)
        }
    }

    private fun sendOnChannel2(message:String) {

        val notificationManager = NotificationManagerCompat.from(applicationContext);
        val notification: Notification = NotificationCompat.Builder(applicationContext, "2")
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .setContentTitle(message)
            .setContentText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        notificationManager.notify(2, notification)
    }
}