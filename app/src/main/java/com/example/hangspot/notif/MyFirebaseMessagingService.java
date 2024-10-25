package com.example.hangspot.notif;

import android.app.Service;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("token", "onNewToken: "+token);
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d("FCM", "onMessageReceived: "+ message.getNotification().getBody());
        Log.d("FCM", "onMessageReceived: "+ message.getNotification().getBody());
        Log.d("FCM", "onMessageReceived: "+ message.getNotification().getBody());
    }
}
