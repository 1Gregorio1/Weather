package com.example.weather;

import android.icu.text.SimpleDateFormat;
import android.icu.util.GregorianCalendar;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SomeClass {

    interface Callback {
        void callingBack(String[][] str);
    }

    Callback callback;

    public void registerCallBack(Callback callback) {
        this.callback = callback;
    }

    void doSomething(String url) {

        Thread thread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
                    httpClient.setRequestProperty("Accept-Charset", "UTF-8");
                    httpClient.setRequestMethod("GET");
                    BufferedReader dateReader = new BufferedReader(new InputStreamReader(httpClient.getInputStream()));
                    StringBuilder builder = new StringBuilder("");
                    String line = "";
                    while ((line = dateReader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }

                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) parser.parse(builder.toString());
                    JSONArray list = (JSONArray) jsonObject.get("list");

                    String[][] builders = new String[list.size()][4];
                    JSONObject[] cloudiness = new JSONObject[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        cloudiness[i] = (JSONObject) list.get(i);
                    }
                    for (int j = 0; j < list.size(); j = j + 1) {
                        JSONObject main = (JSONObject) cloudiness[j].get("main");
                        JSONArray weather = (JSONArray) cloudiness[j].get("weather");
                        JSONObject cloudines = null;

                        for (int i = 0; i < weather.size(); i++) {
                            cloudines = (JSONObject) weather.get(i);
                        }
                        String description = (String) cloudines.get("description");
                        String icon = (String) cloudines.get("icon");

                        Double temp = Double.valueOf(main.get("temp").toString());
                        String txt = (String) cloudiness[j].get("dt_txt");
                        txt = txt.substring(0, 16);
                        builders[j][0] = Day(txt);
                        builders[j][1] = (int) Math.round(temp) + "\u00B0" + "";
                        builders[j][2] = description;
                        builders[j][3] = icon;

                    }
                    //дошли сюда, вызов того что создали и вызвали в майне
                    callback.callingBack(builders);
                    //runOnUiThread(()
                } catch (Throwable e) {
                    String[][] builders = new String[1][4];
                    builders[0][0] = "Город не существует!";
                    builders[0][1] = "";
                    builders[0][2] = "";
                    builders[0][3] = "";
                    callback.callingBack(builders);
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    String Day(String txt) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        int year = Integer.parseInt(txt.substring(1,4));
        int month;
        if (txt.substring(5,6).equals('0')) {
            month = Integer.parseInt(txt.substring(6,7));
        } else {
            month = Integer.parseInt(txt.substring(5,7));
        }
        int day;
        if (txt.substring(8,9).equals('0')) {
            day = Integer.parseInt(txt.substring(9,10));
        } else {
            day = Integer.parseInt(txt.substring(8,10));
        }
        GregorianCalendar calendar = new GregorianCalendar(year, month , day);
        String result = dateFormat.format(calendar.getTime()) + " " + txt.substring(11,16);
        return result;
    }
}
