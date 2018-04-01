package com.example.tonycurrie.assignment_3;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;



public class LocationActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap Map;
    GoogleApiClient GoogleApiClient;
    Marker CurrLocationMarker;
    LocationRequest LocationRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest = new LocationRequest();
        LocationRequest.setInterval(1000);
        LocationRequest.setFastestInterval(1000);
        LocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(GoogleApiClient, LocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {


        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Toast.makeText(getApplicationContext(), "Lat:"+location.getLatitude()+", Long:"+location.getLongitude(), Toast.LENGTH_LONG).show();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        CurrLocationMarker = Map.addMarker(markerOptions);
        Map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        Map.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (GoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(GoogleApiClient, this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Map = googleMap;

        //Adding a marker at current location and move the camera
        LatLng Current = new LatLng(44.64,  -63.57);
        Map.addMarker(new MarkerOptions().position(Current).title("Current Location."));
        Map.moveCamera(CameraUpdateFactory.newLatLng(Current));

        // On selection of location
        Map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng position) {
                // TODO Auto-generated method stub
                Log.d("arg0", position.latitude + "-" + position.longitude);
                double clicklat=position.latitude;
                double clicklong=position.longitude;
                Toast.makeText(getApplicationContext(), "Lat:"+position.latitude +", Long:"+position.longitude, Toast.LENGTH_LONG).show();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(position);

                try {
                    String address="";
                    Geocoder geo = new Geocoder(LocationActivity.this.getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = geo.getFromLocation(clicklat, clicklong, 1);
                    if (addresses.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Waiting for Location", Toast.LENGTH_SHORT).show();
                    } else {

                        if (addresses.size() > 0) {

                            address =addresses.get(0).getLocality();
                            //addresses.get(0).getAdminArea()+ ", " +
                            //+ ", " + addresses.get(0).getCountryName();
                            Toast.makeText(getApplicationContext(), "Address:- " + address, Toast.LENGTH_LONG).show();
                        }

                        markerOptions.title(address);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                        CurrLocationMarker = Map.addMarker(markerOptions);

                        //move map camera
                        Map.moveCamera(CameraUpdateFactory.newLatLng(position));
                        Map.animateCamera(CameraUpdateFactory.zoomTo(11));

                        String line = "";
                        int match=0;
                        String xml="";
                        String city="";
                        // reading CSV file for feeds
                        InputStream is = getResources().openRawResource(R.raw.feeds);
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(is, Charset.forName("UTF-8")));
                        while((line = reader.readLine()) != null) {
                            String cvsSplitBy = ",";
                            // using comma as separator
                            String[] cols = line.split(cvsSplitBy);
                            //Toast.makeText(getApplicationContext(), "cols:- " + cols.length, Toast.LENGTH_LONG).show();
                            Log.d("Cols:",""+cols);
                            for (int i = 0; i < cols.length; i++)
                            {
                                //Toast.makeText(getApplicationContext(), "cols:- " + cols[1], Toast.LENGTH_LONG).show();
                                Log.d("ColsVal:",""+cols[1]);
                                String Colval=cols[1];
                                String addressmatch=" "+address;
                                if(Colval.equals(addressmatch))
                                {
                                    Log.d("ColsIF:",""+cols[1]);
                                    match=1;
                                    xml=cols[0];
                                    city=cols[1];
                                    Toast.makeText(getApplicationContext(), "Csv match:- " + cols[1], Toast.LENGTH_LONG).show();
                                }

                            }

                        }
                        if(match==1)
                        {
                            Toast.makeText(getApplicationContext(), "XML: " +xml, Toast.LENGTH_LONG).show();
                            SharedPreferences previousLocation = getSharedPreferences("location", Context.MODE_PRIVATE);
                            SharedPreferences citys = getSharedPreferences("city", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = previousLocation.edit();
                            editor.putString("updatedloc", xml);
                            editor.putString("updatedCity",city);
                            editor.commit();
                            Intent intent = new Intent(LocationActivity.this, MainActivity.class);
                            startActivity(intent);

                        }

                        else
                        {
                            Toast.makeText(getApplicationContext(), "Please choose other city.", Toast.LENGTH_LONG).show();

                        }
                    }


                }
                catch(Exception e)
                {

                }
            }
        });

    }



}

