package com.example.forecast_mvvm.presentationLayer.screens.favourite.bottomSheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast_mvvm.R
import com.example.forecast_mvvm.dataLayer.entities.models.WeatherState
import com.example.forecast_mvvm.presentationLayer.screens.favourite.FavouriteViewModel
import com.squareup.picasso.Picasso

class FavBsHourlyAdapter(
    private var hourlyList: MutableList<WeatherState>,
    private var viewModel: FavouriteViewModel,
): RecyclerView.Adapter<FavBsHourlyAdapter.MyViewHolder>(){


    fun setAdapterData(models: List<WeatherState>) {
        hourlyList.clear()
        hourlyList.addAll(models)
        notifyDataSetChanged()
    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var time: TextView = itemView.findViewById(R.id.textViewTime)
        var temp: TextView = itemView.findViewById(R.id.textViewTemp)
        var image: ImageView = itemView.findViewById(R.id.tempImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavBsHourlyAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.fav_hourly_item, parent, false)
        return FavBsHourlyAdapter.MyViewHolder(view)
    }


    override fun getItemCount(): Int {
        return hourlyList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = hourlyList[position]

        if(viewModel.isNetworkAvailable()){
            val urlImage = "http://openweathermap.org/img/wn/${hourlyList.get(position).weather[0].icon}@2x.png"
            Picasso.get().load(urlImage).into(holder.image)
        }

        val hour = viewModel.extractTime(item.dt)

        holder.time.text = if (position == 0) "Now" else hour

        holder.temp.text = item.temp.toInt().toString()
    }

}