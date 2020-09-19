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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class custRegister extends AppCompatActivity
{
    ImageView iv_back;
    EditText name, mail, phone, password;
    Button register;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase mDatabase;
    int type=0;
    String usertype;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_register);

        Bundle bundle = getIntent().getExtras();
        usertype = bundle.getString("usertype");
        if(usertype.equals("EV User"))
        {
            type = 1;
        }
        else if(usertype.equals("Charging Station Owner"))
        {
            type = 2;
        }

        //Toast.makeText(custRegister.this,usertype+" "+String.valueOf(type),Toast.LENGTH_SHORT).show();

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(custRegister.this);
        iv_back = findViewById(R.id.iv_back3);
        name = findViewById(R.id.reg_cust_usrnm);
        mail = findViewById(R.id.reg_cust_mail);
        phone = findViewById(R.id.reg_cust_phone);
        password = findViewById(R.id.reg_cust_password);
        register = findViewById(R.id.okbtn);

        iv_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),register.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),register.class));
        finish();
    }

    public void onClickRegBtn(View view)
    {
        String email = mail.getText().toString().trim();
        String usrname =  name.getText().toString().trim();
        String phoneno = phone.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(usrname) || TextUtils.isEmpty(phoneno) || TextUtils.isEmpty(pass))
        {
            Toast.makeText(custRegister.this,"Please enter all credentials.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(phone.getText().toString().length() != 10)
            {
                Toast.makeText(custRegister.this,"Please enter a valid phone number.",Toast.LENGTH_SHORT).show();
            }
            else
            {
                progressDialog.setMessage("Registering User Statement..");
                progressDialog.show();

                //firebase authentication
                firebaseAuth.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if(task.isSuccessful())
                                {
                                    onAuthSuccess(task.getResult().getUser());
                                }
                                else
                                {
                                    Toast.makeText(custRegister.this,"Could not registered. Try again later.",Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
            }
        }

    }

    private void onAuthSuccess(FirebaseUser user)
    {
        // Write new user
        writeNewUser(user.getUid(), type, mail.getText().toString().trim(),phone.getText().toString().trim());

        //check if user type = 2 then add owner info into Owner table
        if(type == 2)
        {
            addOwnerInfo();
            Intent i = new Intent(custRegister.this, CSDetails.class);
            String valueFromRegistration = "valueFromRegistration";
            i.putExtra("value",valueFromRegistration);
            startActivity(i);
            finish();
        }
        else if(type == 1)
        {
            Toast.makeText(custRegister.this,"Successfully registered as EV User.",Toast.LENGTH_LONG).show();
            startActivity(new Intent(custRegister.this, Login.class));
            finish();
        }

    }

    private void writeNewUser(String userId, int type, String email, String PhoneNumber)
    {
        User user = new User(email,PhoneNumber,Integer.toString(type));
        mDatabase.getReference("AllUsers").child(userId).setValue(user);
    }
    private void addOwnerInfo()
    {
        String email = mail.getText().toString().trim();
        String usrname =  name.getText().toString().trim();
        String phoneno = phone.getText().toString().trim();
        String pass = password.getText().toString().trim();

        Owner owner = new Owner();

        owner.setName(usrname);
        owner.setPassword(pass);
        owner.setPhone(phoneno);
        owner.setMail(email);

        owner.setAvl_AC_Type1("0");
        owner.setAvl_AC_Type2("0");
        owner.setAvl_AC_Type3("0");

        owner.setBsy_AC_Type1("0");
        owner.setBsy_AC_Type2("0");
        owner.setBsy_AC_Type3("0");

        owner.setAvl_DC_Type1("0");
        owner.setAvl_DC_Type2("0");
        owner.setAvl_DC_Type3("0");

        owner.setBsy_DC_Type1("0");
        owner.setBsy_DC_Type2("0");
        owner.setBsy_DC_Type3("0");

        owner.setTotal_AC_Type1("0");
        owner.setTotal_AC_Type2("0");
        owner.setTotal_AC_Type3("0");

        owner.setTotal_DC_Type1("0");
        owner.setTotal_DC_Type2("0");
        owner.setTotal_DC_Type3("0");


        owner.setLatitude("Null");
        owner.setLongitude("Null");

        owner.setCSName("Not Available");
        owner.setTotalAvl("0");
        owner.setTotalBsy("0");
        owner.setTotalSlots("0");

        mDatabase.getReference("Owners").child(getOwnerTableNameByMail(email)).setValue(owner);
    }

    public static String getOwnerTableNameByMail(String mail)
    {
        String s = mail.replace(".","").replace("$","").replace("#","").replace("[","").replace("]","");
        int i = s.indexOf("@");
        return s.substring(0,i);
    }

}
