package com.julian.jotihunt.Fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.julian.jotihunt.R;

public class MyPreferenceFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.gps_preferences);
    }
}