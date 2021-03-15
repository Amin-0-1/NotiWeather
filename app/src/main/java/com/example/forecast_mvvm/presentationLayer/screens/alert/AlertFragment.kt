package com.example.forecast_mvvm.presentationLayer.screens.alert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forecast_mvvm.databinding.AlertFragmentBinding


class AlertFragment : Fragment() {

    lateinit var binding:AlertFragmentBinding
    private lateinit var alarmAdapter: AlarmAdapter

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
        viewModel = ViewModelProvider(this).get(AlertViewModel::class.java)

        prepareRecyclerView()
        binding.floatingActionButton.setOnClickListener{
            val dialog = Dialogx(viewModel)
            dialog.show(childFragmentManager, "dialog")
        }

        viewModel.getAllAlarms().observe(viewLifecycleOwner, Observer {
            alarmAdapter.setAdapterData(it)
        })


    }

    private fun prepareRecyclerView() {


        val horizontalLayout = LinearLayoutManager(context)
        horizontalLayout.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = horizontalLayout
        alarmAdapter = AlarmAdapter(mutableListOf(), viewModel, requireContext())
        binding.recyclerView.adapter = alarmAdapter
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle= null
    }

}