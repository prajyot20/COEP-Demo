package com.artifex.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.artifex.mupdfdemo.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMTestService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle(remoteMessage.getNotification().getTitle());
        mBuilder.setContentText(remoteMessage.getNotification().getBody());

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(14, mBuilder.build());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification = new Notification();
        startForeground(12, notification);
        Log.d(TAG, "FCMTestService started");

       /* handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                android.support.v7.app.NotificationCompat.Builder mBuilder = new android.support.v7.app.NotificationCompat.Builder(FCMTestService.this);
                mBuilder.setContentTitle("Sample notification");
                mBuilder.setSmallIcon(R.drawable.sample);
                mBuilder.setContentText("Its cool");

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // notificationID allows you to update the notification later on.
                mNotificationManager.notify(14, mBuilder.build());

            }
        }, 5000);*/
    }

    Handler handler = new Handler() ;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "FCMTestService destroyed");
    }
}
