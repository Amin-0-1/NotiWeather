package com.example.forecast_mvvm.screens.settings

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.forecast_mvvm.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle= null

        val selected_theme: ListPreference? = preferenceManager.findPreference<Preference>("GPS") as ListPreference?
        selected_theme?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                preference.key = newValue.toString()
            if(newValue.toString() != "GPS"){
                // TODO: 3/1/2021 open map and choose location
                Log.i("TAG", "onActivityCreated: ")

            }
                true


            }
    }
}