package com.julian.jotihuntdidam.Activities;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.julian.jotihuntdidam.Logics.AppController;
import com.julian.jotihuntdidam.Logics.DataManager;
import com.julian.jotihuntdidam.R;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.wearable.view.DismissOverlayView;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends Activity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener {

    /**
     * Overlay that shows a short help text when first launched. It also provides an option to
     * exit the app.
     */
    private DismissOverlayView mDismissOverlay;

    /**
     * The map. It is initialized when the map has been fully loaded and is ready to be used.
     *
     * @see #onMapReady(com.google.android.gms.maps.GoogleMap)
     */
    private GoogleMap mMap;
    private HashMap<Integer, Marker> mHashMap = new HashMap<Integer, Marker>();
    int gpsupdate = 5000;

    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        // Set the layout. It only contains a MapFragment and a DismissOverlay.
        setContentView(R.layout.activity_maps);

        // Retrieve the containers for the root of the layout and the map. Margins will need to be
        // set on them to account for the system window insets.
        final FrameLayout topFrameLayout = (FrameLayout) findViewById(R.id.root_container);
        final FrameLayout mapFrameLayout = (FrameLayout) findViewById(R.id.map_container);

        // Set the system view insets on the containers when they become available.
        topFrameLayout.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                // Call through to super implementation and apply insets
                insets = topFrameLayout.onApplyWindowInsets(insets);

                FrameLayout.LayoutParams params =
                        (FrameLayout.LayoutParams) mapFrameLayout.getLayoutParams();

                // Add Wearable insets to FrameLayout container holding map as margins
                params.setMargins(
                        insets.getSystemWindowInsetLeft(),
                        insets.getSystemWindowInsetTop(),
                        insets.getSystemWindowInsetRight(),
                        insets.getSystemWindowInsetBottom());
                mapFrameLayout.setLayoutParams(params);

                return insets;
            }
        });

        // Obtain the DismissOverlayView and display the introductory help text.
        mDismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        mDismissOverlay.setIntroText(R.string.intro_text);
        mDismissOverlay.showIntroIfNecessary();

        // Obtain the MapFragment and set the async listener to be notified when the map is ready.
        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Map is ready to be used.
        mMap = googleMap;

        // Set the long click listener as a way to exit the map.
        mMap.setOnMapLongClickListener(this);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(52.062504, 5.921974))      // Sets the center of the map to Mountain View
                .zoom(9)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        // Display the dismiss overlay with a button to exit this activity.
        mDismissOverlay.show();
    }



    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                while(!thread.isInterrupted()) {
                    sleep(gpsupdate);
                    final String server_ip = getString(R.string.server_ip);
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
                                    if (!mHashMap.containsKey(markerid)) {
                                        Marker marker = mMap.addMarker(m);
                                        mHashMap.put(markerid, marker);
                                        Log.i("marker", "isnt in hashmap");
                                    } else {
                                        Marker existingMarker = mHashMap.get(markerid);
                                        animateMarker(existingMarker, new LatLng(result.getDouble("latitude"), result.getDouble("longitude")), false);
                                        existingMarker.setSnippet(result.getString("createdAt"));
                                        Log.i("marker", "is in hashmap");
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
                            Toast.makeText(getApplicationContext(), "Server connection failed", Toast.LENGTH_LONG).show();
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    };

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
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
}
