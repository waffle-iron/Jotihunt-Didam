package com.julian.jotihuntdidam.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.julian.jotihuntdidam.R;


public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about,
                container, false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Over de app");
        return view;
    }
}
