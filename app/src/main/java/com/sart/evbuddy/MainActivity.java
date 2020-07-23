package com.sart.evbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
{
    Button lgn;
    TextView newreg;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lgn = findViewById(R.id.lgn_btn);
        newreg = findViewById(R.id.tv_newreg);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        if(firebaseAuth.getCurrentUser() != null)
        {
            onAuthSuccess(firebaseAuth.getCurrentUser());
        }

        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                //startActivity(new Intent(getApplicationContext(),Welcome.class));
                finish();
            }
        });

        newreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),register.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //System.exit(0);
    }

    public void onAuthSuccess(FirebaseUser user)
    {
        if(user != null)
        {
            DatabaseReference ref = mDatabase.getReference().child("AllUsers").child(user.getUid()).child("type");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);

                    if(Integer.parseInt(value) == 1)
                    {
                        Toast.makeText(MainActivity.this,"Successfully logged in as EV User.",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, CustomerHome.class));
                        finish();
                    }
                    else if(Integer.parseInt(value) == 2)
                    {
                        Toast.makeText(MainActivity.this,"Successfully logged in as CS Owner.",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, OwnerDash.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
