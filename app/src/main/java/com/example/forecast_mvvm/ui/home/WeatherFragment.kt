package com.example.forecast_mvvm.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forecast_mvvm.databinding.WeatherFragmentBinding

class WeatherFragment : Fragment() {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var hourlyAdapter: HourlyAdapter

    lateinit var binding: WeatherFragmentBinding
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
        // TODO: Use the ViewModel

        prepareLogic()

        val linearLayoutManager:LinearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recycleView.setLayoutManager(linearLayoutManager)
        hourlyAdapter = HourlyAdapter(mutableListOf())
        binding.recycleView.setAdapter(hourlyAdapter)
    }






    // viewModel init and observers
    private fun prepareLogic() {
        observeViewModel()
        viewModel.getWeather()
    }
    private fun observeViewModel() {


        viewModel.getCurrentWeatherLiveData().observe(viewLifecycleOwner, Observer {
//            binding.textView.text = it.weatherState.temp.toString()

            hourlyAdapter.setAdapterData(it.hourly)

        })

        viewModel.getErrorState().observe(viewLifecycleOwner, Observer {

        })

        viewModel.getLoading().observe(viewLifecycleOwner, Observer {

        })
    }
}