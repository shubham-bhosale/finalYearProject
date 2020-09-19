package com.sart.evbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
//import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import android.location.Location;
import android.location.LocationManager;

public class CSDetails extends AppCompatActivity
{
    EditText CSName,latitude,longitude;
    Button ok;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    Switch getCurrentLocation;

    String tableName;
    String value;

    LocationManager locationManager;
    Location myLocation;
    public static final int REQUEST_LOCATION_CODE = 99;

    String mylatitude;
    String mylongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csdetails);

        Intent i = getIntent();
        value = i.getStringExtra("value");

        CSName = findViewById(R.id.et_csname);
        latitude = findViewById(R.id.et_latitude);
        longitude = findViewById(R.id.et_longitude);
        ok = findViewById(R.id.okbtn);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        getCurrentLocation = findViewById(R.id.switch1);

        getCurrentLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if(b == true)
                {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    {
                        checkLocationPermission();
                    }

                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                    boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                    if(ActivityCompat.checkSelfPermission(CSDetails.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(CSDetails.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(CSDetails.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                    }
                    else
                    {
                        if(!isGPSEnabled)
                        {
                            Toast.makeText(CSDetails.this,"Please turn on your GPS",Toast.LENGTH_SHORT).show();
                            //finish();
                            getCurrentLocation.setChecked(false);
                        }
                        else
                        {
                            //code to fetch LatLan
                                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                                Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);


                                if (LocationGps !=null)
                                {
                                    double lat = LocationGps.getLatitude();
                                    double lan = LocationGps.getLongitude();

                                    mylatitude = String.valueOf(lat);
                                    mylongitude = String.valueOf(lan);

                                    latitude.setText(mylatitude);
                                    longitude.setText(mylongitude);

                                    latitude.setEnabled(false);
                                    longitude.setEnabled(false);
                                }
                                else if (LocationNetwork !=null)
                                {
                                    double lat = LocationNetwork.getLatitude();
                                    double lan = LocationNetwork.getLongitude();

                                    mylatitude = String.valueOf(lat);
                                    mylongitude = String.valueOf(lan);

                                    latitude.setText(mylatitude);
                                    longitude.setText(mylongitude);

                                    latitude.setEnabled(false);
                                    longitude.setEnabled(false);
                                }
                                else if (LocationPassive !=null)
                                {
                                    double lat = LocationPassive.getLatitude();
                                    double lan = LocationPassive.getLongitude();

                                    mylatitude = String.valueOf(lat);
                                    mylongitude = String.valueOf(lan);

                                    latitude.setText(mylatitude);
                                    longitude.setText(mylongitude);

                                    latitude.setEnabled(false);
                                    longitude.setEnabled(false);
                                }
                                else
                                {
                                    latitude.setText("");
                                    longitude.setText("");
                                    latitude.setEnabled(true);
                                    latitude.setEnabled(true);
                                    Toast.makeText(CSDetails.this,"Could not found location co-ordinates right now.",Toast.LENGTH_SHORT).show();
                                }
                        }
                    }
                }
                else if(b == false)
                {
                    latitude.setEnabled(true);
                    longitude.setEnabled(true);
                    latitude.setText("");
                    longitude.setText("");
                }
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String _CSName = CSName.getText().toString().trim();
                String _latitude = latitude.getText().toString().trim();
                String _longitude = longitude.getText().toString().trim();

                if(TextUtils.isEmpty(_CSName) || TextUtils.isEmpty(_latitude) || TextUtils.isEmpty(_longitude))
                {
                    Toast.makeText(CSDetails.this,"Please enter all fields.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    firebaseAuth = FirebaseAuth.getInstance();
                    if(firebaseAuth.getCurrentUser() != null)
                    {
                        String mail = firebaseAuth.getCurrentUser().getEmail();

                        tableName = custRegister.getOwnerTableNameByMail(mail);

                        Map<String,Object> updateInfo = new HashMap<>();

                        updateInfo.put("/Owners/"+tableName+"/csname",_CSName);
                        updateInfo.put("/Owners/"+tableName+"/latitude",_latitude);
                        updateInfo.put("/Owners/"+tableName+"/longitude",_longitude);

                        reference.updateChildren(updateInfo);
                        Toast.makeText(CSDetails.this,"CS Info Updated Successfully.",Toast.LENGTH_SHORT).show();

                        if(value.equals("valueFromOwnerDash"))
                        {
                            startActivity(new Intent(CSDetails.this, OwnerDash.class));
                            finish();
                        }
                        else if(value.equals("valueFromRegistration"))
                        {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(CSDetails.this,MainActivity.class));
                            finish();
                        }
                    }
                    else
                    {
                        Toast.makeText(CSDetails.this,"Error Occured.",Toast.LENGTH_SHORT).show();
                        //if this msg occured, usr should clear data of application
                        //go to main activity
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(value.equals("valueFromOwnerDash"))
        {
            startActivity(new Intent(CSDetails.this, OwnerDash.class));
            finish();
        }
        else if(value.equals("valueFromRegistration"))
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(CSDetails.this,MainActivity.class));
            finish();
        }
    }

    public boolean checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
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
}
