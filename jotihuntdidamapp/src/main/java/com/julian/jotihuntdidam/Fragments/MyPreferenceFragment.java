package com.julian.jotihuntdidam.Fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.julian.jotihuntdidam.R;

public class MyPreferenceFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.gps_preferences);
    }
}