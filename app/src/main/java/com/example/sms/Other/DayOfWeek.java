package com.example.sms.Other;

public enum DayOfWeek {
    SUNDAY("Воскресенье"),
    SND("Вс"),
    SNDAY("Вск"),
    MONDAY("Понедельник"),
    MND("Пн"),
    TUESDAY("Вторник"),
    TSD("Вт"),
    WEDNESDAY("Среда"),
    WND("Среду"),
    WND2("Ср"),
    THURSDAY("Четверг"),
    TRD("Чт"),
    FRIDAY("Пятница"),
    FRD("Пятницу"),
    FRD2("Пт"),
    SATURDAY("Суббота"),
    STD("Субботу"),
    STD2("Сб");

    String day;
    DayOfWeek(String day){
        this.day = day;
    }
    public String GetDay(){
        return day;
    }
}
