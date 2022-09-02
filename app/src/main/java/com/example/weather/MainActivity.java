package com.example.weather;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText city;
    private Button main_button;
    public TextView result_weather;
    private TextView date;
    public RecyclerView weather;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.city);
        main_button = findViewById(R.id.main_button);
        date = findViewById(R.id.date);
        linearLayout = findViewById(R.id.linearLayout);


        date.setText(GetDate.getDate()); //Указываем текущую дату

        main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (city.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.cyty_error, Toast.LENGTH_SHORT).show();
                } else {
                    String key = "e7b4cbc1bb3f56d422bf53c93ef78ae4";
                    String gorod = city.getText().toString().trim();
                    String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + gorod + "&appid=" + key + "&units=metric&lang=ru";

                    linearLayout.removeAllViews();

                    TextView textView = new TextView(getApplicationContext());
                    textView.setText("Погода в городе " + gorod);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.parseColor("#FFFFFF"));
                    textView.setTextSize(25);
                    linearLayout.addView(textView);
                    city.setText("");

                    SomeClass.Callback callBack = new SomeClass.Callback() {
                        @Override
                        public void callingBack(String[] str) {
                            String[] weat = str;

                            runOnUiThread(() -> {
                                for (int i = 0; i < weat.length; i++) {
                                    //TextView nov = ff
                                    //Inflater()
                                    TextView textView = new TextView(getApplicationContext());
                                    textView.setText(weat[i]);
                                    textView.setGravity(Gravity.CENTER);
                                    textView.setTextColor(Color.parseColor("#FFFDFF"));
                                    textView.setTextSize(20);
                                    linearLayout.addView(textView);
                                }
                            });


                        }
                    };
                    SomeClass someClass = new SomeClass();
                    someClass.registerCallBack(callBack);

                    someClass.doSomething(url);


                }
            }
        });
    }

//    protected void createRequest(String url) throws MalformedURLException, IOException, ParseException, JSONException {
//
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
//                    httpClient.setRequestProperty("Accept-Charset", "UTF-8");
//                    httpClient.setRequestMethod("GET");
//                    BufferedReader dateReader = new BufferedReader(new InputStreamReader(httpClient.getInputStream()));
//                    StringBuilder builder = new StringBuilder("");
//                    String line = "";
//                    while ((line = dateReader.readLine()) != null) {
//                        builder.append(line).append("\n");
//                    }
//
//                    JSONParser parser = new JSONParser();
//                    JSONObject jsonObject = (JSONObject) parser.parse(builder.toString());
//                    JSONObject main = (JSONObject) jsonObject.get("main");
//                    JSONArray weather = (JSONArray) jsonObject.get("weather");
//
//                    JSONObject cloudiness = null;
//                    for (int i = 0; i < weather.size(); i++){
//                        cloudiness = (JSONObject) weather.get(i);
//                    }
//                    String description = (String) cloudiness.get("description");
//                    Double temp = (Double) main.get("temp");
//                    runOnUiThread(() -> result_weather.setText((int) Math.round(temp)+"\u00B0" + " " + description));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();
//    }
}









