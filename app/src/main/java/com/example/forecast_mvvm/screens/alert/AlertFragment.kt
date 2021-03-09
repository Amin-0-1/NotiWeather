package com.example.forecast_mvvm.screens.alert

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.forecast_mvvm.R
import com.example.forecast_mvvm.databinding.AlertFragmentBinding
import java.util.zip.Inflater

class AlertFragment : Fragment() {

    lateinit var binding:AlertFragmentBinding
    companion object {
        fun newInstance() = AlertFragment()
    }

    private lateinit var viewModel: AlertViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AlertFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener{
            val dialog = Dialogx()
            dialog.show(childFragmentManager,"dialog")
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AlertViewModel::class.java)
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle= null
    }

}