package com.example.gt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gt.Adapter.Grpfrdlistadapter;
import com.example.gt.Adapter.Grpmemb;
import com.example.gt.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GrpEdit extends AppCompatActivity {

    TextView addusertogrp,totalgrpmember;
    TextView grpnameedit;
    Intent intent;
    FirebaseUser fuser;
    DatabaseReference reference;
    RecyclerView recyclerView;
    private List<String> followingList;
    List<User> tList;
    Grpmemb useradapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grp_edit);

        addusertogrp=findViewById(R.id.addusertogrp);
        grpnameedit=findViewById(R.id.grpnameedit1);
        totalgrpmember=findViewById(R.id.totalgrpmemb);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.grpmemberlist);
        intent = getIntent();
        String grpid = intent.getStringExtra("grpid2");
        tList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(GrpEdit.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        checkFollowing(grpid);
        totalgrpmembercount(totalgrpmember,grpid);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("grp1").child(grpid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                grpnameedit.setText(snapshot.child("grpname").getValue().toString());
                String admin =snapshot.child("admin").getValue().toString();
                if (admin.equals(fuser.getUid())) {
                    addusertogrp.setVisibility(View.VISIBLE);
                }else {
                    addusertogrp.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addusertogrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent=new Intent(GrpEdit.this,addgrup.class);
                intent.putExtra("grpid1",grpid);

                startActivity(intent);
            }
        });
    }
    private void checkFollowing(String grpid) {
        followingList = new ArrayList<>();
        followingList.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("grp1")
                .child(grpid)
                .child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    followingList.add(snapshot.getKey());
                }
                readUser();


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                tList.clear();
                for(DataSnapshot snapshot: datasnapshot.getChildren()){
                    User m = snapshot.getValue(User.class);
                    for (String id : followingList) {
                        if (m.getId().equals(id)) {
                            tList.add(m);
                        }
                        //postList.add(post);
                    }
                }
                useradapter=new Grpmemb(GrpEdit.this,tList);
                recyclerView.setAdapter(useradapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void totalgrpmembercount(TextView likes,String grpid){
        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("grp1").child(grpid).child("user");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    likes.setText(snapshot.getChildrenCount() + "");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}