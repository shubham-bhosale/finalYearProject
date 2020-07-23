package com.sart.evbuddy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
{
    Context context;
    ArrayList<Owner> owners;

    public MyAdapter(Context c, ArrayList<Owner> o)
    {
        context = c;
        owners = o;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cs_info_for_customer,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position)
    {
        holder.tv_csName.setText(owners.get(position).getCSName());
        holder.tv_avlSlots.setText("Available Slots: "+owners.get(position).getTotalAvl());

        holder.seedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,owners.get(position).getName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,Csdetails2.class);
                intent.putExtra("OwnerData",owners.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return owners.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_csName,tv_avlSlots;
        ImageView seedetails;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_csName = itemView.findViewById(R.id.cs_name_for_customer);
            tv_avlSlots = itemView.findViewById(R.id.avl_slots_for_customer);
            seedetails = itemView.findViewById(R.id.iv_seeDatails);
        }
    }
}
