package com.julian.jotihuntdidam.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.julian.jotihuntdidam.R;

public class MyPreferenceFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.gps_preferences);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // load user preferences

            Preference gpsInterval = findPreference("gps_interval");
            if (gpsInterval != null) {
                gpsInterval.setSummary(SP.getString(
                        "gps_interval", "")
                        + 5000);
            }
            Preference animationTime = findPreference("animation_time");
            if (animationTime != null) {
                animationTime.setSummary(SP.getString(
                        "animation_gps", "")
                        + 1500);
            }


    }
}