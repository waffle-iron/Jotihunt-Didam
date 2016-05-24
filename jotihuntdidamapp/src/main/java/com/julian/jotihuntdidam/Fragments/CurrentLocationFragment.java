package com.julian.jotihuntdidam.Fragments;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.maps.android.kml.KmlLayer;
import com.julian.jotihuntdidam.Logics.AppController;
import com.julian.jotihuntdidam.Logics.DataManager;
import com.julian.jotihuntdidam.Logics.LocationService;
import com.julian.jotihuntdidam.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CurrentLocationFragment extends Fragment{

    private SupportMapFragment mSupportMapFragment;
    private GoogleMap map;
    private HashMap<Integer, Marker> mHashMap = new HashMap<Integer, Marker>();
    private boolean receiveLocation;
    private Context context;
    Intent sendlocationservice;


    int gpsupdate = 5000;
    int animationupdate = 1500;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_location,
                container, false);
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startFragment();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            // We've been denied once before. Explain why we need the permission, then ask again.
            getActivity().showDialog(123);
        } else {
            // We've never asked. Just do it.
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }

        return view;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        startFragment();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                123);
                    }
                }
            }
        }
    }

    private void startFragment() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        receiveLocation = sharedPref.getBoolean("enable_gps_receive", true);
        boolean sendLocation = sharedPref.getBoolean("enable_gps", false);
        Log.d("test", "test");
        if (sendLocation) {
            Intent sendlocationservice = new Intent(getContext(), LocationService.class);
            getActivity().startService(sendlocationservice);
            Log.d("Location_service", "GPS is Sending");
        } else {
            if (sendlocationservice != null)
            {
                getActivity().stopService(sendlocationservice);
            }
        }
            thread.start();
        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapwhere);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapwhere, mSupportMapFragment).commitAllowingStateLoss();
        }
        startMap();
    }


    public void startMap () {
        mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (googleMap != null) {
                    googleMap.getUiSettings().setAllGesturesEnabled(true);
                    try {
                        context = getActivity();
                        KmlLayer kmlLayerarea = new KmlLayer(googleMap, R.raw.jotihunt2015_deelgebieden, getContext());
                        KmlLayer kmlLayerpoint = new KmlLayer(googleMap, R.raw.jotihunt2015_edit, getContext());
                        kmlLayerarea.addLayerToMap();
                        kmlLayerpoint.addLayerToMap();
                        moveCamera(googleMap);
                        map = googleMap;
                    } catch (IOException | XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void moveCamera(GoogleMap googleMap) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(52.062504, 5.921974))      // Sets the center of the map to Mountain View
                .zoom(9)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    int id = 01;


    public void showNotification() {
        Notification.Builder mBuilder =
                new Notification.Builder(getContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Jotihunt Didam")
                        .setContentText("Coördinaten worden verstuurd")
                        .setOngoing(true);
        Intent resultIntent = new Intent(getActivity(), CurrentLocationFragment.class);
// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
// Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }




    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                    while (receiveLocation) {
                        sleep(gpsupdate);
                        final String server_ip = context.getString(R.string.server_ip);
                        String tag_json_obj = "json_obj_req";
                        String url = server_ip + "allcoords";
                        final String TAG = "GetCoord";
                        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG + "Good", response);
                                try {
                                    //Do it with this it will work
                                    JSONObject coordsobject = new JSONObject(response);
                                    JSONArray coordsarray = coordsobject.getJSONArray("coords");
                                    Log.d("JSON MAP", coordsarray.toString());


                                    int numberofitems = coordsarray.length();
                                    for (int i = 0; i < numberofitems; i++) {
                                        JSONObject result = coordsarray.getJSONObject(i);
                                        int markerid = result.getInt("id");
                                        MarkerOptions m = new MarkerOptions()
                                                .title(result.getString("name"))
                                                .snippet(result.getString("createdAt"))
                                                .position(new LatLng(result.getDouble("latitude"), result.getDouble("longitude")));
                                        m.icon(BitmapDescriptorFactory.fromResource(R.drawable.carmarker));
                                        if (!mHashMap.containsKey(markerid)) {
                                            Marker marker = map.addMarker(m);
                                            mHashMap.put(markerid, marker);
                                        } else {
                                            Marker existingMarker = mHashMap.get(markerid);
                                            animateMarker(existingMarker, new LatLng(result.getDouble("latitude"), result.getDouble("longitude")), false);
                                            existingMarker.setSnippet(result.getString("createdAt"));
                                        }
                                        Log.d("Google Map Marker", result.getString("name") + result.getDouble("latitude"));
                                        Log.d("Google Map Hashmap", mHashMap.toString());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d("Error bolean", DataManager.getError().toString());
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG + "error 1", "Error: " + error.getMessage());
                                Log.d(TAG + "error 2", "" + error.getMessage() + "," + error.toString());
                                Toast.makeText(getContext(), "Server connection failed", Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                Log.d("Input ", params.toString());
                                return params;
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> headers = new HashMap<>();
                                headers.put("Content-Type", "application/x-www-form-urlencoded");
                                headers.put("Auth", DataManager.getApi_key());
                                return headers;
                            }
                        };

                        // Adding request to request queue
                        AppController.getInstance().addToRequestQueue(sr, tag_json_obj);
                    }
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
    };

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = map.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 1500;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    private static final String STRING_KEY = "com.example.key.count";

    private GoogleApiClient mGoogleApiClient;


    // Create a data map and put data in it
    private void sendApiKey() {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/apikey");
        putDataMapReq.getDataMap().putString(STRING_KEY, DataManager.getApi_key());
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
    }

}
