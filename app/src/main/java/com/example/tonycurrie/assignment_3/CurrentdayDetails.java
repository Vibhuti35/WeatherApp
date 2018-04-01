package com.example.tonycurrie.assignment_3;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CurrentdayDetails extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.curday);
        TextView t1=findViewById(R.id.t1);
        String s=null;
        ArrayList<ExtraDetails> details = MainActivity.details;
        for(int i=0; i<details.size(); i++){
            String temp=details.get(i).getOverView();
            s = s +temp;
           // Toast.makeText(getApplicationContext(), "Det:" +s, Toast.LENGTH_LONG).show();
        }
        t1.setText(s);

    }
}
