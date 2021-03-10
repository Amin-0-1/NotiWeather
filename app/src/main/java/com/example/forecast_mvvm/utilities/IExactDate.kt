package com.example.forecast_mvvm.utilities

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

interface IExactDate {
    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(dt: Long) :String{
        return SimpleDateFormat("dd MMM yyyy").format(Date(dt * 1000))
    }
}