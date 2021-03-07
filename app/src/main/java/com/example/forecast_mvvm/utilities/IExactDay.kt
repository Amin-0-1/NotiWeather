package com.example.forecast_mvvm.utilities

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

interface IExactDay {
    @SuppressLint("SimpleDateFormat")
    fun exactDay(dt: Long): String {
        return SimpleDateFormat("EEEE").format(Date(dt * 1000))
    }
}