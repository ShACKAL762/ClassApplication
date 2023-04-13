package com.example.sms.Other.JSONS;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class  Token {
    private static String accessToken;
    public static PendingIntent pendingIntent;
    public static Context context;
    public static Intent intent;

    public  static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        Token.accessToken = accessToken;
    }
}