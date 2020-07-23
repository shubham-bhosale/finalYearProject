package com.sart.evbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CSDetails extends AppCompatActivity
{
    EditText CSName,latitude,longitude;
    Button ok;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csdetails);

        CSName = findViewById(R.id.et_csname);
        latitude = findViewById(R.id.et_latitude);
        longitude = findViewById(R.id.et_longitude);
        ok = findViewById(R.id.okbtn);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
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
                        startActivity(new Intent(CSDetails.this, OwnerDash.class));
                        finish();
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

}
