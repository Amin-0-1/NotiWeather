package com.example.forecast_mvvm.presentationLayer.screens.favourite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forecast_mvvm.presentationLayer.other.MyMap
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        prepareFavouriteRecyclerView()
        prepareLogic()




        binding.floatingActionButton.setOnClickListener {
           if(viewModel.isNetworkAvailable()){
                val intent = Intent(requireActivity().applicationContext, MyMap::class.java)
                intent.putExtra("state", "fav")
                startActivity(intent)
            }else{
                Toast.makeText(
                    requireContext(),
                    "please Connect to The Internet",
                    Toast.LENGTH_LONG
                ).show()
           }

            // save cooord in db
        }

//        binding.swipe.setOnRefreshListener {
//            viewModel.favouriteLocations()
//            binding.swipe.isRefreshing=false
//        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.subtitle= null


    }

    private fun prepareFavouriteRecyclerView(){
        val horizontalLayout = LinearLayoutManager(context)
        horizontalLayout.orientation = LinearLayoutManager.VERTICAL
        binding.favouriteRecycler.layoutManager = horizontalLayout
        favouriteAdapter = FavouriteAdapter(mutableListOf(), viewModel, requireContext())
        binding.favouriteRecycler.adapter = favouriteAdapter
    }

    private fun prepareLogic() {
        observeViewModel()

        viewModel.favouriteLocations()
    }

    private fun observeViewModel() {
        val deleteToast = Toast.makeText(
            requireContext(),
            "Deleted Successfully",
            Toast.LENGTH_SHORT
        )
        val willBeToast = Toast.makeText(
            requireContext(),
            "Data Will be Available once you're connected to Internet",
            Toast.LENGTH_SHORT
        )

        viewModel.getNotNullFavouriteLocation().observe(viewLifecycleOwner, {
//
            Log.i("TAG", "observeViewModel: ${it.size}")
            favouriteAdapter.setAdapterData(it)
        })
        viewModel.getMessage().observe(viewLifecycleOwner, {

//            if (deleteToast.view?.isShown == true)
//                deleteToast.cancel()
//
//            if (willBeToast.view?.isShown == true)
//                willBeToast.cancel()

            if (it == true) {
                deleteToast.show()
            } else {
                Log.i("TAG", "observeViewModel: will be avail")
                willBeToast.show()
            }
        })
    }
}