package com.julian.jotihuntdidam.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.julian.jotihuntdidam.R;

public class GPSFragment extends PreferenceFragmentCompat {



    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        // Define the settings file to use by this settings fragment
        getPreferenceManager().setSharedPreferencesName("settings");
        setPreferencesFromResource(R.xml.gps_preferences, rootKey);
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        // Set a Toolbar to replace the ActionBar.

        android.support.v7.preference.Preference gpsInterval = findPreference("gps_interval");
        if (gpsInterval != null) {
            gpsInterval.setSummary(sharedPref.getString(
                    "gps_interval", "")
                    + 5000);
        }
        android.support.v7.preference.Preference animationTime = findPreference("animation_time");
        if (animationTime != null) {
            animationTime.setSummary(sharedPref.getString(
                    "animation_gps", "")
                    + 1500);
        }

        CheckBoxPreference enable_gps = (CheckBoxPreference)findPreference("enable_gps");
        enable_gps.setOnPreferenceChangeListener(new  android.support.v7.preference.Preference.OnPreferenceChangeListener()
        {

            @Override
            public boolean onPreferenceChange(android.support.v7.preference.Preference preference, Object newValue)
            {
                Log.d("Setting", "User set always run to " + newValue.toString().equals("true"));
                String enable_gps = newValue.toString();
                Boolean boolean1 = Boolean.valueOf(enable_gps);
                sharedPref.edit().putBoolean("enable_gps", boolean1).commit();
                return true;
            }
        });

        CheckBoxPreference enable_gps_receive = (CheckBoxPreference)findPreference("enable_gps_receive");
        enable_gps_receive.setOnPreferenceChangeListener(new  android.support.v7.preference.Preference.OnPreferenceChangeListener()
        {

            @Override
            public boolean onPreferenceChange(android.support.v7.preference.Preference preference, Object newValue)
            {
                Log.d("Setting", "User set always run to " + newValue.toString().equals("true"));
                String enable_gps_receive = newValue.toString();
                Boolean boolean1 = Boolean.valueOf(enable_gps_receive);
                sharedPref.edit().putBoolean("enable_gps_receive", boolean1).commit();
                return true;
            }
        });


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);

         // Set the default white background in the view so as to avoid transparency
        view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.background_material_light));


    }



}