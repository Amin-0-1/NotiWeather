package com.example.forecast_mvvm.utilities

import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.DialogFragment
import java.util.*

class MyTimePicker(private var listener:OnTimeSetListener):DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calender = Calendar.getInstance()
        val hour = calender[Calendar.HOUR_OF_DAY]
        val minute = calender[Calendar.MINUTE]

        return TimePickerDialog(context, listener, hour, minute, DateFormat.is24HourFormat(context))
    }
}