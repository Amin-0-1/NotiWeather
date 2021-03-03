package com.example.forecast_mvvm.screens.favourite

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.forecast_mvvm.R
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteCoordination

class FavouriteAdapter(
        private var favouriteList: MutableList<FavouriteCoordination>,
        private var viewModel: FavouriteViewModel
): RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>() {


    fun setAdapterData(favourite: List<FavouriteCoordination>) {
        favouriteList.clear()
        favouriteList.addAll(favourite)
        notifyDataSetChanged()
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)  {
        val fav_name: TextView = itemView.findViewById(R.id.fav_title)
        val deleteBtn:ImageView  = itemView.findViewById(R.id.delete_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.favourite_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.fav_name.text = favouriteList[position].title

        setOnClickFunctions(holder,position)

    }

    private fun setOnClickFunctions(holder: MyViewHolder,position: Int) {
        holder.itemView.setOnClickListener(View.OnClickListener {
            Log.i("TAG", "clicked: ${favouriteList[position]}")
        })

        holder.deleteBtn.setOnClickListener(View.OnClickListener {
            viewModel.deleteFavourite(favouriteList[position].lat,favouriteList[position].lon)
        })
    }

    override fun getItemCount(): Int {
        return favouriteList.size
    }
}