package com.example.forecast_mvvm.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.forecast_mvvm.R
import com.example.forecast_mvvm.databinding.ActivityMainBinding
import com.example.forecast_mvvm.utilities.SettingsSP

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    lateinit var navController:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        prepareNavigation()
        SettingsSP.setDefaultSettings(this)
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
}