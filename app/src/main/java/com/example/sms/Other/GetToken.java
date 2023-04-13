package com.example.sms.Other;


import com.example.sms.Other.JSONS.Token;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class GetToken extends Thread{
    public String json;
    private final String url = "https://api.moyklass.com/v1/company/auth/getToken";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public GetToken(String json){
        this.json = json;
    }
    @Override
    public void run() {
        try {
            token(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void token(String json) throws IOException {
            final OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();

            String[] array = res.split("\"");
            Token.setAccessToken(array[3]);
        System.out.println(Token.getAccessToken());
                }
}


