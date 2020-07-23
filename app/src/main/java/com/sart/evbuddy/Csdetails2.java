package com.sart.evbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Csdetails2 extends AppCompatActivity {
    Spinner sp;
    TextView csname, ownername;
    ImageView call,locateOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csdetails2);

        sp = findViewById(R.id.spnr_connectorType2);
        csname = findViewById(R.id.tv_evName2);
        ownername = findViewById(R.id.tv_ownerName2);
        call = findViewById(R.id.callToOwner);
        locateOwner = findViewById(R.id.locateOwner);

        sp = findViewById(R.id.spnr_connectorType2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Csdetails2.this, R.array.selectConnectorType, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp.setAdapter(adapter);

        Owner owner = (Owner) getIntent().getSerializableExtra("OwnerData");
        csname.setText(owner.CSName);
        ownername.setText(owner.name);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Owner owner2 = (Owner) getIntent().getSerializableExtra("OwnerData");
                Toast.makeText(Csdetails2.this, owner2.phone, Toast.LENGTH_SHORT).show();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + owner2.phone));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        ActivityCompat.requestPermissions(Csdetails2.this,new String[]{Manifest.permission.CALL_PHONE},1);
                    }
                }
                startActivity(callIntent);
            }
        });

        locateOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Owner o = (Owner) getIntent().getSerializableExtra("OwnerData");
                //Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
                //Uri gmmIntentUri = Uri.parse("geo:"+o.latitude+","+o.longitude);
                //Uri gmmIntentUri2 = Uri.parse("geo:0,0?q=lat,lan(Destination)");
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+o.latitude+","+o.longitude+"(Destination)");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                Owner owner3 = (Owner)getIntent().getSerializableExtra("OwnerData");
                if(sp.getSelectedItem().toString().equals("AC_Type1"))
                {
                    Toast.makeText(Csdetails2.this,"Available slots of selected\nconnector type : "+owner3.Avl_AC_Type1,Toast.LENGTH_SHORT).show();
                }
                else if(sp.getSelectedItem().toString().equals("AC_Type2"))
                {
                    Toast.makeText(Csdetails2.this,"Available slots of selected\nconnector type : "+owner3.Avl_AC_Type2,Toast.LENGTH_SHORT).show();
                }
                else if(sp.getSelectedItem().toString().equals("AC_Type3"))
                {
                    Toast.makeText(Csdetails2.this,"Available slots of selected\nconnector type : "+owner3.Avl_AC_Type3,Toast.LENGTH_SHORT).show();
                }
                else if(sp.getSelectedItem().toString().equals("DC_Type1"))
                {
                    Toast.makeText(Csdetails2.this,"Available slots of selected\nconnector type : "+owner3.Avl_DC_Type1,Toast.LENGTH_SHORT).show();
                }
                else if(sp.getSelectedItem().toString().equals("DC_Type2"))
                {
                    Toast.makeText(Csdetails2.this,"Available slots of selected\nconnector type : "+owner3.Avl_DC_Type2,Toast.LENGTH_SHORT).show();
                }
                else if(sp.getSelectedItem().toString().equals("DC_Type3"))
                {
                    Toast.makeText(Csdetails2.this,"Available slots of selected\nconnector type : "+owner3.Avl_DC_Type3,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
