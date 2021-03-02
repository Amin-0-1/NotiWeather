package com.example.forecast_mvvm.screens.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast_mvvm.R
import com.example.forecast_mvvm.dataLayer.entities.models.WeatherState
import com.squareup.picasso.Picasso

class HourlyAdapter(
    private var hourlyList: MutableList<WeatherState>,
    private var viewModel: WeatherViewModel
) : RecyclerView.Adapter<HourlyAdapter.MyViewHolder>(){

    fun setAdapterData(models: List<WeatherState>) {
        hourlyList.addAll(models)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var time: TextView = itemView.findViewById(R.id.textViewTime)
        var temp: TextView = itemView.findViewById(R.id.textViewTemp)
        var image: ImageView = itemView.findViewById(R.id.tempImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.hourly_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //todo:: get the exact time like 12am .. 1pm .....
        val item = hourlyList[position]

        val urlImage = "http://openweathermap.org/img/wn/${hourlyList.get(position).weather[0].icon}@2x.png"

        Picasso.get().load(urlImage).into(holder.image)

        val hour = viewModel.extractTime(item.dt)

        holder.time.text = if (position == 0) "Now" else hour

        holder.temp.text = item.temp.toInt().toString()
    }

    override fun getItemCount(): Int {
        return hourlyList.size
    }
}