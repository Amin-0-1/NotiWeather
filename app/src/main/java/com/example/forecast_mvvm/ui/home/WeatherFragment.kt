package com.example.forecast_mvvm.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.forecast_mvvm.databinding.WeatherFragmentBinding

class WeatherFragment : Fragment() {

    private lateinit var viewModel: WeatherViewModel

    lateinit var binding: WeatherFragmentBinding
    companion object {
        fun newInstance() = WeatherFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = WeatherFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        // TODO: Use the ViewModel

        prepareLogic()
    }






    // viewModel init and observers
    private fun prepareLogic() {
        observeViewModel()

        viewModel.getWeather() // api call
//        viewModel.getWeather().observe(viewLifecycleOwner, Observer {
//            binding.txt.text = it.weatherState.temp.toString()
//        })
    }
    private fun observeViewModel() {


        viewModel.getCurrentWeatherLiveData().observe(viewLifecycleOwner, Observer {
            binding.txt.text = it.weatherState.temp.toString()
        })





        viewModel.getErrorState().observe(viewLifecycleOwner, Observer {

        })

        viewModel.getLoading().observe(viewLifecycleOwner, Observer {

        })
    }
}