package com.example.sms.Services;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.example.sms.MainActivity;
import com.example.sms.R;

import java.util.Random;

public class Sender extends Service {
    public Sender() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String number = intent.getStringExtra("Number");
        String date = intent.getStringExtra("Date");
        String time = intent.getStringExtra("Time");
        Random random = new Random();

        Log.e("!!!", number + " : " + date + " : " + time);
        SharedPreferences preferences = getSharedPreferences("main",MODE_PRIVATE);
        String message = (preferences.getString("Saved_Massage", ""));
                message = message.replace("%date", date);
                message = message.replace("%time", time);
        Log.e("Message" , message);
        /*getSystemService(SmsManager.class).sendTextMessage(
                number,
                null,
                "Ждем вас "+ date +" в " + time + " в Purple Cup. По адресу ул.Туристская 33к1.",
                PendingIntent.getBroadcast(this, random.nextInt(), new Intent("SentIntent").putExtra("Number",number), PendingIntent.FLAG_IMMUTABLE ),
                PendingIntent.getBroadcast(this, random.nextInt(), new Intent("DeliveryIntent").putExtra("Number",number), PendingIntent.FLAG_IMMUTABLE ));
*/
        

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}