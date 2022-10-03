package com.example.gt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.gt.Adapter.Grpfrdlistadapter;
import com.example.gt.Adapter.UserAdapter;
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

public class addgrup extends AppCompatActivity {
     RecyclerView recyclerView,followerrcy;
    List<User> tList;
    DatabaseReference reference,newchatref;
    FirebaseUser fuser;
    Grpfrdlistadapter useradapter;
    Intent intent,intent2;
    private List<String> followingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgrup);
        recyclerView = findViewById(R.id.userlistgrp);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        tList = new ArrayList<>();
        intent = getIntent();
        String grpid = intent.getStringExtra("grpid1");

        //intent2.putExtra("grdid2",grpid);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(addgrup.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        checkFollowing();
    }
    private void checkFollowing() {
        followingList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Friends")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Follow");
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
                useradapter=new Grpfrdlistadapter(addgrup.this,tList);
                recyclerView.setAdapter(useradapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}