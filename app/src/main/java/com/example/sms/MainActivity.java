package com.example.sms;


import static android.Manifest.permission.SEND_SMS;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.sms.Other.Notification.Channels;
import com.example.sms.Other.Token.GetToken;
import com.example.sms.Other.Token.Token;
import com.example.sms.Services.AlarmService;
import com.example.sms.Services.SmsSendService;

import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver sentReceiver = null;
    private BroadcastReceiver deliveryReceiver = null;
    private BroadcastReceiver alarmSmsReceiver = null;
    final public Context context = this;

    volatile boolean smsWorker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load();
        receivers();
        ActivityCompat.requestPermissions(
                this,
                new String[]{SEND_SMS, NOTIFICATION_SERVICE},
                0);
        new Channels(this);

    }


    public void receivers() {
        IntentFilter sIntentFilter = new IntentFilter("SentIntent");
        IntentFilter dIntentFilter = new IntentFilter("DeliveryIntent");
        if (sentReceiver == null) {

            sentReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {

                    TextView textView = findViewById(R.id.Numbers);
                    if (getResultCode() == Activity.RESULT_OK) {

                        String text = intent.getStringExtra("Number") + " " + "...\n";
                        textView.append(text);

                        //textView.append(intent.getStringExtra("NumberOfUser") + " " + "в процессе...");
                        //TODO Create HashMap with users/status and update textView
                    } else
                        textView.append(intent.getStringExtra("Number") + " " + " error \n");


                }
            };

        }
        if (deliveryReceiver == null) {

            deliveryReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    System.out.println("Delivery extras = " + intent.getStringExtra("NumberOfUser"));
                    System.out.println(context.getApplicationInfo());

                    TextView textView = findViewById(R.id.Numbers);
                    String lines = String.valueOf(textView.getText());
                    String line = intent.getStringExtra("Number") + " " + " OK \n";
                    textView.append(line);
                    //TODO Create HashMap with users/status and update textView

                }
            };
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(sentReceiver, sIntentFilter, RECEIVER_NOT_EXPORTED);
            registerReceiver(deliveryReceiver, dIntentFilter, RECEIVER_NOT_EXPORTED);
        }


        if (alarmSmsReceiver == null) {
            IntentFilter TestFilter = new IntentFilter("Alarm");
            alarmSmsReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    Log.e("TestReceiver", "Input");
                    if (smsWorker) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notify");
                        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
                        builder.setContentTitle("Смс");
                        builder.setContentText("В процессе отправки");
                        Notification notify = builder.build();

                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.notify(202, notify);
                        //TODO sms
                        EditText token = findViewById(R.id.Token);
                        Log.e("c", token.getText().toString());
                        Thread tokener = new GetToken("{\"apiKey\":\"" + token.getText().toString() + "\"}");

                        tokener.start();
                        ThreadJoiner(tokener);

                        Log.d("Token ", Token.getAccessToken());


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(new Intent(context, SmsSendService.class));
                            startForegroundService(new Intent(context, AlarmService.class));
                        }
                    }


                    Log.e("TestReceiver", "end");


                }
            };
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(alarmSmsReceiver, TestFilter, RECEIVER_NOT_EXPORTED);
            }
        }

        Toast.makeText(this, "Готово к работе", Toast.LENGTH_SHORT).show();
    }

    public void SaveToken(View v) {

        EditText editText = findViewById(R.id.Token);
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();

        ed.putString("Saved_Token", editText.getText().toString());
        ed.apply();

        Toast.makeText(this, "Token - сохранен!", Toast.LENGTH_SHORT).show();
    }

    public void load() {
        EditText editText = findViewById(R.id.Token);
        editText.setText(getPreferences(MODE_PRIVATE).getString("Saved_Token", ""));
    }

    public void Sent(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(context, SmsSendService.class).putExtra("Send", true));


            Thread thread = new Thread(() -> {

                try {
                    Log.e("odm", "bom");


                    startForegroundService(new Intent(context, SmsSendService.class).putExtra("Number", 5));
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            });

            thread.start();
        }

    }

    public void Switcher(View v) {
        AlarmManager alarmManager = getSystemService(AlarmManager.class);
        SwitchCompat switchStatus = findViewById(R.id.switcher);
        Random r = new Random();
        Intent intent = new Intent("Alarm");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, r.nextInt(), intent, PendingIntent.FLAG_MUTABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (switchStatus.isChecked()) {
                startForegroundService(new Intent(this, AlarmService.class));
                smsWorker = true;


            } else {
                Toast.makeText(this, "Рассылка выключена", Toast.LENGTH_SHORT).show();
                Log.e("Service", "Stop");
                smsWorker = false;
                alarmManager.cancel(pendingIntent);
                stopService(new Intent(this, SmsSendService.class));
                stopService(new Intent(this, AlarmService.class));
            }

        }
    }

    public void option(View view) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog);
        EditText editText = dialog.findViewById(R.id.smsMassage);
        editText.setText(getPreferences(MODE_PRIVATE).getString("Saved_Massage", ""));

        Button saveButton = dialog.findViewById(R.id.saveMassage);
        saveButton.setOnClickListener(v -> {

                        SharedPreferences sPref = getSharedPreferences("main" ,Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed = sPref.edit();

                        ed.putString("Saved_Massage", editText.getText().toString());
                        ed.apply();

                        Toast.makeText(context, "Сообщение - сохранено!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    });



       dialog.show();
    }
    /*public void saveMessage(View view){
        EditText editText = findViewById(R.id.smsMassage);
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();

        ed.putString("Saved_Massage", editText.getText().toString());
        ed.apply();

        Toast.makeText(this, "Сообщение - сохранено!", Toast.LENGTH_SHORT).show();

    }*/


    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop");
    }

    @Override
    protected void onDestroy() {
        if (sentReceiver != null)
            unregisterReceiver(sentReceiver);
        if (deliveryReceiver != null)
            unregisterReceiver(deliveryReceiver);
        if (alarmSmsReceiver !=null)
            unregisterReceiver(alarmSmsReceiver);
        super.onDestroy();
        System.out.println("OnDestroy");
    }

    private void ThreadJoiner(Thread thread) {
        try {
            thread.join(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}