package com.example.sms;


import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.Activity;
import android.app.AlarmManager;
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
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.sms.Other.MyIntentService;
import com.example.sms.Other.Notification.Channels;
import com.example.sms.Sevices.AlarmService;
import com.example.sms.Sevices.SmsSendService;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    BroadcastReceiver sentReceiver = null;
    BroadcastReceiver deliveryReceiver = null;
    BroadcastReceiver testReceiver = null;
    BroadcastReceiver testReceiver2 = null;
    int count = 0;
    Calendar cal = new GregorianCalendar();
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
                        String text = intent.getStringExtra("number") + " " + "...";
                        textView.setText(text);
                        //textView.append(intent.getStringExtra("NumberOfUser") + " " + "в процессе...");
                        //TODO Create HashMap with users/status and update textView
                    }else
                        textView.append(intent.getStringExtra("number") + " " + " error ");


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
                    String line = intent.getStringExtra("number") + " " + " OK ";
                    textView.setText(line);
                    //TODO Create HashMap with users/status and update textView

                }
            };
            registerReceiver(deliveryReceiver,dIntentFilter);
        }
        //TODO remove down
        if(testReceiver == null){
            IntentFilter TestFilter = new IntentFilter("Alarm");
            testReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                   Log.e("TestReceiver","Input");
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notify");
                    builder.setSmallIcon(R.drawable.ic_launcher_foreground);
                    builder.setContentText("30 минут");
                    builder.setContentTitle("Прошло");
                    Notification notify = builder.build();

                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.notify(1,notify);


                    Log.e("TestReceiver","end");
                    startService( new Intent(context,AlarmService.class));


                }
            };
            registerReceiver(testReceiver,TestFilter);
        }
        if(testReceiver2 == null){

            cal.setTimeInMillis(System.currentTimeMillis());

            IntentFilter TestFilter2 = new IntentFilter("Alarm2");
            testReceiver2 = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.e("TestReceiver","Input");
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notify");
                    builder.setSmallIcon(R.drawable.ic_launcher_foreground);
                    builder.setContentText("Time: " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
                    builder.setContentTitle("Прошло");
                    builder.setGroup("First").setGroupSummary(true);
                    Notification notify = builder.build();

                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.notify(++count,notify);


                    Log.e("TestReceiver","end");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService( new Intent(context,AlarmService.class));
                    }


                }
            };
            registerReceiver(testReceiver2,TestFilter2);
        }
        //TODO remove up
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
        notificationManager.notify(2, notification);

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
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void Switcher(View v) {
        AlarmManager alarmManager = getSystemService(AlarmManager.class);
        SwitchCompat c = findViewById(R.id.switcher);
        Random r = new Random();
        Intent intent = new Intent("Alarm");
        Intent counter = new Intent(context,SmsSendService.class).putExtra("number","89779482492").putExtra("date","12.07.09").putExtra("time","12.15");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,r.nextInt(),intent,PendingIntent.FLAG_MUTABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

              if (c.isChecked()) {
                  ActivityCompat.requestPermissions(this, new String[] {POST_NOTIFICATIONS},108);
                  receivers();
                  startForegroundService(new Intent(this, AlarmService.class));



            } else{
                  Toast.makeText(this, "Рассылка выключена", Toast.LENGTH_SHORT).show();
                  Log.e("Service", "Stop");
                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                      alarmManager.canScheduleExactAlarms();
                  }
                  alarmManager.cancel(pendingIntent);
                  unregisterReceiver(testReceiver);
                  stopService(new Intent(this,SmsSendService.class));

            }



                //stopService(new Intent(this, SmsSendService.class));
                //stopService(counter);
        }
    }

    public void button(View v) {
        AlarmManager alarmManager = getSystemService(AlarmManager.class);



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