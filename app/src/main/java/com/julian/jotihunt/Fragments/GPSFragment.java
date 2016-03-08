package com.julian.jotihunt.Fragments;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceFragmentCompat;

import android.view.View;

import com.julian.jotihunt.R;

public class GPSFragment extends PreferenceFragmentCompat {



    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        // Define the settings file to use by this settings fragment
        getPreferenceManager().setSharedPreferencesName("settings");
        setPreferencesFromResource(R.xml.gps_preferences, rootKey);

        // Set a Toolbar to replace the ActionBar.

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);

         // Set the default white background in the view so as to avoid transparency
        view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.background_material_light));


    }


}