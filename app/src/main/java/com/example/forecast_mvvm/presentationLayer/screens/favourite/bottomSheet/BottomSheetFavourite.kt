package com.example.forecast_mvvm.presentationLayer.screens.favourite.bottomSheet

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteCoordination
import com.example.forecast_mvvm.databinding.FavWeatherFragmentBinding
import com.example.forecast_mvvm.presentationLayer.screens.favourite.FavouriteViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetFavourite: BottomSheetDialogFragment() {


    private lateinit var viewModel: FavouriteViewModel
    private lateinit var hourlyAdapter: FavBsHourlyAdapter
    private lateinit var dailyAdapter: FavBsDailyAdapter
    lateinit var binding: FavWeatherFragmentBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavWeatherFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lat = arguments?.getDouble("lat")!!
        val lon = arguments?.getDouble("lon")!!

        Log.i("track", "onViewCreated: ${lat} lon: $lon")

        viewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        viewModel.getFavouriteItemDetails(FavouriteCoordination(lat,lon))

        // preparing the recycler views for the fragment
        val horizontalLayout = LinearLayoutManager(context)
        val verticalLayout = LinearLayoutManager(context)

        horizontalLayout.orientation = LinearLayoutManager.HORIZONTAL
        verticalLayout.orientation = LinearLayoutManager.VERTICAL

        binding.hourlyRecycleView.layoutManager = horizontalLayout
        binding.dailyRecyclerView.layoutManager = verticalLayout

        hourlyAdapter = FavBsHourlyAdapter(mutableListOf(),viewModel)
        dailyAdapter = FavBsDailyAdapter(mutableListOf(),viewModel)

        binding.hourlyRecycleView.adapter = hourlyAdapter
        binding.dailyRecyclerView.adapter = dailyAdapter

        viewModel.getLocalFavouriteItemDetails(lat,lon).observe(viewLifecycleOwner,
            Observer {
                hourlyAdapter.setAdapterData(it.hourly)
                dailyAdapter.setAdapterData(it.daily)

                binding.textViewTemperature.text = it.weatherState.temp.toString()
                binding.cloudsTextView.text = it.weatherState.clouds.toString()
                binding.humidityTextView.text = it.weatherState.humidity.toString()
                binding.pressureTextView.text = it.weatherState.pressure.toString()
                binding.windTextView.text = it.weatherState.windSpeed.toString()
                binding.textViewState.text = it.weatherState.weather[0].description
            })

    }
}