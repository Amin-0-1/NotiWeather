package com.example.forecast_mvvm.screens

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.forecast_mvvm.R
import com.example.forecast_mvvm.databinding.ActivityMainBinding
import com.example.forecast_mvvm.utilities.ILanguage
import com.example.forecast_mvvm.utilities.SettingsSP
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import java.util.*


class MainActivity : AppCompatActivity() ,ILanguage{

    lateinit var binding:ActivityMainBinding
    lateinit var navController:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        prepareNavigation()
        Log.i("TAG", "onCreate: ")

        val radius = resources.getDimension(com.example.forecast_mvvm.R.dimen.default_corner_radius) //32dp

        val toolbar = findViewById<MaterialToolbar>(binding.toolbar.id)

        val materialShapeDrawable = toolbar.background as MaterialShapeDrawable
        materialShapeDrawable.shapeAppearanceModel = materialShapeDrawable.shapeAppearanceModel
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, radius)
            .build()
    }

    //Navigation specific
    private fun prepareNavigation() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        binding.bottomNav.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        Log.i("TAG", "attachBaseContext: ")
        SettingsSP.setDefaultSettings(this)
        SettingsSP.loadSettings(applicationContext)

        val lang = SettingsSP.getLanguageSetting()
        Log.i("TAG", "attachBaseContext: ${SettingsSP.getLanguageSetting()}")
//        getLocaleContext(newBase)
        setLocale(lang, resources)
    }

//    private fun setLocale(lng: String) {
//        val locale = Locale(lng)
//        Locale.setDefault(locale)
//        val config = Configuration()
//        config.locale = locale
//        resources.updateConfiguration(config, resources.displayMetrics)
//    }
}