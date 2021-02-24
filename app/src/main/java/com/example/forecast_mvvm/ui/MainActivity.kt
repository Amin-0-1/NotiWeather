package com.example.forecast_mvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.forecast_mvvm.MainViewModel
import com.example.forecast_mvvm.R
import com.example.forecast_mvvm.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.coroutineScope

class MainActivity : AppCompatActivity() {


    lateinit var binding:ActivityMainBinding
    lateinit var navController:NavController
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        prepareNavigation()
        prepareLogic()


    }




    // viewModel init and observers
    private fun prepareLogic() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        observeViewModel(mainViewModel)

        mainViewModel.getWeather() // api call
    }
    private fun observeViewModel(viewModel: MainViewModel) {
        viewModel.getCurrentWeather().observe(this, Observer {

        })

        viewModel.getErrorState().observe(this, Observer {

        })

        viewModel.getLoading().observe(this, Observer {

        })
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