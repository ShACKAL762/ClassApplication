package com.example.sms.Other;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

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
            Log.d("OnStartCommand", "starting");
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    while(serviceWork){
                        System.out.println(++i);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    /*for (int i = 3; i > 0; i--) {

                        getSystemService(SmsManager.class).sendTextMessage(
                                "89779482492",
                                null,
                                "test",
                                null,
                                null);
                        try {
                            TimeUnit.SECONDS.sleep(10);
                            System.out.println(i);
                        } catch (InterruptedException e) {
                            throw new
                            RuntimeException(e);
                        }
                    }*/
                }
            };
        Thread thread = new Thread(runnable);
            thread.start();




        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {


        throw new UnsupportedOperationException("Not yet implemented");
    }
}