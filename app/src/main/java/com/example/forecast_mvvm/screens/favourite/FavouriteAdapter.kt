package com.example.forecast_mvvm.screens.favourite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast_mvvm.R
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteCoordination
import com.example.forecast_mvvm.screens.favourite.bottomSheet.BottomSheetFavourite


class FavouriteAdapter(
    private var favouriteList: MutableList<FavouriteCoordination>,
    private var viewModel: FavouriteViewModel
): RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>() {

    private lateinit var context:Context

    fun setAdapterData(favourite: List<FavouriteCoordination>,context: Context) {
        favouriteList.clear()
        favouriteList.addAll(favourite)
        notifyDataSetChanged()

        this.context = context
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

        setOnClickFunctions(holder, position)

    }

    private fun setOnClickFunctions(holder: MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {

            val bottomSheetDialogFragment = BottomSheetFavourite()
            val bundle = Bundle()
            bundle.putDouble("lat", favouriteList[position].lat)
            bundle.putDouble("lon", favouriteList[position].lon)
            bottomSheetDialogFragment.arguments = bundle
            bottomSheetDialogFragment.show((context as FragmentActivity).supportFragmentManager,"bottomUpSheet")
//            val intent = Intent(context,Details::class.java)
//            intent.putExtra("lat",favouriteList[position].lat)
//            intent.putExtra("lon",favouriteList[position].lon)
//
//            context.startActivity(intent)


        }

        holder.deleteBtn.setOnClickListener {
            viewModel.deleteFavourite(favouriteList[position].lat, favouriteList[position].lon)
        }
    }

    override fun getItemCount(): Int {
        return favouriteList.size
    }
}