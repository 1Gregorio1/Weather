package com.example.weather;

import android.annotation.SuppressLint;

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
            @SuppressLint("SetTextI18n")
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
                        builders[j][0] = txt;
                        builders[j][1] = (int) Math.round(temp) + "\u00B0" + "";
                        builders[j][2] = description;
                        builders[j][3] = icon;

                    }
                    //дошли сюда, вызов того что создали и вызвали в майне
                    callback.callingBack(builders);
                    //runOnUiThread(()
                } catch (Throwable e) {
                    String[][] builders = new String[1][4];
                    builders[0][0] = "Город";
                    builders[0][1] = " не";
                    builders[0][2] = " существует";
                    builders[0][3] = "!";
                    callback.callingBack(builders);
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
