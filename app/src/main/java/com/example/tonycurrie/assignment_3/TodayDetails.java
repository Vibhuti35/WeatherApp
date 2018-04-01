package com.example.tonycurrie.assignment_3;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class TodayDetails extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today);


        TextView t1=findViewById(R.id.t1);
        String s=null;
        ArrayList<WeatherDetails> details = MainActivity.weatherForecasts;
        for(int i=0; i<details.size(); i++){
            String temp=details.get(i).getDegree() + details.get(i).getTime();
            s = s +temp;
            // Toast.makeText(getApplicationContext(), "Det:" +s, Toast.LENGTH_LONG).show();
        }
        t1.setText(s);

    }
}
