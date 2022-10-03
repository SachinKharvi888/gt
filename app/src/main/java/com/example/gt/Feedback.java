package com.example.gt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.gt.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.awt.font.TextAttribute;
import java.util.HashMap;

public class Feedback extends AppCompatActivity {
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    DatabaseReference reference;
    EditText feed;
    ImageButton back;
    Button feedbtn;
    User user;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feed=findViewById(R.id.feedbacktxt);
        back=findViewById(R.id.feed_back);
        feedbtn=findViewById(R.id.sendFeed);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        auth=FirebaseAuth.getInstance();


        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("username").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        feedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference= FirebaseDatabase.getInstance().getReference("FeedBack").child(firebaseUser.getUid());
                HashMap<String, Object> map = new HashMap<>();
               map.put("Username",name);
                map.put("feed",feed.getText().toString());
                reference.setValue(map);
                Toast.makeText(Feedback.this, "FeedBack Sending", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Feedback.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


    }
}