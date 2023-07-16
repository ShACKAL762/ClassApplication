package com.example.sms.Sevices;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.Random;

public class SmsSendService extends Service {

    volatile boolean serviceWork;
    int i;
    public SmsSendService() {
    }

    @Override
    public void onCreate() {
        Log.e("OnCreate", "Create");
        serviceWork = true;
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.e("onDestoy","service stop");
        serviceWork = false;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        stopSelf();
        super.onDestroy();
        Log.e("d","d");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println(++i);
        String number = "404";
        String date = "403";
        String time = "402";
        Random random = new Random();
        if(intent.getStringExtra("number") != null && intent.getStringExtra("date") != null && intent.getStringExtra("time") != null) {
            number = intent.getStringExtra("number");
            date = intent.getStringExtra("date");
            time = intent.getStringExtra("time");
        }


        getSystemService(SmsManager.class).sendTextMessage(
                number,
                null,
                "test " + date + " " + time,
                PendingIntent.getBroadcast(this, random.nextInt(), new Intent("DeliveryIntent").putExtra("number",number), PendingIntent.FLAG_IMMUTABLE ),
                PendingIntent.getBroadcast(this, random.nextInt(), new Intent("SentIntent").putExtra("number",number), PendingIntent.FLAG_IMMUTABLE ));


            Log.d("OnStartCommand", "starting");






        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {


        throw new UnsupportedOperationException("Not yet implemented");
    }
}