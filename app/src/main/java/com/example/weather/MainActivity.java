package com.example.weather;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
                        public void callingBack(String[][] str) {
                            String[][] weat = str;

                            runOnUiThread(() -> {
                                for (int i = 0; i < weat.length; i++) {

                                    LayoutInflater inflater = getLayoutInflater();
                                    View view = inflater.inflate(R.layout.item_settings,null, true);
                                    TextView time = view.findViewById(R.id.time);
                                    time.setText(weat[i][0]);
                                    TextView description = view.findViewById(R.id.description);
                                    description.setText(weat[i][2]);
                                    TextView temp = view.findViewById(R.id.temp);
                                    temp.setText(weat[i][1]);
                                    ImageView icon = view.findViewById(R.id.icon);
                                    Glide.with(icon).load("https://openweathermap.org/img/wn/"+weat[i][3]+"@2x.png").into(icon);
                                    linearLayout.addView(view);
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
}









