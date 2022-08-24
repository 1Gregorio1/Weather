package com.example.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText city;
    private Button main_button;
    private TextView result_weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = findViewById(R.id.city);
        main_button = findViewById(R.id.main_button);
        result_weather = findViewById(R.id.rezult);

        main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (city.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.cyty_error, Toast.LENGTH_SHORT).show();
                } else {
                    String key = "e7b4cbc1bb3f56d422bf53c93ef78ae4";
                    String gorod = city.getText().toString();
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + gorod + "&appid=" + key + "&units=metric&lang=ru";
                    System.out.println(url);
                    try {
                        createRequest(url);
                    } catch (IOException | ParseException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    protected void createRequest(String url) throws MalformedURLException, IOException, ParseException, JSONException {

        Thread thread = new Thread(new Runnable() {
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
                    JSONObject main = (JSONObject) jsonObject.get("main");
                    Double temp = (Double) main.get("temp");
                    runOnUiThread(() -> result_weather.setText((int) Math.round(temp) + ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}








