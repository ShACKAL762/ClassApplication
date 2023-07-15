package com.example.sms.Other.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

public class Channels {
    public Channels(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        CharSequence nameOfChannel = "Уведомления дерьма";
        String id = "Notify";
        NotificationChannel channel = new NotificationChannel(id, nameOfChannel, NotificationManager.IMPORTANCE_DEFAULT );
        channel.setDescription("Тута всё");
            //TODO
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
