package com.example.sms.Other;

import static com.example.sms.Other.JSONS.Token.context;
import static com.example.sms.Other.JSONS.Token.intent;

import android.app.PendingIntent;

import com.example.sms.Other.JSONS.Token;

public class SendSms extends Thread {
    @Override
    public void run() {
        Token.pendingIntent = PendingIntent.getActivity(context,0, intent,PendingIntent.FLAG_IMMUTABLE);
    }




}
