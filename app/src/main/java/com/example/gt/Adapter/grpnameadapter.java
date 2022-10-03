package com.example.gt.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gt.MessageActivity;
import com.example.gt.R;
import com.example.gt.groupChat;
import com.example.gt.model.Post;
import com.example.gt.model.User;
import com.example.gt.model.grpname;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class grpnameadapter extends RecyclerView.Adapter<grpnameadapter.ViewHolder> {

    private Context mContext;
    private List<grpname> mPosts;
    FirebaseAuth auth;
    FirebaseUser fuser;

    public grpnameadapter(Context context, List<grpname> posts){
        mContext = context;
        mPosts = posts;
    }


    @NonNull
    @Override
    public grpnameadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.grpnamecreate,parent,false);
        return new grpnameadapter.ViewHolder(view);

    }




    @Override

    public void onBindViewHolder(@NonNull grpnameadapter.ViewHolder holder, int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        final grpname user = mPosts.get(position);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("grp1").child(user.getId()).child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(fuser.getUid()).exists()){
                    holder.addtogrp.setVisibility(View.VISIBLE);
                    FirebaseDatabase.getInstance().getReference("group").child(fuser.getUid()).child(user.getId())
                            .setValue(true);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext,groupChat.class);
                            intent.putExtra("grpid",user.getId());
                            mContext.startActivity(intent);
                        }
                    });
                    //

                }else {

                    holder.itemView.setVisibility(View.VISIBLE);
                    holder.addtogrp.setVisibility(View.GONE);
                    holder.addusergrp1.setText("Your Not In");
                    holder.addusergrp1.setTextColor(R.color.red);
                    // Toast.makeText(mContext, "your not in this group", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Glide.with().load(user.getImageURL()).into(holder.adduserimagegrp);
        holder.addusergrp.setText(user.getGrpname());

        //holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    }


    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView addusergrp,addusergrp1;
        public CircleImageView adduserimagegrp;
        public CircleImageView addtogrp;
        RelativeLayout hideview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addusergrp=itemView.findViewById(R.id.usernamegrp1);
            addusergrp1=itemView.findViewById(R.id.usernamegrp11);

            adduserimagegrp=itemView.findViewById(R.id.profile_imagegrp1);
            addtogrp=itemView.findViewById(R.id.addtogrp1);
            hideview=itemView.findViewById(R.id.hideview);
          ///  itemView.setVisibility(View.GONE);
        }
    }

}
