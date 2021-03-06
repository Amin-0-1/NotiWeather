package com.example.forecast_mvvm.presentationLayer.screens.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forecast_mvvm.R
import com.example.forecast_mvvm.dataLayer.entities.models.WeatherState
import com.example.forecast_mvvm.databinding.WeatherFragmentBinding
import com.example.forecast_mvvm.presentationLayer.other.MainActivity
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*


class WeatherFragment : Fragment() {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyAdapter: DailyAdapter
    lateinit var binding: WeatherFragmentBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        fun newInstance() = WeatherFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = WeatherFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        if(!viewModel.checkPermission())
            requestLocationPermission()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        prepareLogic()
        enableNavigationButtons()


        // preparing the recycler views for the fragment
        val horizontalLayout = LinearLayoutManager(context)
        val verticalLayout = LinearLayoutManager(context)

        horizontalLayout.orientation = LinearLayoutManager.HORIZONTAL
        verticalLayout.orientation = LinearLayoutManager.VERTICAL

        binding.hourlyRecycleView.layoutManager = horizontalLayout
        binding.dailyRecyclerView.layoutManager = verticalLayout


        binding.dailyRecyclerView.isNestedScrollingEnabled = false


        hourlyAdapter = HourlyAdapter(mutableListOf(), viewModel,requireContext())
        dailyAdapter = DailyAdapter(mutableListOf(), viewModel, requireContext())

        binding.hourlyRecycleView.adapter = hourlyAdapter
        binding.dailyRecyclerView.adapter = dailyAdapter

    }

    // viewModel init and observers
    private fun prepareLogic() {
        observeViewModel()

        viewModel.getWeather(fusedLocationClient)
    }


    @SuppressLint("SimpleDateFormat")
    private fun observeViewModel() {

        viewModel.getCurrentWeatherLiveData.observe(viewLifecycleOwner, Observer {

            Log.i("TAG", "observeViewModel: fragment")


            val currentDateAndTime: String = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())
            Log.i("TAG", "observeViewModel: $currentDateAndTime")

            hourlyAdapter.setAdapterData(it.hourly)
            dailyAdapter.setAdapterData(it.daily)

            updateBlockingUi()
            //updateLocation(it.lat, it.lon)
            updateLocation(it.timezone,it.locality)
            updateCurrentDate(it.weatherState.dt)
            updateTemperature(it.weatherState)
            updateWeatherDetails(it.weatherState)


        })

        viewModel.getRequestPermissionLiveData.observe(viewLifecycleOwner, {
            requestLocationPermission()
        })

        // no local data and no internet
        viewModel.getInternetState.observe(viewLifecycleOwner,  {
            Toast.makeText(context, "no Internet Connection !!", Toast.LENGTH_SHORT).show()
            disableNavigationButtons()
//            updateBlockingUi()

        })

        viewModel.getLoading.observe(viewLifecycleOwner,  {

        })
    }

    @SuppressLint("CutPasteId", "SetTextI18n")
    private fun disableNavigationButtons() {
        (activity as? AppCompatActivity)?.findViewById<BottomNavigationView>(R.id.bottom_nav)?.menu?.findItem(R.id.favouriteFragment)?.isEnabled = false
        (activity as? AppCompatActivity)?.findViewById<BottomNavigationView>(R.id.bottom_nav)?.menu?.findItem(R.id.alertFragment)?.isEnabled = false
        (activity as? AppCompatActivity)?.findViewById<BottomNavigationView>(R.id.bottom_nav)?.menu?.findItem(R.id.settingsFragment)?.isEnabled = false

        binding.progressBarLoading.visibility = View.GONE
        binding.textViewLoading.text = "Internet Connection Needed"
        binding.textViewLoading.visibility = View.VISIBLE

    }

    @SuppressLint("CutPasteId")
    private fun enableNavigationButtons(){
        (activity as? AppCompatActivity)?.findViewById<BottomNavigationView>(R.id.bottom_nav)?.menu?.findItem(R.id.favouriteFragment)?.isEnabled = true
        (activity as? AppCompatActivity)?.findViewById<BottomNavigationView>(R.id.bottom_nav)?.menu?.findItem(R.id.alertFragment)?.isEnabled = true
        (activity as? AppCompatActivity)?.findViewById<BottomNavigationView>(R.id.bottom_nav)?.menu?.findItem(R.id.settingsFragment)?.isEnabled = true
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 1
        )
    }

    private fun updateBlockingUi() {
        binding.groupLoading.visibility = View.GONE
        binding.screenGroup.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun updateWeatherDetails(weatherState: WeatherState) {
        var unit = "meter/sec"
        if(viewModel.checkUnit())
            unit = "miles/hour"


        binding.windTextView.text = "${weatherState.windSpeed} $unit"
        binding.pressureTextView.text = "${weatherState.pressure} hPa"
        binding.humidityTextView.text = "${weatherState.humidity} %"
        binding.cloudsTextView.text = "${weatherState.clouds} %"
    }

    @SuppressLint("SetTextI18n")
    private fun updateTemperature(state: WeatherState) {
        binding.textViewTemperature.text = "${state.temp.toInt()}°"
        binding.textViewState.text = state.weather[0].description
    }

    private fun updateCurrentDate(dt: Long) {

        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = viewModel.getCurrentDate(dt)
    }

    private fun updateLocation(timeZone:String,locality: String?) {
//        val value = viewModel.getCityName(lat, lon)

        Log.i("TAG", "updateLocation lllllllllllllllllllllllll: $locality")
        if(locality != null){
            (activity as? AppCompatActivity)?.supportActionBar?.title = locality
        }else{
            (activity as? AppCompatActivity)?.supportActionBar?.title = timeZone

        }
//        val string:String = timezone.substring(timezone.indexOf("/", 0, true) + 1)

    }

}