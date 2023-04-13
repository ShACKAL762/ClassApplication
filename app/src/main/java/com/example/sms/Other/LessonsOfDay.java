package com.example.sms.Other;

import android.telephony.SmsManager;

import com.example.sms.Other.JSONS.Lesson;
import com.example.sms.Other.JSONS.LessonsOfDayJson;
import com.example.sms.Other.JSONS.Token;
import com.example.sms.Other.JSONS.UserJson;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class LessonsOfDay extends Thread {
    Lesson lessonJsn;
    LessonsOfDayJson lessonsOfDayJson;
    Response response;
    UserJson userJson;
    UsersNumbers usersNumbers = new UsersNumbers();

    @Override
    public void run() {
        final OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .addHeader("x-access-token", Token.getAccessToken())
                .url("https://api.moyklass.com/v1/company/lessons?" + "date=2023-04-30" + "&includeRecords=true")
                .get()
                .build();

        {
            try {
                response = client.newCall(request).execute();
                String res = response.body().string();

                Gson g = new Gson();
                lessonsOfDayJson = g.fromJson(res, LessonsOfDayJson.class);

                if (lessonsOfDayJson.lessons != null) {
                    for (JsonElement lesson : lessonsOfDayJson.lessons) {
                        this.lessonJsn = g.fromJson(lesson, Lesson.class);
                        if (lessonJsn.records != null) {

                            for (JsonElement user : this.lessonJsn.records) {
                                userJson = g.fromJson(user, UserJson.class);
                                Thread.sleep(100);
                                request = new Request.Builder()
                                        .addHeader("x-access-token", Token.getAccessToken())
                                        .url("https://api.moyklass.com/v1/company/users/" + userJson.userId)
                                        .get()
                                        .build();


                                response = client.newCall(request).execute();
                                System.out.println(request.url());
                                System.out.println(response.code());
                                res = response.body().string();
                                userJson = g.fromJson(res, UserJson.class);
                                System.out.println(userJson.userId + userJson.name);

                                usersNumbers.setNumbers(userJson.phone, lessonJsn.beginTime);


                            }
                        }
                        System.out.println(this.lessonJsn.records);
                    }
                    usersNumbers.numbers.forEach((k, v) ->
                        System.out.println("Number: " + k + " Time: " + v));
                    try {
                        SmsManager.getDefault().sendTextMessage("000000",null, "sms", null,null);
                        System.out.println(SmsManager.STATUS_ON_ICC_SENT);

                    }catch (Exception e) {
                        System.out.println("e");
                    }

                            /*usersNumbers.numbers.forEach((k, v) -> {
                                 SmsManager.getDefault().sendTextMessage(k, null, "Ждем вас  в " + v + " в  Purple Cup. По адресу ул. Туристская 33к1.", null, null);

                            }
                            );

                             */

                }







            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

