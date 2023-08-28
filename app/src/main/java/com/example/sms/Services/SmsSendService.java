package com.example.sms.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.sms.Other.SmsSender.SendSmsToTomorrowStudents;
import com.example.sms.R;

import java.util.Random;

public class SmsSendService extends Service {


    volatile boolean serviceWork = true;
    int i;

    public SmsSendService() {
    }

    @Override
    public void onCreate() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Notify");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentText("Но это не точно");
        builder.setContentTitle("Рассылка включена");
        builder.setOngoing(true);

        Toast.makeText(this, "Создан сервис", Toast.LENGTH_SHORT).show();
        Log.e("OnCreate", "Create");

        startForeground(100, builder.build());
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.e("onDestoy", "service stop");
        serviceWork = false;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        stopSelf();
        super.onDestroy();
        Log.e("d", "d");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new SendSmsToTomorrowStudents(this).start();


        Log.d("OnStartCommand", "starting");


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {


        throw new UnsupportedOperationException("Not yet implemented");
    }
}