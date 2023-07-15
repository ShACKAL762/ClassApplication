package com.example.sms.Other;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Intents {
    private Intent sentIntent;
    private Intent deliver;
    private PendingIntent sent;
    private PendingIntent delivered;

    public PendingIntent getSent() {
        return sent;
    }

    public PendingIntent getDelivered() {
        return delivered;
    }

    public Intents(Context context) {

        sentIntent = new Intent("Sending");
        deliver = new Intent("Delivered");

        sent = PendingIntent.getBroadcast(context,0,sentIntent,PendingIntent.FLAG_IMMUTABLE);
        delivered = PendingIntent.getBroadcast(context,0,deliver,PendingIntent.FLAG_IMMUTABLE);
    }
}

