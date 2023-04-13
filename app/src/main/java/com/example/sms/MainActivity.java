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
            //SmsManager.getDefault().sendTextMessage("79779482492",null, "sms", pendingIntent ,null);
             SmsManager.getDefault().sendTextMessage("79779482492",null, "smser",null, pendingIntent);

            



        /*
        //GetToken getToken = new GetToken("{\"apiKey\": \"" + token + "\"}");
        GetToken getToken = new GetToken("{\"apiKey\": \"1XDwdz3gksxKhEEuQJJ6UtkzRxD8psJpuO5SlAm1o4ebidr5jO\"}");
        LessonsOfDay lessonsOfDay = new LessonsOfDay();
        getToken.start();
        ThreadJoin(getToken);

        lessonsOfDay.start();

        Toast toast = Toast.makeText(this, "Hello Android!",Toast.LENGTH_LONG);
        toast.show();


         */
    }
    private void ThreadJoin(Thread thread){
        try {
            thread.join(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }









        /*

         private String post (String url, String json) {
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                System.out.println(request.body().toString());
                //Response response = client.newCall(request).execute();

                try {
                    Response response = client.newCall(request).execute();
                    String res = response.body().string();
                    System.out.println(res);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "s";
            }}


        EditText number=(EditText)findViewById(R.id.phoneNumber);
        EditText message=(EditText)findViewById(R.id.Message);
        String numberText = number.getText().toString();
        String messageText= message.getText().toString();
        URL url = new URL("https://api.moyklass.com/v1/company/auth/getToken");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        //conn.setRequestMethod("POST");

        //conn.setRequestProperty("apiKey", "1XDwdz3gksxKhEEuQJJ6UtkzRxD8psJpuO5SlAm1o4ebidr5jO");

        //conn.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        System.out.println(sb);

        System.out.println(conn.getContent());
        //conn.connect();
        //System.out.println(conn.getContentType());


         */





}