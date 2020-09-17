package com.sart.evbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class register extends AppCompatActivity
{
    ImageView iv_back,iv_next;
    RadioGroup rg;
    RadioButton rb;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        iv_back = findViewById(R.id.iv_back2);
        iv_next = findViewById(R.id.iv_nxtbtn);
        rg = findViewById(R.id.rg1);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = rg.getCheckedRadioButtonId();
                rb = findViewById(id);
                if(id == -1)
                {
                    Toast.makeText(register.this,"Nothing selected",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent i = new Intent(register.this,custRegister.class);
                    i.putExtra("usertype",rb.getText());
                    startActivity(i);
                    finish();
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
}
