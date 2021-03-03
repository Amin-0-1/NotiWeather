package com.example.forecast_mvvm.screens.favourite

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView
import com.example.forecast_mvvm.R

class FavouriteAdapter(
        private var favouriteList: MutableList<String>,
        private var viewModel: FavouriteViewModel
): RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>() {


    fun setAdapterData(favourite: List<String>) {
        favouriteList.clear()
        favouriteList.addAll(favourite)
        notifyDataSetChanged()
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)  {
        val fav_name: TextView = itemView.findViewById(R.id.fav_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.favourite_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.fav_name.text = favouriteList[position]
        holder.itemView.setOnClickListener(View.OnClickListener {
            Log.i("TAG", "clicked: ${favouriteList[position]}")
        })
    }

    override fun getItemCount(): Int {
        return favouriteList.size
    }
}