package com.example.sms;


import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sms.Other.JSONS.Token;
import com.example.sms.Other.SendSms;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void smsSend(View v){
        EditText editText = (EditText)findViewById(R.id.Token);
        String token = editText.getText().toString();



        Token.intent = new Intent(MainActivity.this, MainActivity2.class);
        Token.context = getApplicationContext();
        SendSms sendSms = new SendSms();
        sendSms.start();
        PendingIntent pendingIntent = Token.pendingIntent;
        System.out.println(123);

             SmsManager.getDefault().sendTextMessage("000000",null, "smser",null, pendingIntent);

    }
    private void ThreadJoin(Thread thread){
        try {
            thread.join(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}