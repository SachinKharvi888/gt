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
import com.example.gt.MessageActivity;
import com.example.gt.R;
import com.example.gt.model.User;
import com.example.gt.model.grpname;
import com.example.gt.otherProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Grpmemb extends RecyclerView.Adapter<Grpmemb.ViewHolder>{
    private Context mcontext;
    private List<User> mUser;
    FirebaseAuth firebaseAuth;
    String grpid;
    Intent intent;
    FirebaseUser firebaseUser;



    public  Grpmemb(Context mcontext, List<User>mUser){
        this.mcontext=mcontext;
        this.mUser=mUser;

    }
    @NonNull
    @Override
    public Grpmemb.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.addgrpuser,parent,false);

        return new Grpmemb.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Grpmemb.ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = mUser.get(position);
        Glide.with(mcontext).load(user.getImageURL()).into(holder.adduserimagegrp);
        holder.addusergrp.setText(user.getUsername());
        holder.userbio.setText(user.getBio());
        Toast.makeText(mcontext, user.getBio()+"wfadfsdfsdf", Toast.LENGTH_SHORT).show();

        intent = ((Activity) mcontext).getIntent();
        grpid = intent.getStringExtra("grpid2");
        holder.addtogrp.setVisibility(View.GONE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("grp1").child(grpid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String admin = snapshot.child("admin").getValue().toString();

                if (admin.equals(user.getId())){
                   holder.admintext.setText("Admin");
                   holder.admintext.setVisibility(View.VISIBLE);
                }else {
                    holder.admintext.setText("");
                   // holder.admintext.setVisibility(View.GONE);

               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, MessageActivity.class);
                intent.putExtra("userid",user.getId());
                mcontext.startActivity(intent);
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
