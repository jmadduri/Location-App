package com.example.locapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    double latitude;
    double longitude;
    double distance;
    TextView textViewlatitude;
    TextView textViewLongitude;
    TextView textViewAddress;
    TextView textViewDistance;
    TextView textViewTime;
    Button resetButton;
    ArrayList<Address> addresses;
    ArrayList<Location> locationsArrayList;
    int count;
    int timeReset;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count = 0;
        distance = 0;
        timeReset = 0;
        locationsArrayList = new ArrayList<>();
        textViewlatitude = findViewById(R.id.textView3);
        textViewLongitude = findViewById(R.id.textView);
        textViewAddress = findViewById(R.id.textView4);
        textViewDistance = findViewById(R.id.textView5);
        textViewTime = findViewById(R.id.textView6);
        resetButton = findViewById(R.id.button);
        addresses = new ArrayList<>();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 1;
                distance = 0;
                timeReset = 0;
                textViewTime.setText("0 sec");
                textViewDistance.setText("0.0 meters");
            }
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12345);
        }

        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.US);
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                locationsArrayList.add(location);

                textViewlatitude.setText("Latitude: " + latitude);
                textViewLongitude.setText("Longitude: " + longitude);

                try {
                    Log.d("TAG8", "Works Here");
                    addresses.add(geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0));
                    Log.d("TAG7", "Works Here");
                    textViewAddress.setText(addresses.get(count).getAddressLine(0));
                    Log.d("TAG6", "Works Here");
                    count++;
                } catch (Exception e) {
                    Log.d("TAG5", e.getMessage());
                }
                if(locationsArrayList != null && locationsArrayList.size() > 1){
                    distance += location.distanceTo(locationsArrayList.get(locationsArrayList.size() - 2));
                    long currentTime = (location.getElapsedRealtimeNanos()/1000000000);
                    long prevTime = (locationsArrayList.get(locationsArrayList.size() - 2).getElapsedRealtimeNanos()/1000000000);
                    long timeElapsed = currentTime - prevTime;
                    textViewTime.setText(timeElapsed+ " sec");
                }
                textViewDistance.setText(distance + " meters");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, locationListener);
        }catch (Exception e){
            Log.d("TAG4", e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 12345){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("TAG3", "PERMISSION GRANTED");
            }
        }
    }
}




