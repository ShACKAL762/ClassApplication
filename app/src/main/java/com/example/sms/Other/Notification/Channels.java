package com.example.sms.Other.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

public class Channels {
    public Channels(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        CharSequence nameOfChannel1 = "Общие уведомления ";
        CharSequence nameOfChannel2 = "Общие уведомления ";
        String firstChannelId = "Notify";
        String secondChannelId = "ForeverNotify";
        NotificationChannel channel = new NotificationChannel(firstChannelId, nameOfChannel1, NotificationManager.IMPORTANCE_DEFAULT );
        NotificationChannel channel2 = new NotificationChannel(secondChannelId, nameOfChannel2, NotificationManager.IMPORTANCE_DEFAULT );
        channel.setDescription("Тута всё");

            //TODO
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.createNotificationChannel(channel2);
        }
    }
}
