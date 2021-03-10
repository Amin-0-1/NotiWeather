package com.example.forecast_mvvm.screens.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forecast_mvvm.R
import com.example.forecast_mvvm.dataLayer.entities.models.WeatherState
import com.example.forecast_mvvm.dataLayer.local.LocalDataSource
import com.example.forecast_mvvm.databinding.WeatherFragmentBinding
import com.google.android.gms.location.*
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
//        viewModel.locationPermission(requireActivity(),fusedLocationClient)
        prepareLogic()


        // preparing the recycler views for the fragment
        val horizontalLayout = LinearLayoutManager(context)
        val verticalLayout = LinearLayoutManager(context)

        horizontalLayout.orientation = LinearLayoutManager.HORIZONTAL
        verticalLayout.orientation = LinearLayoutManager.VERTICAL

        binding.hourlyRecycleView.layoutManager = horizontalLayout
        binding.dailyRecyclerView.layoutManager = verticalLayout

        hourlyAdapter = HourlyAdapter(mutableListOf(), viewModel,requireContext())
        dailyAdapter = DailyAdapter(mutableListOf(), viewModel, requireContext())

        binding.hourlyRecycleView.adapter = hourlyAdapter
        binding.dailyRecyclerView.adapter = dailyAdapter

    }

    // viewModel init and observers
    private fun prepareLogic() {
        observeViewModel()

        viewModel.getWeather(requireActivity(), fusedLocationClient)
    }


    @SuppressLint("SimpleDateFormat")
    private fun observeViewModel() {

        viewModel.getCurrentWeatherLiveData().observe(viewLifecycleOwner, Observer {

            Log.i("TAG", "observeViewModel: fragment")


            val currentDateAndTime: String = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())
            Log.i("TAG", "observeViewModel: $currentDateAndTime")

            hourlyAdapter.setAdapterData(it.hourly)
            dailyAdapter.setAdapterData(it.daily)

            updateBlockingUi()
            //updateLocation(it.lat, it.lon)
            updateLocation(it.locality)
            updateCurrentDate(it.weatherState.dt)
            updateTemperature(it.weatherState)
            updateWeatherDetails(it.weatherState)


        })

        viewModel.getErrorState().observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "no Internet Connection !!", Toast.LENGTH_SHORT).show()
            updateBlockingUi()

        })

        viewModel.getLoading().observe(viewLifecycleOwner, Observer {

        })
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

    private fun updateLocation(locality: String?) {
//        val value = viewModel.getCityName(lat, lon)

        Log.i("TAG", "updateLocation lllllllllllllllllllllllll: $locality")
        if(locality != "null"){
            (activity as? AppCompatActivity)?.supportActionBar?.title = locality
        }else{
            (activity as? AppCompatActivity)?.supportActionBar?.title = resources.getString(R.string.loading)

        }
//        val string:String = timezone.substring(timezone.indexOf("/", 0, true) + 1)

    }

}