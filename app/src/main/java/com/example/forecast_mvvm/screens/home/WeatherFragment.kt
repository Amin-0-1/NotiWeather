package com.example.forecast_mvvm.screens.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forecast_mvvm.dataLayer.entities.WeatherState
import com.example.forecast_mvvm.databinding.WeatherFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        binding.screenGroup.visibility = View.GONE

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
//        viewModel.locationPermission(requireActivity(),fusedLocationClient)
        prepareLogic()


        // preparing the recycler views for the fragment
        val horizontalLayout:LinearLayoutManager = LinearLayoutManager(context)
        val verticalLayout:LinearLayoutManager = LinearLayoutManager(context)

        horizontalLayout.orientation = LinearLayoutManager.HORIZONTAL
        verticalLayout.orientation = LinearLayoutManager.VERTICAL

        binding.hourlyRecycleView.layoutManager = horizontalLayout
        binding.dailyRecyclerView.layoutManager = verticalLayout

        hourlyAdapter = HourlyAdapter(mutableListOf(), viewModel)
        dailyAdapter = DailyAdapter(mutableListOf(), viewModel)

        binding.hourlyRecycleView.adapter = hourlyAdapter
        binding.dailyRecyclerView.adapter = dailyAdapter

    }


    // viewModel init and observers
    private fun prepareLogic() {
        observeViewModel()
        viewModel.getWeather()
//        viewModel.getWeather(requireActivity(),fusedLocationClient)
    }

    @SuppressLint("SimpleDateFormat")
    private fun observeViewModel() {

        viewModel.getCurrentWeatherLiveData().observe(viewLifecycleOwner, Observer {

//            val it = viewModel.checkWeatherStateDateValidation(it)
//            if (it === null) {
//                Log.i("time", "observeViewModel: null time stamp")
//            }else{
//                hourlyAdapter.setAdapterData(it.hourly)
//                dailyAdapter.setAdapterData(it.daily)
//
//                binding.groupLoading.visibility = View.GONE
//                binding.screenGroup.visibility = View.VISIBLE
//                updateLocation(it.timezone)
//                updateCurrentDate(it.weatherState.dt)
//                updateTemperature(it.weatherState)
//                updateWeatherDetails(it.weatherState)
//            }


            val currentDateAndTime: String = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())
            Log.i("TAG", "observeViewModel: $currentDateAndTime")

            hourlyAdapter.setAdapterData(it.hourly)
            dailyAdapter.setAdapterData(it.daily)

            binding.groupLoading.visibility = View.GONE
            binding.screenGroup.visibility = View.VISIBLE
            updateLocation(it.timezone)
            updateCurrentDate(it.weatherState.dt)
            updateTemperature(it.weatherState)
            updateWeatherDetails(it.weatherState)


        })

        viewModel.getErrorState().observe(viewLifecycleOwner, Observer {

        })

        viewModel.getLoading().observe(viewLifecycleOwner, Observer {

        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateWeatherDetails(weatherState: WeatherState) {
        binding.windTextView.text = "${weatherState.windSpeed} meter/sec"
        binding.pressureTextView.text = "${weatherState.pressure} hPa"
        binding.humidityTextView.text = "${weatherState.humidity} %"
        binding.cloudsTextView.text = "${weatherState.clouds} %"
    }

    @SuppressLint("SetTextI18n")
    private fun updateTemperature(state: WeatherState) {
        binding.textViewTemperature.text = "${state.temp.toInt().toString()}°"
        binding.textViewState.text = state.weather[0].description
    }

    private fun updateCurrentDate(dt: Long) {

        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = viewModel.getCurrentDate(dt)
    }

    private fun updateLocation(timezone: String) {
        val string:String = timezone.substring(timezone.indexOf("/", 0, true) + 1)
        (activity as? AppCompatActivity)?.supportActionBar?.title = string
    }
}