package com.julian.jotihunt.Logics;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class CallApi {

    private static final String TAG = "LoginActivity";

    public void apiLogin() {

        String tag_json_obj = "json_obj_req";
        String url = "http://145.116.203.82/api/v1/login";


        StringRequest sr = new StringRequest(Request.Method.POST, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG + "good", response.toString());
                try {
                    //Do it with this it will work
                    JSONObject login = new JSONObject(response);
                    DataManager.setError(login.getBoolean("error"));
                    DataManager.setMessage(login.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG + "error 1", "Error: " + error.getMessage());
                Log.d(TAG + "error 2", ""+error.getMessage()+","+error.toString());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", DataManager.getMail());
                params.put("password", DataManager.getPassword());
                Log.d("Input ", params.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(sr, tag_json_obj);
    }
}
