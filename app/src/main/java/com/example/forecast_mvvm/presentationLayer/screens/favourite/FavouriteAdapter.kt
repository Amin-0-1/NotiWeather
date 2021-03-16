package com.example.forecast_mvvm.presentationLayer.screens.favourite

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast_mvvm.R
import com.example.forecast_mvvm.dataLayer.local.response.FavouriteCoordination
import com.example.forecast_mvvm.presentationLayer.screens.favourite.bottomSheet.BottomSheetFavourite

class FavouriteAdapter(
    private var favouriteList: MutableList<FavouriteCoordination>,
    private var viewModel: FavouriteViewModel,
    private var context: Context
): RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>() {

    fun setAdapterData(favourite: List<FavouriteCoordination>) {
        favouriteList.clear()
        favouriteList.addAll(favourite)
        notifyDataSetChanged()

    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)  {
        val fav_name: TextView = itemView.findViewById(R.id.alarm_type)
        val deleteBtn:ImageView  = itemView.findViewById(R.id.delete_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.favourite_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if(favouriteList[position].title != null){
            holder.fav_name.text = favouriteList[position].title
        }else{
            holder.fav_name.text = "loading..."
        }


        setOnClickFunctions(holder, position)

    }

    private fun setOnClickFunctions(holder: MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {

            val bottomSheetDialogFragment = BottomSheetFavourite()
            val bundle = Bundle()
            bundle.putDouble("lat", favouriteList[position].lat)
            bundle.putDouble("lon", favouriteList[position].lon)
            bundle.putString("placeTitle",favouriteList[position].title)

            bottomSheetDialogFragment.arguments = bundle
            bottomSheetDialogFragment.show(
                (context as FragmentActivity).supportFragmentManager,
                "bottomUpSheet"
            )
        }

        holder.deleteBtn.setOnClickListener {

            val alert = AlertDialog.Builder(context)
            alert.setTitle("Delete Favourite Location")
            alert.setMessage("Are you sure ?")
            alert.setPositiveButton(android.R.string.yes, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    Log.i("alert", "onClick: delete from view model")
                    viewModel.deleteFavourite(
                        favouriteList[position].lat,
                        favouriteList[position].lon
                    )
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
        return favouriteList.size
    }
}