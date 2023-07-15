package com.example.sms.Other;

import android.app.PendingIntent;
import android.content.Intent;

public class Sender extends Thread {
    Intent intent;
    PendingIntent penIntent;
    Sender(Intent intent, PendingIntent penIntent){
        this.intent = intent;
        this.penIntent = penIntent;
    }

    @Override
    public void run() {
        /*intent.putExtra("numbers", numbers[i]);
        SmsManager.getDefault().sendTextMessage(
                numbers[i],
                null,
                "smser",
                pSent, null);*/
    }
}
