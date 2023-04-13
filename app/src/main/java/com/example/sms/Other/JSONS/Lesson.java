package com.example.sms.Other.JSONS;

import com.google.gson.JsonElement;

import java.util.ArrayList;

public class Lesson {
    /*
    **Fields of Json{
    *
    *public int id;
    *public String date;
    *public String beginTime;
    *public String endTime;
    *public String CreatedAt;
    *public int filialId;
    *public int roomId;
    *public int classId;
    *public int status;
    *public String comment;
    *public int maxStudents;
    *public String topic;
    *public ArrayList<Integer> teachersIds;
    *public ArrayList<JsonElement> records;
    *public Object homeTask;
    *public Object lessonTask;
    *public ArrayList<Object> marks;
    *public ArrayList<Object> answers;
    *
    *}
     */

    public String date;
    public String beginTime;
    public int classId;
    public ArrayList<JsonElement> records;

    @Override
   public String toString(){
        String nameOfLesson;
        if (classId == 145802)
            nameOfLesson = "*Творческий день*";
        else
        if (classId == 146001 || classId == 146004)
            nameOfLesson = "*Лепка*";
        else nameOfLesson = "*Гончарный круг*";
        return (nameOfLesson + "\n");
        /*
                + new DateFormat(dateString, DateFormatState.FOR_USER).getDate() + "\n"
                + this.beginTime + "   Записано : " + this.records.size() + "/" + this.maxStudents);

         */
   }
}

