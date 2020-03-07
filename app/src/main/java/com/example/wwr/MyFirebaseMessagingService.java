package com.example.wwr;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        need to implement this if you want to do something when you receive a notification while app is in the foreground.
        // Check if message contains a data payload.
    /*  if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage);

            Intent broadcast = new Intent();
            broadcast.setAction("OPEN_NEW_ACTIVITY");
            sendBroadcast(broadcast);
            Toast.makeText(getApplicationContext(), "UHHHHHHH", Toast.LENGTH_LONG).show();
        } */
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param remoteMessage FCM message message received.
     *
    private void handleNotification(RemoteMessage remoteMessage) {
        // replace myactivity with accept/decline page
        Intent intent = new Intent(this, MyFirebaseMessagingService.class);

        Map<String, String> hmap ;
        hmap = remoteMessage.getData();
        hmap.get("data_info");
        intent.putExtra("data_info", hmap.get("data_info"));
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

    } */
}
