package com.example.sms.Other.Token;


import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class GetToken extends Thread{
    public String json;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public GetToken(String json){
        this.json = json;
    }
    @Override
    public void run() {

           Token.setAccessToken(token(json));

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

            String[] array = res.split("\"");

        if (array[3].length()>0)
            return array[3];
        else
                return "null";}
}


