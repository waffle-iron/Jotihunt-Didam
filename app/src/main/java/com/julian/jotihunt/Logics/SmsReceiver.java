package com.julian.jotihunt.Logics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.julian.jotihunt.Activities.MainActivity;
import com.julian.jotihunt.Fragments.CurrentLocationFragment;

import java.util.ArrayList;

public class SmsReceiver extends BroadcastReceiver {
    private String TAG = SmsReceiver.class.getSimpleName();
    String sender;
    String gpsnumber;

    String batterylife;
    String speed;
    String currenttime;

    Boolean savehistory;
    double latitude;
    double longitude;

    private ArrayList<LatLng> latlngs = new ArrayList<>();
    ArrayList userList   = new ArrayList();

    //private static final String CARD_API = "http://192.168.2.8/CardApi/";
    private static final String CARD_API = "http://82.176.182.161/";





    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the data (SMS data) and initiates the bundle to save everything in
        Bundle bundle = intent.getExtras();
        SharedPreferences prefs = context.getSharedPreferences("settings",
                Context.MODE_PRIVATE);
        SmsMessage[] msgs;
        String str = "";
        if (bundle != null) {
            // Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            // For every SMS message received
            for (int i=0; i < msgs.length; i++) {
                // Convert Object array
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                // Sender's phone number
                str += "SMS from " + msgs[i].getOriginatingAddress() + " : ";
                sender = msgs[i].getOriginatingAddress();
                // Fetch the text message
                str += "\n";
                // Newline <img src="http://codetheory.in/wp-includes/images/smilies/simple-smile.png" alt=":-)" class="wp-smiley" style="height: 1em; max-height: 1em;">
                str += msgs[i].getMessageBody().toString();
            }

            // Initiates the shared prefs, and checks if the number given in the options is the
            // same as the number received from the sms.


            gpsnumber = prefs.getString("gps_number", "");
            savehistory = prefs.getBoolean("save_history", false);
            Log.d(TAG, str);
            Log.d("Current number", gpsnumber);
            Log.d("Current sender", sender);
            if (PhoneNumberUtils.compare(gpsnumber, sender)) {

                // Pars the whole sms
                MessageParser SMSParser = new MessageParser(str);
                String result = SMSParser.parseLatitude() + ", " + SMSParser.parseLongitude();
                Log.d("parsed SMS", result);

                // Put the data in variables
                latitude = SMSParser.parseLatitudeDouble();
                longitude = SMSParser.parseLongitudeDouble();
                batterylife = SMSParser.parseBatteryLife();
                speed = SMSParser.parseSpeed();
                currenttime = SMSParser.parseCurrentTime();

                String result2 = SMSParser.parseLatitudeDouble() + ", " + SMSParser.parseLongitudeDouble();
                Log.d("Parsed doubles = ", result2);
                Log.d("Parsed batterylife = ", batterylife);
                Log.d("Parsed speed = ", speed);
                Log.d("Parsed currenttime = ", currenttime);

                if (savehistory) {
                    userList.add(new LatLng(latitude, longitude));
                }


//Set the values
                Gson gson = new Gson();

                String json = gson.toJson(latlngs);

                // Send to API
                String urlString = CARD_API +String.format("?action=add&lat=%s&lon=%s",latitude,longitude);
                (new CallApiGPS(context) {
                    @Override
                    protected void onPostExecute(String result) {

                    }
                }).execute(urlString, "GET");


                // Save data from sms in sharedprefs
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("arraylist", json);
                editor.putString("batterylife", batterylife);
                editor.putString("speed", speed);
                editor.putString("currenttime", currenttime);
                editor.putString("latitude", SMSParser.parseLatitude());
                editor.putString("longitude", SMSParser.parseLongitude());
                editor.apply();



                CurrentLocationFragment fragment = new CurrentLocationFragment ();

                // Passes the data to new intent
                Intent i = new Intent(context,MainActivity.class);
                i.putExtra("currenttime", currenttime);
                i.putExtra("latitude", latitude);
                i.putExtra("longitude", longitude);
                bundle.putDouble("latitude", latitude);
                bundle.putDouble("Longitude", longitude);
                fragment.setArguments(bundle);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

        }
    }


}
