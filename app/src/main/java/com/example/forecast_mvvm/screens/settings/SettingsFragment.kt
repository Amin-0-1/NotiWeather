package com.example.forecast_mvvm.screens.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.forecast_mvvm.MyMap
import com.example.forecast_mvvm.R

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var mPreferences:SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        mPreferences = requireContext().getSharedPreferences("location", Context.MODE_PRIVATE);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle= null

        val selected_theme: ListPreference? = preferenceManager.findPreference<Preference>("GPS") as ListPreference?
        selected_theme?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                preference.key = newValue.toString()
            if(newValue.toString() != "GPS"){
                val intent = Intent(requireContext(),MyMap::class.java)
                intent.putExtra("state","set")
                startActivity(intent)
                Log.i("TAG", "onActivityCreated settings frag: ${mPreferences.getString("lat",null)}")
                if(mPreferences.getString("lat","null")==null ) false

            }else if(newValue.toString() == "GPS"){
                val sharedPref = requireContext().getSharedPreferences("location", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPref.edit()
                editor.clear()
                editor.apply()
            }
                true


        }
    }
}