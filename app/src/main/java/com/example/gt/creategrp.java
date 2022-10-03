package com.example.gt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class creategrp extends AppCompatActivity {
    EditText creategrpname;
    Button createbtn;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategrp);

        creategrpname=findViewById(R.id.grpnameedit);
        createbtn=findViewById(R.id.createbtn);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("grp1");
                String grpid = reference.push().getKey();
                HashMap<String,Object> hashMap  =new HashMap<>();
                hashMap.put("id",grpid);
                hashMap.put("admin",firebaseUser.getUid());
                hashMap.put("grpname",creategrpname.getText().toString());

                reference.child(grpid).setValue(hashMap);


                FirebaseDatabase.getInstance().getReference("grp1").child(grpid).child("user")
                        .child(firebaseUser.getUid()).setValue(true);

                Toast.makeText(creategrp.this, creategrpname.getText().toString()+"  Created", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(creategrp.this,grp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
    }
}