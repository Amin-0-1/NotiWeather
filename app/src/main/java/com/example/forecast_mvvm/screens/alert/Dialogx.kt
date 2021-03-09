package com.example.forecast_mvvm.screens.alert

import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
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


class Dialogx: DialogFragment(){

    lateinit var binding:AlertDialogBinding
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

        updateTextWithTodayDate()
        binding.calenderBtn.setOnClickListener{
            val dialogFragment: DialogFragment = MyDatePicker(myDateSetListener)
            dialogFragment.show(parentFragmentManager, "datepicker")
        }
        binding.timeBtn.setOnClickListener{
            val dialogFragment = MyTimePicker(myTimeSetListener)
            dialogFragment.show(parentFragmentManager, "timepicker")
        }


        // TODO: 3/9/2021 work manager 
        
        binding.button2.setOnClickListener{
            initWorkManager()
        }
    }

    private fun initWorkManager() {
        /* val myConstraints = Constraints.Builder()
        .setRequiresDeviceIdle(true) //checks whether device should be idle for the WorkRequest to run
        .setRequiresCharging(true) //checks whether device should be charging for the WorkRequest to run
        .setRequiredNetworkType(NetworkType.CONNECTED) //checks whether device should have Network Connection
        .setRequiresBatteryNotLow(true) // checks whether device battery should have a specific level to run the work request
        .setRequiresStorageNotLow(true) // checks whether device storage should have a specific level to run the work request
        .build()


    */

        /* val current=System.currentTimeMillis()
         val fut=1615234503957

         */

        val name = workDataOf(
            "type" to "Clouds",
            "lat" to 37.422,
            "lng" to -122.084
        )
        var oneTimeRequest = OneTimeWorkRequestBuilder<Worker>()
            .setInitialDelay(2, TimeUnit.SECONDS)
            .setInputData(name)
            .build()

        //  37.422001
        //-122.0840133         Clouds

        //30.0444196          Clear
        //31.2357116



        WorkManager.getInstance(requireContext()).enqueue(oneTimeRequest)


        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(oneTimeRequest.id)
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it != null ) {
                    Toast.makeText(requireContext(), "Work was success: ${it.state}", Toast.LENGTH_LONG).show()
                    Log.i("TAG", "iniWorkManager: ${it.state}")

                }
            })


        // WorkManager.cancelWorkById(oneTimeRequest.id)

    }

    private fun updateTextWithTodayDate() {
        val calender = Calendar.getInstance()
        calender.setTimeInMillis(System.currentTimeMillis())
        binding.timeTextView.text = MessageFormat.format("{0}:{1}", calender.getTime().getHours(), calender.getTime().getMinutes())
        binding.dateTextView.text = MessageFormat.format("{0}/{1}/{2}", calender.getTime().getDate(), calender.getTime().getMonth() + 1, calender.getTime().getYear() + 1900)
    }


    var myTimeSetListener = OnTimeSetListener { view, hourOfDay, minute ->
        Log.i("time", "$hourOfDay:$minute")
    }

    var myDateSetListener = OnDateSetListener{ view, year, month, dayOfMonth->
        Log.i("time", "$dayOfMonth : $month : $year ")
    }

}