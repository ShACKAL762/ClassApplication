package com.example.sms.Other.Token;

import android.app.PendingIntent;

public class  Token {

    private static String accessToken;
    public static PendingIntent pendingIntent;
    public static PendingIntent pendingGood;

    public  static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        Token.accessToken = accessToken;
    }
    public Token(String token){Token.accessToken = token;}
}