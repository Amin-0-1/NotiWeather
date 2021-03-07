package com.example.forecast_mvvm.utilities

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

interface IExactTime {
    @SuppressLint("SimpleDateFormat")
    fun extractTime(dt: Long): String {
        val transformedDate = SimpleDateFormat("hh a").format(Date(dt * 1000))

        if(transformedDate[0] == '0') {
            return transformedDate.substring(1)
        }

        return transformedDate
    }
}