package com.sart.evbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CSDetailsForCustomer extends AppCompatActivity
{
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Owner> list;
    Location lastLocation;
    MyAdapter adapter;
    public static final int REQUEST_LOCATION_CODE = 99;

    LocationManager locationManager;
    LocationListener locationListener;

    Double lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csdetails_for_customer);

        Toast.makeText(this,"Please wait!",Toast.LENGTH_SHORT).show();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            checkLocationPermission();
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        reference = FirebaseDatabase.getInstance().getReference().child("Owners");
        recyclerView = findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Owner>();

        if(isGPSEnabled)
        {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location)
                {
                    lastLocation = location;
                    lat = lastLocation.getLatitude();
                    lon = lastLocation.getLongitude();
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            list.clear();

                            for (DataSnapshot ownerSnapshot : dataSnapshot.getChildren())
                            {
                                Owner owner = ownerSnapshot.getValue(Owner.class);

                                Location targetPoint=new Location("targetLocation");
                                targetPoint.setLatitude(Double.valueOf(owner.getLatitude()));
                                targetPoint.setLongitude(Double.valueOf(owner.getLongitude()));

                                Location srcPoint=new Location("srcLocation");

                                if(lat == null || lon == null)
                                {

                                }
                                else
                                {
                                    srcPoint.setLatitude(Double.valueOf(lat));
                                    srcPoint.setLongitude(Double.valueOf(lon));
                                }



                                if(srcPoint != null)
                                {
                                    if(srcPoint.distanceTo(targetPoint) <= 50000)
                                    {
                                        list.add(ownerSnapshot.getValue(Owner.class));
                                    }
                                }
                                else
                                {
                                    Toast.makeText(CSDetailsForCustomer.this,"Something Went Wrong.",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                            adapter = new MyAdapter(CSDetailsForCustomer.this,list);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
        }
        else
        {
            Toast.makeText(this,"Please turn on your GPS",Toast.LENGTH_SHORT).show();
            finish();
        }

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else
        {
            if(isGPSEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
            }
            else
            {
                Toast.makeText(this,"Please turn on your GPS",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
            }
        }
    }
}
