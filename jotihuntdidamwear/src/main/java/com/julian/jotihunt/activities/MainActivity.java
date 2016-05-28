package com.julian.jotihunt.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.julian.jotihunt.R;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("wear", "Loaded mainactivity");
    }
}
