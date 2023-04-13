package com.example.sms.Other;
//Users From lessonsOfDay to Array

import java.util.HashMap;

public class UsersNumbers {
    public HashMap<String, String> numbers;
    public String getNumbers(int i) {
        return numbers.get(i);
    }

    public void setNumbers(String number, String time) {
        numbers.put(number, time );
    }


    public UsersNumbers(){
        numbers = new HashMap<>();
    }





}
