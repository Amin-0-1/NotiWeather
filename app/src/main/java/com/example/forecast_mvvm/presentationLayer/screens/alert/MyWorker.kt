package com.example.forecast_mvvm.presentationLayer.screens.alert

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_VIBRATE
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.forecast_mvvm.dataLayer.Repository
import kotlinx.coroutines.*


class MyWorker(private val context: Context, workerParams: WorkerParameters) :Worker(
    context,
    workerParams
){


    val repo = Repository(context.applicationContext as Application)
    override fun doWork(): Result {

//        createNotificationChannels()
//        sendOnChannel2("dddd")
//        playSound()
//
        val id = inputData.getLong("id", 0)
        val lat = repo.getAlarmLat(id)
        val lon = repo.getAlarmLon(id)
        val type = repo.getAlarmType(id)
        runBlocking {
//            withContext(Dispatchers.Main){
//                Toast.makeText(context,"inside do work", Toast.LENGTH_LONG).show()
//            }

            val job = CoroutineScope(Dispatchers.IO).launch { repo.getWeatherData(lat.toString(),lon.toString())}
            job.invokeOnCompletion {
                val checkState= repo.getWeatherAlertStatus(lat, lon, type!!)
                if (checkState)
                {
                    createNotificationChannels()
                    sendOnChannel2("$type")
                    playSound()
                }
            }
        }

        repo.deleteAlarm(id)
        return Result.success()
    }

    private fun playSound() {
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(applicationContext, notification)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private  fun createNotificationChannels() {
        // create android channel
        var androidChannel: NotificationChannel? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            androidChannel = NotificationChannel(
                "2",
                "channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            androidChannel.enableLights(true)
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true)
            // Sets the notification light color for notifications posted to this channel
            androidChannel.lightColor = Color.GREEN
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val manager: NotificationManager = applicationContext.getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(androidChannel)
        }
    }

    private fun sendOnChannel2(message: String) {

        val notificationManager = NotificationManagerCompat.from(applicationContext);
        val notification: Notification = NotificationCompat.Builder(applicationContext, "2").setDefaults(
            DEFAULT_VIBRATE
        )
            .setSmallIcon(com.example.forecast_mvvm.R.drawable.ic_alarm_on_24)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        com.example.forecast_mvvm.R.drawable.ic_alarm_on_24))
            .setContentTitle(message)
            .setContentText("watch out now it's $message in your area")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setOnlyAlertOnce(true)
            .build()
        notificationManager.notify(2, notification)
    }
}