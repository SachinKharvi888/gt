package com.example.gt;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.gt.Adapter.UserAdapter;
import com.example.gt.model.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FrdlistActiviy extends AppCompatActivity {
    private RecyclerView recyclerView,followerrcy;
    private List<User> tList;
    private List<User> tList2;
    private List<String> followingList;
    private List<String> followingList2;
    private UserAdapter useradapter;
    FirebaseUser fuser;
    DatabaseReference reference;
    TextView followtxt,followerstxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frdlist_activiy);
        recyclerView = findViewById(R.id.frdRecle);
        followerrcy = findViewById(R.id.folloingRecyc);
        followtxt=findViewById(R.id.followtxt);
        followerstxt=findViewById(R.id.followerstxt);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(FrdlistActiviy.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        followerrcy.setHasFixedSize(true);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(FrdlistActiviy.this);
        LayoutManager.setReverseLayout(true);
        LayoutManager.setStackFromEnd(true);
        followerrcy.setLayoutManager(LayoutManager);

        followtxt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view){
                recyclerView.setVisibility(View.VISIBLE);
                followerrcy.setVisibility(View.GONE);
                followtxt.setTextColor(R.color.red);


            }
        });
        followerstxt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view){
                recyclerView.setVisibility(View.GONE);
                followerrcy.setVisibility(View.VISIBLE);
                followerstxt.setTextColor(R.color.purple_200);
            }
        });

       // useradapter= new UserAdapter(FrdlistActiviy.this,tList,false);
       // recyclerView.setAdapter(useradapter);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        tList = new ArrayList<>();
        tList2 = new ArrayList<>();
        //readUser();
        checkFollowing();
        checkFollowing2();

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
    private void checkFollowing2() {
        followingList2 = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follower").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    followingList2.add(snapshot.getKey());
                  //  String s = snapshot.getChildren()

                        Toast.makeText(FrdlistActiviy.this, followingList2.toString(), Toast.LENGTH_SHORT).show();

                }
             // String s = dataSnapshot.getChildren().toString();


               readUser2();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void readUser2() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                tList2.clear();
                for(DataSnapshot snapshot: datasnapshot.getChildren()){
                    User m = snapshot.getValue(User.class);
                    for (String id : followingList2){
                        if (m.getId().equals(id)){
                            tList2.add(m);

                        }
                    }
                }
                useradapter=new UserAdapter(FrdlistActiviy.this,tList2,true);
                followerrcy.setAdapter(useradapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                tList.clear();
                for(DataSnapshot snapshot: datasnapshot.getChildren()){
                    User m = snapshot.getValue(User.class);
                    for (String id : followingList){
                        if (m.getId().equals(id)){
                            tList.add(m);

                        }
                    }
                }
                useradapter=new UserAdapter(FrdlistActiviy.this,tList,true);
                recyclerView.setAdapter(useradapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}