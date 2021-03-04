package com.example.forecast_mvvm.screens.favourite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forecast_mvvm.MyMap
import com.example.forecast_mvvm.databinding.FavouriteFragmentBinding

class FavouriteFragment : Fragment() {

    lateinit var binding: FavouriteFragmentBinding
    private lateinit var viewModel: FavouriteViewModel
    private lateinit var favouriteAdapter: FavouriteAdapter


    companion object {
        fun newInstance() = FavouriteFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavouriteFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.favouriteLocations()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle= null

        prepareLogic()
        prepareFavouriteRecyclerView()

        binding.floatingActionButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(requireContext(),MyMap::class.java)
            intent.putExtra("state","fav")
            startActivity(intent)
            // save cooord in db
        })

    }

    private fun prepareFavouriteRecyclerView(){
        val horizontalLayout = LinearLayoutManager(context)
        horizontalLayout.orientation = LinearLayoutManager.VERTICAL
        binding.favouriteRecycler.layoutManager = horizontalLayout
//        favouriteAdapter = FavouriteAdapter(listOf("mahmoud","amin","muhammed","salma","samy") as MutableList<String>, viewModel)
        favouriteAdapter = FavouriteAdapter(mutableListOf(), viewModel)
        binding.favouriteRecycler.adapter = favouriteAdapter
    }

    private fun prepareLogic() {
        observeViewModel()

        viewModel.favouriteLocations()
    }

    private fun observeViewModel() {
//        viewModel.getNullLocations().observe(viewLifecycleOwner, Observer {
//            viewModel.updateLocaility(it)
//        })
        viewModel.getLocations().observe(viewLifecycleOwner, Observer {
//            viewModel.
            Log.i("TAG", "observeViewModel: $it")
            favouriteAdapter.setAdapterData(it)
        })
        viewModel.getMessage().observe(viewLifecycleOwner, Observer {
            if(it == true){
                Toast.makeText(requireContext(),"Deleted Successfully",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"Data Will be Available once you're connected to Internet",Toast.LENGTH_SHORT).show()
            }
        })
    }

}