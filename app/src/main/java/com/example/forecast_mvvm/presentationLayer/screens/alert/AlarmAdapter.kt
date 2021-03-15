package com.example.forecast_mvvm.presentationLayer.screens.alert

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.example.forecast_mvvm.R
import com.example.forecast_mvvm.dataLayer.local.response.Alarm


class AlarmAdapter(
    private var alarmList: MutableList<Alarm>,
    private var viewModel: AlertViewModel,
    private var context: Context
): RecyclerView.Adapter<AlarmAdapter.MyViewHolder>() {

    fun setAdapterData(alarms: List<Alarm>) {
        alarmList.clear()
        alarmList.addAll(alarms)
        notifyDataSetChanged()

    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val alarmType: TextView = itemView.findViewById(R.id.alarm_type)
        val dateText: TextView = itemView.findViewById(R.id.date_text)
        val timeText:TextView = itemView.findViewById(R.id.time_text)
        val deleteBtn:ImageView = itemView.findViewById(R.id.delete_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.alarm_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.alarmType.text = alarmList[position].title
        holder.dateText.text = alarmList[position].date
        holder.timeText.text = alarmList[position].time

        holder.deleteBtn.setOnClickListener{

            val alert = AlertDialog.Builder(context)
            alert.setTitle("Delete Alarm")
            alert.setMessage("Are you sure ?")
            alert.setPositiveButton(android.R.string.yes, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val id = alarmList[position].id
                    WorkManager.getInstance().cancelAllWorkByTag("$id")
                    viewModel.deleteAlarm(id)
                    Toast.makeText(context,"Alarm deleted Successfully",Toast.LENGTH_SHORT).show()
                }
            })
            alert.setNegativeButton(
                android.R.string.no
            ) { dialog, which -> // close dialog
                dialog.cancel()
            }
            alert.show()
        }
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

}
