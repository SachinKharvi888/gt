package com.example.gt.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gt.R;
import com.example.gt.addgrup;
import com.example.gt.model.Post;
import com.example.gt.model.User;
import com.example.gt.model.grpname;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Grpfrdlistadapter extends RecyclerView.Adapter<Grpfrdlistadapter.ViewHolder>{
    private Context mcontext;
    private List<User> mUser;
    Boolean frd = false;
    FirebaseAuth firebaseAuth;
    String grpId;
    grpname grpname1;
    String grpid;
    Intent intent;



    public  Grpfrdlistadapter(Context mcontext, List<User>mUser){
        this.mcontext=mcontext;
        this.mUser=mUser;

    }

    @NonNull
    @Override
    public Grpfrdlistadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.addgrpuser,parent,false);
        return new Grpfrdlistadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Grpfrdlistadapter.ViewHolder holder, int position) {
        final User user = mUser.get(position);

        intent = ((Activity) mcontext).getIntent();
        grpid = intent.getStringExtra("grpid1");

        Glide.with(mcontext).load(user.getImageURL()).into(holder.adduserimagegrp);
        holder.addusergrp.setText(user.getUsername());
        holder.admintext.setVisibility(View.GONE);
        holder.userbio.setVisibility(View.GONE);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("grp1").child(grpid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("user").child(user.getId()).exists()){
                    holder.removetogrp.setVisibility(View.VISIBLE);
                    holder.addtogrp.setVisibility(View.GONE);


                }else {
                    holder.removetogrp.setVisibility(View.GONE);

                    holder.addtogrp.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.addtogrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                       FirebaseDatabase.getInstance().getReference("grp1").child(grpid).child("user")
                                .child(user.getId()).setValue(true);
                FirebaseDatabase.getInstance().getReference("mygrp").child(user.getId())
                        .child(grpid).setValue(true);

            }
        });

        holder.removetogrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference("grp1").child(grpid).child("user")
                        .child(user.getId()).removeValue();


                FirebaseDatabase.getInstance().getReference("mygrp").child(user.getId())
                        .child(grpid).removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView addusergrp,admintext,userbio;
       public CircleImageView adduserimagegrp;
       public ImageButton addtogrp;
        public ImageButton removetogrp;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addusergrp=itemView.findViewById(R.id.usernamegrp);
            adduserimagegrp=itemView.findViewById(R.id.profile_imagegrp);
            addtogrp=itemView.findViewById(R.id.addtogrp);
            removetogrp=itemView.findViewById(R.id.rempvetogrp);
            admintext=itemView.findViewById(R.id.admintext);
            userbio=itemView.findViewById(R.id.userbio);


        }
    }
}
