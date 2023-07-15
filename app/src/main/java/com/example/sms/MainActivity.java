package com.example.sms;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationCompat;

import com.example.sms.Other.MyIntentService;
import com.example.sms.Other.Notification.Channels;
import com.example.sms.Other.SmsSendService;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    BroadcastReceiver sentReceiver = null;
    BroadcastReceiver deliveryReceiver = null;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load();
        receivers();
        new Channels(this);
    }
    public void receivers(){
        if(sentReceiver == null){
            IntentFilter sIntentFilter = new IntentFilter("SentIntent");
            sentReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    TextView textView = findViewById(R.id.Numbers);

                    if(getResultCode() == Activity.RESULT_OK){
                        String text = intent.getStringExtra("NumberOfUser") + " " + "...";
                        textView.setText(text);
                        //textView.append(intent.getStringExtra("NumberOfUser") + " " + "в процессе...");
                        //TODO Create HashMap with users/status and update textView
                    }else
                        textView.append(intent.getStringExtra("NumberOfUser") + " " + " error ");


                }
            };
            registerReceiver(sentReceiver,sIntentFilter);
        }
        if(deliveryReceiver == null){
            IntentFilter dIntentFilter = new IntentFilter("DeliveryIntent");
            deliveryReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    System.out.println("Delivery extras = " + intent.getStringExtra("NumberOfUser"));
                    System.out.println(context.getApplicationInfo());

                    TextView textView = findViewById(R.id.Numbers);
                    String lines = String.valueOf(textView.getText());
                    String line = intent.getStringExtra("NumberOfUser") + " " + " OK ";
                    textView.setText(line);
                    //TODO Create HashMap with users/status and update textView

                }
            };
            registerReceiver(deliveryReceiver,dIntentFilter);
        }
        Toast.makeText(this,"Готово к работе", Toast.LENGTH_SHORT).show();
    }
    public void SaveToken(View v){

        EditText editText = findViewById(R.id.Token);
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();

        ed.putString("Saved_Token", editText.getText().toString());
        ed.apply();

        Toast.makeText(this, "Token - сохранен!", Toast.LENGTH_SHORT).show();
    }
    public void load(){
        EditText editText = findViewById(R.id.Token);
        editText.setText(getPreferences(MODE_PRIVATE).getString("Saved_Token",""));
    }
    public void Sent(View v){
        String id = "Notify";

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, id)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Title")
                        .setContentText("Notification text");

        Notification notification = builder.build();
        NotificationManager notificationManager =
                getSystemService(NotificationManager.class);
        notificationManager.notify(1, notification);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, MyIntentService.class).putExtra("count", "counter"));
        }

        Log.e("IntentService", "start");
        Runnable r = new Runnable() {
            @Override
            public void run() {

                for(int i = 1000; i > 0; i--) {
                    try {
                        Thread.sleep(1000);
                        System.out.println(i);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                getSystemService(SmsManager.class).sendTextMessage(
                        "89779482492",
                        null,
                        "test",
                        null,
                        null);

            }
        };
        Thread thread = new Thread(r);
        //thread.start();





       /* EditText token = findViewById(R.id.Token);
        GetToken tok = new GetToken("{\"apiKey\":\"" + token.getText().toString() + "\"}");
        tok.start();
        ThreadJoiner(tok);
        new SendSmsToTomorrowStudents(this).start();*/






    }
    public void Switcher(View v) {
        SwitchCompat c = findViewById(R.id.switcher);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ServiceConnection serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    Log.e("Bind", "Connection");
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.e("Bind", "DisConnection");
                }
            };
            Intent counter = new Intent(this,SmsSendService.class);
            if (c.isChecked()) {
                startService(counter);
                //startForegroundService(counter.putExtra("Turn", false).putExtra("count", "counter"));
            } else{
                stopService(counter);

            }



                //stopService(new Intent(this, SmsSendService.class));
                //stopService(counter);
        }
    }

    public void button(View v) {
        ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        int maxNum = 100;
        List<ActivityManager.RunningServiceInfo> list = activityManager.getRunningServices(maxNum);

        StringBuilder info = new StringBuilder();

        info.append("Services currently running: ").append(list.size()).append("\n\n");
        for(int i=0; i<list.size(); i++){
            info.append(list.get(i).service).append("\n\n");
        }

        System.out.println(info);

        //stopService(new Intent(this,SmsSendService.class));
        /*Button button = findViewById(R.id.Button);
        button.setEnabled(false);
        button.setText("Нажата");
        EditText token = findViewById(R.id.Token);
        TextView numbers = findViewById(R.id.Numbers);

        numbers.setText("Загрузка!");
        GetToken tok = new GetToken("{\"apiKey\":\"" + token.getText().toString() + "\"}");
        tok.start();
        ThreadJoiner(tok);



*/


    }


    /*public void Sent(View v){

        IntentFilter fSent = new IntentFilter("Sent");
        IntentFilter fUnSent = new IntentFilter("UnSent");

        Intent sentIntent = new Intent("Sent");
        Intent unSentIntent = new Intent("UnSent");


        textView = findViewById(R.id.Numbers);
        System.out.println(textView.getText());
        textView.setText(null);

        ArrayList<String>list;
        list = new ArrayList<>();
        ArrayList<String> List = list;
        System.out.println("List in this point = " + List);
        System.out.println("textView after " + textView.getText());

        System.out.println("textView after all " + textView.getText());


        BroadcastReceiver receiver;
        if (!receiverOn) {
            System.out.println("new receiver");
            receiverOn= true;


            receiver = new BroadcastReceiver() {


                @Override
                public void onReceive(Context context, Intent intent) {
                    System.out.println("pip");
                    System.out.println(intent.getExtras().size());
                    if(intent.getBooleanExtra("1",false))
                        List.clear();
                    List.add(intent.getStringExtra("5") + " +\n");
                    System.out.println(List);
                    TextView text;
                    System.out.println(intent.getExtras());
                    text = textView;

                    text.append(intent.getStringExtra("5") + " +\n");

                    //System.out.println(appender);


                    //System.out.println(intent.getStringExtra(consta));
                    System.out.println("Сообщение");


                }
            };
            ;
            BroadcastReceiver receiver2 = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {


                    textView.append(intent.getStringExtra("5") + " delivered +\n");


                    System.out.println("Сообщение2");

                }
            };
            registerReceiver(receiver2, fUnSent);

            registerReceiver(receiver, fSent);
        }


        //String num = "89165313653";
        //sentIntent.putExtra(consta,num);
        String[]numbers = new String[2];
        numbers[0] = "89779482492";
        numbers[1] = "89165313653";
        //for (int i = 0; i < numbers.length; i++) {

        //}

        class myRun extends Thread{
            final Context context;
            Intent sIntent;
            final Intent dIntent;

            myRun(Context context, Intent ... intents){

                this.sIntent = intents[0];
                this.dIntent = intents[1];

                this.context = context;

            }

            @Override
            public void run() {
                PendingIntent pSent;

                ArrayList <String> strings = new ArrayList<>();
                strings.add("test");
                strings.add("test");
                System.out.println(strings);

                for (int i = 0; i < numbers.length; i++){
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    sIntent = new Intent("Sent");
                    sIntent.putExtra("5",numbers[i]);
                    System.out.println("Pending to " + sIntent.getStringExtra("5"));
                    Random random = new Random();

                        getSystemService(Vibrator.class).vibrate(300);
                    System.out.println(numbers.length);
                        pSent = PendingIntent.getBroadcast(context,random.nextInt() , new Intent("Sent").putExtra("5",numbers[0]).putExtra("1", true), PendingIntent.FLAG_IMMUTABLE );

                                getSystemService(SmsManager.class).sendTextMessage(
                                        numbers[0],
                                        null,
                                        "test",
                                        pSent,
                                        PendingIntent.getBroadcast(context, random.nextInt(), new Intent("unSent").putExtra("5",numbers[0]), PendingIntent.FLAG_IMMUTABLE ));

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    getSystemService(SmsManager.class).sendTextMessage(
                            numbers[1],
                            null,
                            "test",
                            PendingIntent.getBroadcast(context,random.nextInt() , new Intent("Sent").putExtra("5",numbers[1]).putExtra("1", true), PendingIntent.FLAG_IMMUTABLE ),
                            PendingIntent.getBroadcast(context, random.nextInt(), new Intent("unSent").putExtra("5",numbers[1]), PendingIntent.FLAG_IMMUTABLE ));
                    break;






                }

            }
        }
        myRun m = new myRun(this,sentIntent,unSentIntent);
        m.start();
        ThreadJoin(m);



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
    protected void onDestroy()
    {
        if(sentReceiver != null)
            unregisterReceiver(sentReceiver);
        if(deliveryReceiver != null)
            unregisterReceiver(deliveryReceiver);
        super.onDestroy();
        System.out.println("OnDestroy");
    }
    private void ThreadJoiner(Thread thread){
        try {
            thread.join(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}