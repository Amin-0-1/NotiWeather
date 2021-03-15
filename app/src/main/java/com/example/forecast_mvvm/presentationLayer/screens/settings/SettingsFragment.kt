package com.example.forecast_mvvm.presentationLayer.screens.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.forecast_mvvm.presentationLayer.other.MyMap
import com.example.forecast_mvvm.R
import com.example.forecast_mvvm.presentationLayer.other.MainActivity
import com.example.forecast_mvvm.utilities.ILanguage
import com.example.forecast_mvvm.utilities.SettingsSP

class SettingsFragment : PreferenceFragmentCompat() ,ILanguage{

    private lateinit var mPreferences:SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle= null
        mPreferences = requireContext().getSharedPreferences("location", Context.MODE_PRIVATE);

        val selected_theme: ListPreference? = preferenceManager.findPreference<Preference>("GPS") as ListPreference?
        selected_theme?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            Log.i("TAG", "onActivityCreated: gps")
            if(newValue.toString() != "GPS"){ // set location

                val intent = Intent(requireContext(), MyMap::class.java)
                intent.putExtra("state", "set")
                startActivity(intent)
                false


            }else {
                preference.key = newValue.toString()
                val sharedPref = requireContext().getSharedPreferences("location", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPref.edit()
                editor.clear()
                editor.apply()
                true
            }
        }


        val language: ListPreference? = preferenceManager.findPreference<Preference>("English") as ListPreference?
        language?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newVal ->
//            setLocale()
            Log.i("TAG", "onActivityCreated: $newVal")
            setLocale(newVal.toString(),resources)
            restartActivity()
            true
        }
    }

    private fun restartActivity() {
        requireActivity().finish();
        val refresh = Intent(context, MainActivity::class.java)
        startActivity(refresh)
    }

    override fun onResume() {
        super.onResume()
        Log.i("TAG", "onResume: ")
        if(mPreferences.getString("lon", "null")!="null" ){
            Log.i("TAG", "onActivityCreated: inside if")
            SettingsSP.setLocation()
        }
    }
}