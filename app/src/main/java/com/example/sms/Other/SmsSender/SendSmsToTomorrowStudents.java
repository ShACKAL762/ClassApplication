package com.example.sms.Other.SmsSender;


import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

import com.example.sms.Other.Intents;
import com.example.sms.Other.JSONS.LessonJson;
import com.example.sms.Other.JSONS.LessonsOfDayJson;
import com.example.sms.Other.JSONS.UserData;
import com.example.sms.Other.JSONS.UserJson;
import com.example.sms.Other.Token.Token;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SendSmsToTomorrowStudents extends Thread {

    private Request request;
    private Response response;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final Context context;


    public SendSmsToTomorrowStudents(Context context){
        this.context = context;
    }

    @Override
    public void run() {
        SendSmsToUsers();
    }
    private void SendSmsToUsers() {
        Map<String, UserData> users;

        try {

            users = GetUsersPhonesFromLessons();

            users.forEach((k, v) -> {
                //TODO Send sms to all users
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });


        } catch (IndexOutOfBoundsException | IOException e) {
            Log.e("Error", String.valueOf(e));


            //throw new RuntimeException(e);
        }

        //SendSms(users);
        //SendSmsTest();
    }

    private void SendSms(Map<String, UserData> users) {
        Intents intents = new Intents(context);

        users.forEach((k, v) ->{
                System.out.println(k +  "Ждем вас "+ v.date +" в " + v.beginTime + " в Purple Cup.По адресу ул.Туристская 33к1.");

        SmsManager.getDefault().sendTextMessage(
                k,
                null,
                "Ждем вас "+ v.date +" в " + v.beginTime + " в Purple Cup. По адресу ул.Туристская 33к1.",
                intents.getSent(),
                intents.getDelivered());
        });
    }



    private Map<String, UserData> GetUsersPhonesFromLessons() throws IOException {
        ArrayList<JsonElement> lessons;
        lessons = GetLessons();
        if(lessons == null){
            throw  new IOException();
        }
        return GetUsersFromLessons(lessons);

    }

    private ArrayList<JsonElement> GetLessons() {
        LessonsOfDayJson lessonsOfDayJson;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,1);
//TODO Date tomorrow in request
        request = requestBuild("https://api.moyklass.com/v1/company/lessons?" + "date=2023-06-11" + "&includeRecords=true");
        {
            try {
                response = client.newCall(request).execute();
                String result = response.body().string();
                lessonsOfDayJson = gson.fromJson(result, LessonsOfDayJson.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(lessonsOfDayJson.lessons != null)
            return lessonsOfDayJson.lessons;
        else return null;
    }



    private Map<String, UserData> GetUsersFromLessons(List<JsonElement> lessons) {
        LessonJson lessonJson;
        UserJson userJson;
        String url;
        String result;
        Map<String, UserData> usersArray = new LinkedHashMap<>();


        for (JsonElement lesson : lessons) {
            lessonJson = gson.fromJson(lesson, LessonJson.class);

            if (!lessonJson.records.isEmpty()) {
                for (JsonElement user : lessonJson.records) {
                    userJson = gson.fromJson(user, UserJson.class);

                    try {
                        Thread.sleep(150);

                        url = "https://api.moyklass.com/v1/company/users/" + userJson.userId;
                        request = requestBuild(url);

                        response = client.newCall(request).execute();
                        result = response.body().string();
                        userJson = gson.fromJson(result, UserJson.class);

                        usersArray.put(userJson.phone, new UserData(lessonJson.beginTime,DateChange(lessonJson.date)));

                    } catch (InterruptedException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return usersArray;
    }

    private String DateChange(String date) {
        String[] returner = date.split("-");
        return returner[2] + "." + returner[1] +"." + returner[0].substring(2);
    }


    private Request requestBuild(String URL ) {
       return request = new Request.Builder()
                .addHeader("x-access-token", Token.getAccessToken())
                .url(URL)
                .get()
                .build();
            }
    }
