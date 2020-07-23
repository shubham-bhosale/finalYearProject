package com.sart.evbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.opengl.EGLExt;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class OwnerScreen extends AppCompatActivity
{
    Spinner sp;
    TextView EVName,totalAvlCount;
    EditText totalSlots, busySlots;
    Button updateBtn;
    FloatingActionButton fab;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_screen);

        sp = findViewById(R.id.spnr_connectorType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(OwnerScreen.this,R.array.selectConnectorType, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp.setAdapter(adapter);

        EVName = findViewById(R.id.tv_evName);
        totalAvlCount = findViewById(R.id.tv_avl_cnt_of_slots);
        totalSlots = findViewById(R.id.et_totalCount);
        busySlots = findViewById(R.id.et_busySlots);

        updateBtn = findViewById(R.id.updateBtn);
        fab = findViewById(R.id.fab);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null)
        {
            String mail = firebaseAuth.getCurrentUser().getEmail();
            tableName = custRegister.getOwnerTableNameByMail(mail);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    String MYEVSTATION = dataSnapshot.child("Owners").child(tableName).child("csname").getValue().toString();
                    String AVLCOUNT = dataSnapshot.child("Owners").child(tableName).child("totalAvl").getValue().toString();
                    EVName.setText(MYEVSTATION);
                    totalAvlCount.setText(AVLCOUNT);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
        {
            Toast.makeText(OwnerScreen.this,"Error to get EV Station Name.",Toast.LENGTH_SHORT).show();
        }

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String connectorType = sp.getSelectedItem().toString();
                String _totalSlots = totalSlots.getText().toString().trim();
                String _busySlots = busySlots.getText().toString().trim();

                if(TextUtils.isEmpty(_totalSlots) || TextUtils.isEmpty(_busySlots))
                {
                    Toast.makeText(OwnerScreen.this,"Please enter all fields.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(Integer.parseInt(_busySlots) > Integer.parseInt(_totalSlots))
                    {
                        Toast.makeText(OwnerScreen.this,"Busy slots can't be more than total slots.",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(firebaseAuth.getCurrentUser() != null)
                        {
                            Map<String,Object> updateInfo = new HashMap<>();
                            String _avlSlots = getAvlSlots(_totalSlots,_busySlots);

                            updateInfo.put("/Owners/"+tableName+"/total_"+connectorType,_totalSlots);
                            updateInfo.put("/Owners/"+tableName+"/avl_"+connectorType,_avlSlots);
                            updateInfo.put("/Owners/"+tableName+"/bsy_"+connectorType,_busySlots);
                            //////////////////////////////////////////////////////////////////
                            reference.updateChildren(updateInfo);
                            updateTotalAvlSlots();
                            Toast.makeText(OwnerScreen.this,"Info Updated Successfully.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(OwnerScreen.this,"Error Occured.",Toast.LENGTH_SHORT).show();
                            //if this msg occured, usr should clear data of application
                            //go to main activity
                        }
                    }
                }
            }
        });
    }

    public String getAvlSlots(String totalSlots, String busySlots)
    {
        int i = Integer.parseInt(totalSlots) - Integer.parseInt(busySlots);
        return String.valueOf(i);
    }

    public void updateTotalAvlSlots()
    {
        int a;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String Total_AC_Type1 = dataSnapshot.child("Owners").child(tableName).child("total_AC_Type1").getValue().toString();
                String Total_AC_Type2 = dataSnapshot.child("Owners").child(tableName).child("total_AC_Type2").getValue().toString();
                String Total_AC_Type3 = dataSnapshot.child("Owners").child(tableName).child("total_AC_Type3").getValue().toString();

                String Total_DC_Type1 = dataSnapshot.child("Owners").child(tableName).child("total_DC_Type1").getValue().toString();
                String Total_DC_Type2 = dataSnapshot.child("Owners").child(tableName).child("total_DC_Type2").getValue().toString();
                String Total_DC_Type3 = dataSnapshot.child("Owners").child(tableName).child("total_DC_Type3").getValue().toString();

                int ac_t1,ac_t2,ac_t3,dc_t1,dc_t2,dc_t3;
                ac_t1 = Integer.parseInt(Total_AC_Type1);
                ac_t2 = Integer.parseInt(Total_AC_Type2);
                ac_t3 = Integer.parseInt(Total_AC_Type3);

                dc_t1 = Integer.parseInt(Total_DC_Type1);
                dc_t2 = Integer.parseInt(Total_DC_Type2);
                dc_t3 = Integer.parseInt(Total_DC_Type3);

                int Total = ac_t1+ac_t2+ac_t3+dc_t1+dc_t2+dc_t3;
                totalAvlCount.setText(String.valueOf(Total));
                Map<String,Object> updateInfo2 = new HashMap<>();
                updateInfo2.put("/Owners/"+tableName+"/totalAvl",Total);
                reference.updateChildren(updateInfo2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
