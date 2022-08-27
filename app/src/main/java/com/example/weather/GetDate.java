package com.example.weather;
import java.util.Date;

public class GetDate {
    static String getDate() {
        Date date = new java.util.Date();
        String day = String.format("%tc", date);
        String[] arr = day.split(" ");
        String rez = arr[0] + " " + arr[2] + " " + arr[1] + " " + arr[3].substring(0, 5);
        return rez;
    }
}
