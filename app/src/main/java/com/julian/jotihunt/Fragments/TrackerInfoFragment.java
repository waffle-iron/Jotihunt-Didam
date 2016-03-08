package com.julian.jotihunt.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.julian.jotihunt.R;



public class TrackerInfoFragment extends Fragment{


    TextView currentime;
    TextView currentbattery;
    TextView currentspeed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracker_information,
                container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Tracker Informatie");
        SharedPreferences prefs = getContext().getSharedPreferences("settings",
                Context.MODE_PRIVATE);

        String speed = prefs.getString("speed", "0.0");
        String currenttime = prefs.getString("currenttime", "0.0");
        String batterylife = prefs.getString("batterylife", "0%");

        // Now we display formattedDate value in TextView
        currentime  =(TextView)view.findViewById(R.id.currentTime);
        currentime.setText(currenttime);
        currentime.setTextSize(20);

        currentbattery  =(TextView)view.findViewById(R.id.currentBattery);
        currentbattery.setText(batterylife);
        currentbattery.setTextSize(20);

        currentspeed = (TextView)view.findViewById(R.id.currentSpeed);
        currentspeed.setText(speed);
        currentspeed.setTextSize(20);

        ProgressBar pb = (ProgressBar) view.findViewById(R.id.BatteryLevelProgress);
        batterylife = batterylife.replace("%", "");

        int batterylifeint = Integer.parseInt(batterylife);
        pb.setProgress(batterylifeint);


        //progress is between 0 and 100 so set level of drawable to progress * 100
        Drawable batteryProgressD = pb.getProgressDrawable();
        batteryProgressD.setLevel(batterylifeint*100);
        pb.setProgress(batterylifeint);

        return view;
    }

}

