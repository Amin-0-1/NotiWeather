package com.example.forecast_mvvm.screens.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast_mvvm.R
import com.example.forecast_mvvm.dataLayer.entities.models.Daily
import com.squareup.picasso.Picasso

class DailyAdapter(
    private var dailyList: MutableList<Daily>,
    private var viewModel: WeatherViewModel
): RecyclerView.Adapter<DailyAdapter.MyViewHolder>() {

    fun setAdapterData(daily: List<Daily>) {
        dailyList.addAll(daily)
        notifyDataSetChanged()
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)  {
        val day: TextView = itemView.findViewById(R.id.dayTextView)
        val stateImage:ImageView = itemView.findViewById(R.id.stateImageView)
        val maxTemp:TextView = itemView.findViewById(R.id.maxTemp)
        val minTemp:TextView = itemView.findViewById(R.id.minTemp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.daily_item, parent, false)
        return DailyAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = dailyList[position]

        val day = viewModel.exactDay(item.dt)
        holder.day.text = day

        holder.maxTemp.text = item.temp.max.toString()
        holder.minTemp.text = item.temp.min.toString()

        val urlImage = "http://openweathermap.org/img/wn/${dailyList.get(position).weather[0].icon}@2x.png"
        Picasso.get().load(urlImage).into(holder.stateImage)

    }

    override fun getItemCount(): Int {
        return dailyList.size
    }


}
