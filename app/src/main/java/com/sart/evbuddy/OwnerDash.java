package com.sart.evbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

public class OwnerDash extends AppCompatActivity
{
    Spinner sp;
    CardView c;
    ImageView moreoptions;
    TextView EVStationName,tv_owner_name,tv_total_slots,tv_available_slots,tv_busy_slots;
    EditText totalSlots, busySlots;
    Button updateBtn;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_dash);

        sp = findViewById(R.id.spnr_connectorType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(OwnerDash.this,R.array.selectConnectorType, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp.setAdapter(adapter);

        moreoptions = findViewById(R.id.ic_more);

        c = findViewById(R.id.c5);
        EVStationName = findViewById(R.id.tv_evName);
        tv_total_slots = findViewById(R.id.tv_totalSlots);
        tv_available_slots = findViewById(R.id.tv_avlSlots);
        tv_busy_slots = findViewById(R.id.tv_busySlots);
        tv_owner_name = findViewById(R.id.tv_ownerName);

        totalSlots = findViewById(R.id.et_totalCount);
        busySlots = findViewById(R.id.et_busySlots);

        updateBtn = findViewById(R.id.updateBtn);


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
                    String OwnerName = dataSnapshot.child("Owners").child(tableName).child("name").getValue().toString();
                    String MYEVSTATION = dataSnapshot.child("Owners").child(tableName).child("csname").getValue().toString();
                    String AVLCOUNT = dataSnapshot.child("Owners").child(tableName).child("totalAvl").getValue().toString();
                    String BSYCOUNT = dataSnapshot.child("Owners").child(tableName).child("totalBsy").getValue().toString();
                    String TOTALCOUNT = dataSnapshot.child("Owners").child(tableName).child("totalSlots").getValue().toString();
                    tv_owner_name.setText(OwnerName);
                    EVStationName.setText(MYEVSTATION);
                    tv_total_slots.setText(TOTALCOUNT);
                    tv_available_slots.setText(AVLCOUNT);
                    tv_busy_slots.setText(BSYCOUNT);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
        {
            Toast.makeText(OwnerDash.this,"Error to get EV Station Name.",Toast.LENGTH_SHORT).show();
        }

        moreoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                PopupMenu popup = new PopupMenu(OwnerDash.this,moreoptions);
                popup.getMenuInflater().inflate(R.menu.popup_owner,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        if(menuItem.getItemId() == R.id.item_cs_details)
                        {
                            String valueFromOwnerDash = "valueFromOwnerDash";
                            Intent i = new Intent(OwnerDash.this,CSDetails.class);
                            i.putExtra("value",valueFromOwnerDash);
                            startActivity(i);
                            finish();
                        }
                        else if(menuItem.getItemId() == R.id.item_logout)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(OwnerDash.this);
                            builder.setTitle("Exit");
                            builder.setMessage("Do you really want to logout?");
                            builder.setCancelable(false);

                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(OwnerDash.this,MainActivity.class));
                                    finish();
                                }
                            });

                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            builder.create().show();
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String connectorType = sp.getSelectedItem().toString();
                String _totalSlots = totalSlots.getText().toString().trim();
                String _busySlots = busySlots.getText().toString().trim();

                if(TextUtils.isEmpty(_totalSlots) || TextUtils.isEmpty(_busySlots))
                {
                    Toast.makeText(OwnerDash.this,"Please enter all fields.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(Integer.parseInt(_busySlots) > Integer.parseInt(_totalSlots))
                    {
                        Toast.makeText(OwnerDash.this,"Busy slots can't be more than total slots.",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(OwnerDash.this,"Info Updated Successfully.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(OwnerDash.this,"Error Occured.",Toast.LENGTH_SHORT).show();
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

                String avl_AC_Type1 = dataSnapshot.child("Owners").child(tableName).child("avl_AC_Type1").getValue().toString();
                String avl_AC_Type2 = dataSnapshot.child("Owners").child(tableName).child("avl_AC_Type2").getValue().toString();
                String avl_AC_Type3 = dataSnapshot.child("Owners").child(tableName).child("avl_AC_Type3").getValue().toString();
                String avl_DC_Type1 = dataSnapshot.child("Owners").child(tableName).child("avl_DC_Type1").getValue().toString();
                String avl_DC_Type2 = dataSnapshot.child("Owners").child(tableName).child("avl_DC_Type2").getValue().toString();
                String avl_DC_Type3 = dataSnapshot.child("Owners").child(tableName).child("avl_DC_Type3").getValue().toString();

                String bsy_AC_Type1 = dataSnapshot.child("Owners").child(tableName).child("bsy_AC_Type1").getValue().toString();
                String bsy_AC_Type2 = dataSnapshot.child("Owners").child(tableName).child("bsy_AC_Type2").getValue().toString();
                String bsy_AC_Type3 = dataSnapshot.child("Owners").child(tableName).child("bsy_AC_Type3").getValue().toString();
                String bsy_DC_Type1 = dataSnapshot.child("Owners").child(tableName).child("bsy_DC_Type1").getValue().toString();
                String bsy_DC_Type2 = dataSnapshot.child("Owners").child(tableName).child("bsy_DC_Type2").getValue().toString();
                String bsy_DC_Type3 = dataSnapshot.child("Owners").child(tableName).child("bsy_DC_Type3").getValue().toString();

                int ac_t1,ac_t2,ac_t3,dc_t1,dc_t2,dc_t3;
                ac_t1 = Integer.parseInt(Total_AC_Type1);
                ac_t2 = Integer.parseInt(Total_AC_Type2);
                ac_t3 = Integer.parseInt(Total_AC_Type3);

                dc_t1 = Integer.parseInt(Total_DC_Type1);
                dc_t2 = Integer.parseInt(Total_DC_Type2);
                dc_t3 = Integer.parseInt(Total_DC_Type3);

                int avlAC1,avlAC2,avlAC3,avlDC1,avlDC2,avlDC3;
                int bsyAC1,bsyAC2,bsyAC3,bsyDC1,bsyDC2,bsyDC3;

                avlAC1 = Integer.parseInt(avl_AC_Type1);
                avlAC2 = Integer.parseInt(avl_AC_Type2);
                avlAC3 = Integer.parseInt(avl_AC_Type3);
                avlDC1 = Integer.parseInt(avl_DC_Type1);
                avlDC2 = Integer.parseInt(avl_DC_Type2);
                avlDC3 = Integer.parseInt(avl_DC_Type3);

                bsyAC1 = Integer.parseInt(bsy_AC_Type1);
                bsyAC2 = Integer.parseInt(bsy_AC_Type2);
                bsyAC3 = Integer.parseInt(bsy_AC_Type3);
                bsyDC1 = Integer.parseInt(bsy_DC_Type1);
                bsyDC2 = Integer.parseInt(bsy_DC_Type2);
                bsyDC3 = Integer.parseInt(bsy_DC_Type3);

                int Total_slots = ac_t1+ac_t2+ac_t3+dc_t1+dc_t2+dc_t3;//sum of all types of slots
                int total_avl_slots = avlAC1+avlAC2+avlAC3+avlDC1+avlDC2+avlDC3;//sum of all types of avl slots
                int total_bsy_slots = bsyAC1+bsyAC2+bsyAC3+bsyDC1+bsyDC2+bsyDC3;//sum of all types of bsy slots


                Map<String,Object> updateInfo2 = new HashMap<>();
                updateInfo2.put("/Owners/"+tableName+"/totalAvl",String.valueOf(total_avl_slots));
                updateInfo2.put("/Owners/"+tableName+"/totalBsy",String.valueOf(total_bsy_slots));
                updateInfo2.put("/Owners/"+tableName+"/totalSlots",String.valueOf(Total_slots));

                reference.updateChildren(updateInfo2);

                String AVLCOUNT = dataSnapshot.child("Owners").child(tableName).child("totalAvl").getValue().toString();
                String BSYCOUNT = dataSnapshot.child("Owners").child(tableName).child("totalBsy").getValue().toString();
                String TOTALCOUNT = dataSnapshot.child("Owners").child(tableName).child("totalSlots").getValue().toString();

                tv_total_slots.setText(TOTALCOUNT);
                tv_available_slots.setText(AVLCOUNT);
                tv_busy_slots.setText(BSYCOUNT);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
