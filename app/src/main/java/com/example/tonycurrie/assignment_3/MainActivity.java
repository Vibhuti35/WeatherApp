package com.example.tonycurrie.assignment_3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    SharedPreferences previousLoc,city;
    Button b1,cur,day,dayn,day1,day2,day3,day4,day5,day6,day7;
    public static ArrayList<WeatherDetails> weatherForecasts = new ArrayList<>();
    String locationSelected = "null";
    public static NodeList list;
    public static NodeList entryItems;
    Node childNode;
    public static ArrayList<ExtraDetails> details = new ArrayList<>();
    public static HashMap<String, String> Currentdetails = new HashMap<>();
    TextView selectedCity,d1,d2,d3,d4,d5,d6,d7,td,tn;
    String cityn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1=(Button) findViewById(R.id.B1);
         selectedCity = (TextView)findViewById(R.id.City);
        d1 = (TextView) findViewById(R.id.d1);
        d2 = (TextView) findViewById(R.id.d2);
        d3 = (TextView) findViewById(R.id.d3);
        d4 = (TextView) findViewById(R.id.d4);
        d5 = (TextView) findViewById(R.id.d5);
        d6 = (TextView) findViewById(R.id.d6);
        d7 = (TextView) findViewById(R.id.d7);
        td = (TextView) findViewById(R.id.today);
        cur=(Button) findViewById(R.id.Detailcur);
        day=(Button) findViewById(R.id.Detailtoday);
        day1=(Button) findViewById(R.id.Detail1);
        day2=(Button) findViewById(R.id.Detail2);
        day3=(Button) findViewById(R.id.Detail3);
        day4=(Button) findViewById(R.id.Detail4);
        day5=(Button) findViewById(R.id.Detail5);
        day6=(Button) findViewById(R.id.Detail6);
        day7=(Button) findViewById(R.id.Detail7);
        //To change the location


        //Retriving previous values if already stored
        previousLoc = getSharedPreferences("location", Context.MODE_PRIVATE);
        city=getSharedPreferences("City", Context.MODE_PRIVATE);
        boolean preferenceCheck = previousLoc.contains("updatedloc");

        //If not first time it will show dialog box
        checkTheLocation(preferenceCheck);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent location = new Intent(MainActivity.this,LocationActivity.class);
                startActivity(location);
            }
        });

        cur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent location = new Intent(MainActivity.this,CurrentdayDetails.class);
                startActivity(location);
            }
        });

        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent location = new Intent(MainActivity.this,TodayDetails.class);
                startActivity(location);
            }
        });

        if(preferenceCheck==true) {
            previousLoc = getSharedPreferences("location", Context.MODE_PRIVATE);
            city=getSharedPreferences("location", Context.MODE_PRIVATE);
            locationSelected = previousLoc.getString("updatedloc", null);
             cityn=city.getString("updatedCity",null);
            Toast.makeText(getApplicationContext(), "City: " +cityn, Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "locationSelected: " +locationSelected, Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), "previousLocation: " +previousLoc, Toast.LENGTH_LONG).show();
            getWeatherConditions pull = new getWeatherConditions();
            pull.execute(locationSelected);

        }

    }

    //Prompt user to select the location
    public void checkTheLocation(boolean value){
        if(value == false) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Please select the location!!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                    startActivity(intent);
                } });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                } });

            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    //Parsing xml from URL fetched and Storing it to arraylist and hashmap
    private class getWeatherConditions extends AsyncTask<String,Void,ArrayList<WeatherDetails>> {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        URL url =  null;
        Node node;
        Element element;

        @Override
        protected ArrayList<WeatherDetails> doInBackground(String... params) {
            try {
                db = dbf.newDocumentBuilder();
                url = new URL(params[0]);
                Document doc = db.parse(url.openStream());
                doc.getDocumentElement().normalize();

                list = doc.getElementsByTagName("entry");

                for(int i=0; i<list.getLength(); i++){
                    entryItems = (NodeList) list.item(i).getChildNodes();
                    for(int j=0; j<entryItems.getLength(); j++){
                        childNode = entryItems.item(j);
                        if(childNode.getNodeName().equals("category")){
                            String categoryType = childNode.getAttributes().getNamedItem("term").getNodeValue();
                            if(categoryType.equals("Weather Forecasts")){
                                String title = entryItems.item(1).getTextContent();
                                String summary = entryItems.item(11).getTextContent();
                                String[] arr = title.split(":");
                                String[] arr1 = arr[1].split("\\.", 2);
                                if(i%2==0) {
                                    WeatherDetails e = new WeatherDetails(arr1[0], arr1[1], arr[0]);
                                    weatherForecasts.add(e);
                                }
                                ExtraDetails d = new ExtraDetails(title, summary);
                                details.add(d);
                            }else if(categoryType.equals("Current Conditions")){
                                String text =  entryItems.item(11).getTextContent();
                                String[] textSplit = text.split("(<b>)|(:</b>)|<br/>");
                                for(int k=1;k<textSplit.length; k=k+3) {
                                    Currentdetails.put(textSplit[k].trim(), textSplit[k + 1].trim());
                                }
                            }
                        }
                    }
                }

                return weatherForecasts;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        //Display data
        protected void onPostExecute(ArrayList<WeatherDetails> list){

            selectedCity.setText("Current City:"+cityn);

            ArrayList<TextView> elements = new ArrayList<>();
            elements.add(d1);
            elements.add(d2);
            elements.add(d3);
            elements.add(d4);
            elements.add(d5);
            elements.add(d6);
            elements.add(d7);

            int size = list.size();
            //Toast.makeText(getApplicationContext(), "Size" +size, Toast.LENGTH_LONG).show();
            for(int i=0; i<size; i++){
                String s = details.get(i).getHeading();//+list.get(i).getDegree() + list.get(i).getTime();
                elements.get(i).setText(s);
            }

        }
    }


}



