package com.example.forecast_mvvm.screens.alert

import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.forecast_mvvm.databinding.AlertDialogBinding
import com.example.forecast_mvvm.utilities.MyDatePicker
import com.example.forecast_mvvm.utilities.MyTimePicker
import java.text.MessageFormat
import java.util.*
import java.util.concurrent.TimeUnit


class Dialogx(private val viewModel: AlertViewModel) : DialogFragment(){

    lateinit var binding:AlertDialogBinding
    lateinit var calender:Calendar
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = AlertDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calender = Calendar.getInstance()
        updatePickerDates()

        Log.i("TAG", "onViewCreated: ${calender.timeInMillis}")
        binding.calenderBtn.setOnClickListener{
            if(! this::calender.isInitialized)
                calender = Calendar.getInstance()

            val dialogFragment: DialogFragment = MyDatePicker(myDateSetListener)
            dialogFragment.show(parentFragmentManager, "datepicker")
        }
        binding.timeBtn.setOnClickListener{
            if(! this::calender.isInitialized)
                calender = Calendar.getInstance()

            val dialogFragment = MyTimePicker(myTimeSetListener)
            dialogFragment.show(parentFragmentManager, "timepicker")
        }


        // TODO: 3/9/2021 work manager 
        
        binding.button2.setOnClickListener{
            if(this::calender.isInitialized){

                if(viewModel.getCurrentLat() != null && viewModel.getCurrentLon() != null){

                    val id = viewModel.insertAlarm(viewModel.getCurrentLat()!!,
                        viewModel.getCurrentLon()!!,
                        binding.repeatingSpinner.selectedItem.toString(),
                        getCalendarDate(),
                        getCalendarTime())

                    initWorkManager(id)
                    this.dismiss()
                    Toast.makeText(context,"Alarm has been set",Toast.LENGTH_SHORT).show()
                }
            }

            else
                Toast.makeText(context,"Please select alarm time...",Toast.LENGTH_LONG).show()
        }
    }

    var myTimeSetListener = OnTimeSetListener { view, hourOfDay, minute ->
        calender[Calendar.HOUR_OF_DAY] = hourOfDay
        calender[Calendar.MINUTE] = minute
        calender[Calendar.SECOND] = 0

        updatePickerDates()
        Log.i("time", "${DateFormat.format("hh:mm aaa", calender.getTime())}")
    }

    var myDateSetListener = OnDateSetListener{ view, year, month, dayOfMonth->
        calender[Calendar.YEAR] = year
        calender[Calendar.MONTH] = month
        calender[Calendar.DAY_OF_MONTH] = dayOfMonth
        calender[Calendar.SECOND] = 0
        updatePickerDates()
        Log.i("time", "$dayOfMonth : $month : $year ")
    }

    private fun initWorkManager(id: Long) {
        /* val myConstraints = Constraints.Builder()
        .setRequiresDeviceIdle(true) //checks whether device should be idle for the WorkRequest to run
        .setRequiresCharging(true) //checks whether device should be charging for the WorkRequest to run
        .setRequiredNetworkType(NetworkType.CONNECTED) //checks whether device should have Network Connection
        .setRequiresBatteryNotLow(true) // checks whether device battery should have a specific level to run the work request
        .setRequiresStorageNotLow(true) // checks whether device storage should have a specific level to run the work request
        .build()*/

        Log.i("TAG", "initWorkManager:xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx $id")
        val data = workDataOf("id" to id)
        val oneTimeRequest = OneTimeWorkRequestBuilder<Worker>()
            .setInitialDelay(calender.timeInMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag("$id")
            .build()
        WorkManager.getInstance(requireContext()).enqueue(oneTimeRequest)
        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(oneTimeRequest.id)
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it != null) {
                    Toast.makeText(requireContext(), "Work was success: ${it.state}", Toast.LENGTH_LONG).show()
                    Log.i("TAG", "iniWorkManager: ${it.state}")

                }
            })

//        WorkManager.getInstance().cancelAllWorkByTag("$id")
        // WorkManager.cancelWorkById(oneTimeRequest.id)

    }

    private fun updatePickerDates() {
        binding.timeTextView.text = MessageFormat.format("{0}", getCalendarTime())
        binding.dateTextView.text = MessageFormat.format("{0}", getCalendarDate() )
    }

    private fun getCalendarTime(): String {
        return DateFormat.format("hh:mm aaa", calender.getTime()).toString()
    }

    private fun getCalendarDate(): String {
        return "${calender.getTime().getDate()}/${calender.getTime().getMonth() + 1}/${calender.getTime().getYear() + 1900}"
    }

}