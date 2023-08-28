package com.example.sms.Other.Token;


import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class GetToken extends Thread{
    static private long timer;
    public String json;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public GetToken(String json){
        this.json = json;
    }
    @Override
    public void run() {
            Log.e("System", ""+System.currentTimeMillis());
            Log.e("System2", ""+ timer);
        Log.e("log", System.currentTimeMillis() - timer + " : " + 60*1000);
            if((System.currentTimeMillis() - timer) >= 60*1000) {
                Log.e("log", System.currentTimeMillis() - timer + " : " + 60*1000);
                Token.setAccessToken(token(json));
                timer = System.currentTimeMillis();
                Log.e("!!!!!","wtf");
            }

    }
    public String token(String json) {
        Response response;
        String url;
        String res;
            final OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, json);
         url = "https://api.moyklass.com/v1/company/auth/getToken";

        Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

        try {
            response = client.newCall(request).execute();
            res = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Log.e("Alarmer", "" + response.code());
            String[] array = res.split("\"");
        Log.e("Alarmer", array[0]);

        if (array[3].length()>0)
            return array[3];
        else
                return "null";}
}


