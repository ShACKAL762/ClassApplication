package com.example.sms.Other.JSONS;

import com.google.gson.JsonElement;

import java.util.ArrayList;

public class LessonsOfDayJson {
   private final ArrayList<Lesson> lessonsPottery = new ArrayList<>();
   private final ArrayList<Lesson> lessonsTable = new ArrayList<>();
   public ArrayList<JsonElement> lessons;

   public ArrayList<Lesson> getLessons() {
      return lessonsTable;
   }
   public ArrayList<Lesson> getLessonsPottery() {
      return lessonsPottery;
   }


   public void setLessons(Lesson lesson) {


   }
}
