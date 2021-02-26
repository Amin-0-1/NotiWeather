package com.example.forecast_mvvm.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast_mvvm.R
import com.example.forecast_mvvm.dataLayer.entities.WeatherState
import kotlinx.android.synthetic.main.hourly_item.view.*

class HourlyAdapter(private var hourlyList: MutableList<WeatherState>) : RecyclerView.Adapter<HourlyAdapter.MyViewHolder>(){


    fun setAdapterData(models: List<WeatherState>) {
        hourlyList.addAll(models)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.hourly_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task: WeatherState = hourlyList.get(position)
        holder.itemView.textView2.text = hourlyList.get(position).temp.toString()
        holder.itemView.textView3.text = hourlyList.get(position).dt.toString()

    }

    override fun getItemCount(): Int {
        return hourlyList.size
    }
}