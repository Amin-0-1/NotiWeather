package com.example.forecast_mvvm.utilities

import android.content.res.Configuration
import android.content.res.Resources
import java.util.*

interface ILanguage {
     fun setLocale(lng: String,resources: Resources) {
        val locale = Locale(lng)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}