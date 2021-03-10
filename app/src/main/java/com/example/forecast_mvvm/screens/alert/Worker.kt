package com.example.forecast_mvvm.screens.alert

import android.R
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_VIBRATE
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.forecast_mvvm.dataLayer.Repository


//import com.mohamedabdallah.weather.repo.HomeRepository
//import com.mohamedabdallah.weather.ui.navigation.home.HomeViewModel


class Worker(context: Context, workerParams: WorkerParameters) :Worker(context, workerParams){

    private val repo = Repository(Application())
    override fun doWork(): Result {

//        val type=inputData.getString("type")
//        val lat=inputData.getDouble("lat", 0.0)
//        val lng=inputData.getDouble("lng", 0.0)

        val id = inputData.getLong("id",0)
        Log.i("TAG", "doWork:-------------------------------------- $id")
        val lat = repo.getAlarmLat(id)
        val lon = repo.getAlarmLon(id)
        val type = repo.getAlarmType(id)
        val x= repo.getWeatherAlertStatus(lat, lon, type!!)
        if (x == true)
        {
            Log.i("TAG", "doWork: dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd")
            createNotificationChannels()
            sendOnChannel2("$type")
        }
        else{
//            createNotificationChannels()
//            sendOnChannel2("$type")
            Log.i("TAG", "doWork: hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh")
        }

        repo.deleteAlarm(id)
        return Result.success()
    }

    private  fun createNotificationChannels() {
        // create android channel
        var androidChannel: NotificationChannel? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            androidChannel = NotificationChannel("2", "channel", NotificationManager.IMPORTANCE_DEFAULT)
            androidChannel.enableLights(true)
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true)
            // Sets the notification light color for notifications posted to this channel
            androidChannel.lightColor = Color.GREEN
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val manager: NotificationManager = applicationContext.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(androidChannel)
        }
    }

    private fun sendOnChannel2(message: String) {

        val notificationManager = NotificationManagerCompat.from(applicationContext);
        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val notification: Notification = NotificationCompat.Builder(applicationContext, "2").setDefaults( DEFAULT_VIBRATE )
            .setSmallIcon(R.drawable.ic_popup_reminder)
            .setContentTitle(message)
            .setContentText("watch out now it's $message in your area")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .build()
        notificationManager.notify(2, notification)
    }
}