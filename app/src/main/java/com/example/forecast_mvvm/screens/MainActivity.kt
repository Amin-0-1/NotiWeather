package com.example.forecast_mvvm.screens

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.forecast_mvvm.R
import com.example.forecast_mvvm.databinding.ActivityMainBinding
import com.example.forecast_mvvm.utilities.SettingsSP
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    lateinit var navController:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        prepareNavigation()
        Log.i("TAG", "onCreate: ")
    }

    //Navigation specific
    private fun prepareNavigation() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        binding.bottomNav.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp( navController,null)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        Log.i("TAG", "attachBaseContext: ")
        SettingsSP.setDefaultSettings(this)
        SettingsSP.loadSettings(applicationContext)

        val lang = SettingsSP.getLanguageSetting()
        Log.i("TAG", "attachBaseContext: ${SettingsSP.getLanguageSetting()}")
//        getLocaleContext(newBase)
        setLocale(lang)
    }

    private fun setLocale(lng: String) {
        val locale = Locale(lng)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
    }

//    fun getLocaleContext(context: Context?): Context? {
//        context?.let {
//            val locale = Locale(getLocale(context))
//            val res = context.resources
//            val config = Configuration(res.configuration)
//            config.setLocale(locale)
//            return context.createConfigurationContext(config)
//        }
//
//        return context
//    }
//    fun getLocale(context: Context): String {
////        val modelRepository = ModelRepository(context = context)
//        var currentLang  = SettingsSP.getLanguageSetting()
//
////        when(currentLang){
////            DEFAULT_LANG_STRING-> currentLang = "en"
////            ARABIC_LANG_STRING-> currentLang = "ar"
////        }
//        Log.i("Helper",currentLang)
//        return  currentLang
//    }
}