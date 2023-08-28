package com.example.sms.Services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.sms.R;

public class AlarmService extends Service {
    public AlarmService() {
    }

    @Override
    public void onCreate() {
        Toast.makeText(this,"Создан сервис",Toast.LENGTH_SHORT).show();

        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Notify");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentText("SmsService");
        builder.setContentTitle("Started");
        builder.setOngoing(true);

        Notification notify = builder.build();
            startForeground(5,notify);
        AlarmManager alarmManager = getSystemService(AlarmManager.class);
        PendingIntent p = PendingIntent.getBroadcast(this,0,new Intent("Alarm"),PendingIntent.FLAG_IMMUTABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if(alarmManager.canScheduleExactAlarms()) {
                Log.d("AlarmManager","Rabotaet");
                //TODO time
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10*1000, p);
            }
        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}