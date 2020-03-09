package com.example.wwr;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getName();

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