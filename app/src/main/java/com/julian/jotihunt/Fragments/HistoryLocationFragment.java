package com.julian.jotihunt.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.julian.jotihunt.Logics.CallApiGPS;
import com.julian.jotihunt.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HistoryLocationFragment extends Fragment {
    SupportMapFragment mSupportMapFragment;
    String currenttime;
    String gpsnumber;
    private static final LatLng MIDDELBURG = new LatLng(51.4987962, 3.610998);
    private ArrayList<LatLng> latlngs = new ArrayList<>();


    private MarkerOptions options = new MarkerOptions();

    //private static final String CARD_API = "http://192.168.2.8/CardApi/";
    private static final String CARD_API = "http://82.176.182.161/";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_location,
                container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Locatie Geschiedenis");

        final SharedPreferences prefs = getContext().getSharedPreferences("settings",
                Context.MODE_PRIVATE);
        // Initiates Google Map
        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapwhere);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapwhere, mSupportMapFragment).commit();
        }

        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    if (googleMap != null) {
                        googleMap.getUiSettings().setAllGesturesEnabled(true);

                        // Send to API
                        String urlString = CARD_API +String.format("?action=get_coordinates&limit=50");
                        (new CallApiGPS(getContext()) {
                            @Override
                            protected void onPostExecute(String result) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String status = jsonObject.getString("status");
                                    if (status.equals("ok")) {
                                        //okay
                                        JSONArray coordinates = jsonObject.getJSONObject("data").getJSONArray("coordinates");
                                        for (int i=0; i<coordinates.length(); i++) {
                                            String latitude = coordinates.getJSONObject(i).getString("lat");
                                            String longitude = coordinates.getJSONObject(i).getString("lon");

                                            double latitudedouble = Double.parseDouble(latitude);
                                            double longitudedouble = Double.parseDouble(longitude);
                                            Log.d("History ", latitude + longitude);

                                            latlngs.add(new LatLng(latitudedouble, longitudedouble));

                                            for (LatLng point : latlngs) {
                                                options.position(point);
                                                options.title("someTitle");
                                                options.snippet("someDesc");
                                                googleMap.addMarker(options);
                                            }
                                        }
                                    } else {
                                        //error
                                    }
                                } catch (JSONException e) {}
                            }
                        }).execute(urlString, "GET");






                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(MIDDELBURG)
                                .zoom(10.0f)
                                .build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        googleMap.moveCamera(cameraUpdate);
                    }

                }

            });
        }
        return view;
    }

}
