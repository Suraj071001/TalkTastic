package com.example.android.talktastic.Utilities;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

public class NotificationUtils {

    private static final String CHANNEL_ID = "message_notification_channel";

    public static void createNotification(Context context,String number){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"Message Channel",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }
}
