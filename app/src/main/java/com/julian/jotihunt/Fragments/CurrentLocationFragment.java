package com.julian.jotihunt.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.geometry.Point;
import com.google.maps.android.kml.KmlContainer;
import com.google.maps.android.kml.KmlLayer;
import com.google.maps.android.kml.KmlPlacemark;
import com.google.maps.android.kml.KmlPoint;
import com.google.maps.android.kml.KmlPolygon;
import com.julian.jotihunt.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class CurrentLocationFragment extends Fragment {

    private SupportMapFragment mSupportMapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_location,
                container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Huidige Locatie");


        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapwhere);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapwhere, mSupportMapFragment).commit();
        }
        startMap();
        return view;
    }

    public void startMap () {
        mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (googleMap != null) {
                    googleMap.getUiSettings().setAllGesturesEnabled(true);
                    try {
                        KmlLayer kmlLayerarea = new KmlLayer(googleMap, R.raw.jotihunt2015_deelgebieden, getContext());
                        KmlLayer kmlLayerpoint = new KmlLayer(googleMap, R.raw.jotihunt2015_edit, getContext());
                        kmlLayerarea.addLayerToMap();
                        kmlLayerpoint.addLayerToMap();
                        moveCameraToKml(kmlLayerarea);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void moveCameraToKml(KmlLayer kmlLayer) {
        KmlContainer document = kmlLayer.getContainers().iterator().next();
        //Retrieve a nested container within the first container
        KmlContainer folder = document.getContainers().iterator().next();
        //Retrieve the first placemark in the nested container
        KmlPlacemark placemark = folder.getPlacemarks().iterator().next();
        //Retrieve a polygon object in a placemark
        KmlPolygon polygon = (KmlPolygon) placemark.getGeometry();
        //Create LatLngBounds of the outer coordinates of the polygon
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : polygon.getOuterBoundaryCoordinates()) {
            builder.include(latLng);
        }
        mSupportMapFragment.getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 1));

    }

}
