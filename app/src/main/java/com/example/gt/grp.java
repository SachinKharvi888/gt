package com.example.gt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gt.Adapter.Grpfrdlistadapter;
import com.example.gt.Adapter.grpnameadapter;
import com.example.gt.model.User;
import com.example.gt.model.grpname;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class grp extends AppCompatActivity {
    ImageButton crtgrup;
    TextView mylist,grplist;
    RecyclerView recyclerView,recyclerView1;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    DatabaseReference reference;
    List<grpname> tList;
    List<grpname> tList2;

    FirebaseUser fuser;
    grpnameadapter useradapter;
    private List<String> followingList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grp);
        crtgrup=findViewById(R.id.crtgrup);
        recyclerView=findViewById(R.id.grplist);
        recyclerView1=findViewById(R.id.grplist2);
        mylist=findViewById(R.id.mygrouplist);
        grplist=findViewById(R.id.grpchatlisttext);

        crtgrup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(grp.this,creategrp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        mylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.GONE);
                recyclerView1.setVisibility(View.VISIBLE);
                Toast.makeText(grp.this, "my", Toast.LENGTH_SHORT).show();
            }
        });
        grplist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView1.setVisibility(View.GONE);
                Toast.makeText(grp.this, "gr", Toast.LENGTH_SHORT).show();

            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        tList = new ArrayList<>();
        tList2 = new ArrayList<>();




        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(grp.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView1.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(grp.this);
        mLayoutManager1.setReverseLayout(true);
        mLayoutManager1.setStackFromEnd(true);
        recyclerView1.setLayoutManager(mLayoutManager1);

        readUser();
        checkFollowing(firebaseUser.getUid());

    }
    private void checkFollowing(String  grpid) {
        followingList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("mygrp")
                .child(grpid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    followingList.add(snapshot.getKey());
                }

                readUser2();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readUser2() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("grp1");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                //Toast.makeText(grp.this,datasnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                tList2.clear();

                for(DataSnapshot snapshot: datasnapshot.getChildren()){
                    grpname m = snapshot.getValue(grpname.class);
                    for (String id : followingList){

                        if (m.getId().equals(id)){
                            tList2.add(m);

                        }
                        //postList.add(post);
                    }


                }
                useradapter=new grpnameadapter(grp.this,tList2);
                recyclerView1.setAdapter(useradapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void readUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("grp1");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                //Toast.makeText(grp.this,datasnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                tList.clear();

                for(DataSnapshot snapshot: datasnapshot.getChildren()){
                    grpname m = snapshot.getValue(grpname.class);
                    tList.add(m);


                }
                useradapter=new grpnameadapter(grp.this,tList);
                recyclerView.setAdapter(useradapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}