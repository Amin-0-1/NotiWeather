package com.example.forecast_mvvm.screens.favourite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.forecast_mvvm.MyMap
import com.example.forecast_mvvm.databinding.FavouriteFragmentBinding

class FavouriteFragment : Fragment() {

    lateinit var binding: FavouriteFragmentBinding
    private lateinit var viewModel: FavouriteViewModel

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle= null
        viewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)

        binding.floatingActionButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(requireContext(),MyMap::class.java)
            intent.putExtra("state","fav")
            startActivity(intent)

        })


    }

}