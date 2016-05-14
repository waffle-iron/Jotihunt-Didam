package com.julian.jotihuntdidam.Logics;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.julian.jotihuntdidam.Activities.MapsActivity;

public class ListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if (messageEvent.getPath().equals("/message_path")) {
            final String message = new String(messageEvent.getData());
            // Broadcast message to wearable activity for display
            DataManager.setApi_key(message);
            Log.d("WEAR Watch", message);
            Intent i = new Intent(this, MapsActivity.class);
            i.putExtra("apikey", message);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else {
            super.onMessageReceived(messageEvent);
            Log.d("WEAR Watch", "failed");
        }
    }
}
