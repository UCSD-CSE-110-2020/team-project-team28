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
    // Creates appropriate notifications within the app.
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Received");
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From " + remoteMessage.getFrom());
        Log.d(TAG, "Body " + remoteMessage.getNotification().getBody());

        Context context = getApplicationContext();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}