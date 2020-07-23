package com.sart.evbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity
{
    ImageView iv;
    TextView newreg;
    EditText etmail,password;
    Button login;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iv = findViewById(R.id.iv_back);
        newreg = findViewById(R.id.tv_newreg2);
        etmail = findViewById(R.id.lgn_usrnm);
        password = findViewById(R.id.lgn_pswd);
        login = findViewById(R.id.lgn_btn2);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        if(firebaseAuth.getCurrentUser() != null)
        {
            onAuthSuccess(firebaseAuth.getCurrentUser());
        }

        progressDialog = new ProgressDialog(Login.this);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        newreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),register.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String email = etmail.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass))
                {
                    Toast.makeText(Login.this,"Please enter all credentials.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog.setMessage("Logging in. Please Wait.");
                    progressDialog.show();

                    firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            progressDialog.dismiss();
                            if(task.isSuccessful())
                            {
                                onAuthSuccess(task.getResult().getUser());
                            }
                            else
                            {
                                Toast.makeText(Login.this,"Could not login. Try again.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
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
                        Toast.makeText(Login.this,"Successfully logged in as EV User.",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Login.this, CustomerHome.class));
                        finish();
                    }
                    else if(Integer.parseInt(value) == 2)
                    {
                        Toast.makeText(Login.this,"Successfully logged in as CS Owner.",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Login.this, OwnerDash.class));
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