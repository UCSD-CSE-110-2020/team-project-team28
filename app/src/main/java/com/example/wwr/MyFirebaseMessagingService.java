package com.example.wwr;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.opencensus.internal.Utils;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("OnMessage", "Received");
        super.onMessageReceived(remoteMessage);
        Log.d("tag", "From " + remoteMessage.getFrom());
        Log.d("tag", "Body " + remoteMessage.getNotification().getBody());

        Context context = getApplicationContext();
        //Toast.makeText(MainActivity.this, remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();

                //Here you can update your UI
            }
        });


    }



}