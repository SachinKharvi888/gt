package com.example.gt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gt.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersonalInfoOther extends AppCompatActivity {
    TextView nike,email,qulific,DOB,gender,address;
    FirebaseAuth auth;
    DatabaseReference reference;
    ImageButton backprsonalinfobtn;

    FirebaseUser firebaseUser;


    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_other);
        nike=findViewById(R.id.nikenameshow);
        email=findViewById(R.id.Emailshow);

        qulific=findViewById(R.id.qualificationshow);
        DOB=findViewById(R.id.DOBshow);
        gender=findViewById(R.id.Gendershow);
        address=findViewById(R.id.Addressshow);
        backprsonalinfobtn=findViewById(R.id.backprsonalinfobtn);


        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        auth=FirebaseAuth.getInstance();
        Intent intent= getIntent();
        String rev;
        rev = intent.getStringExtra("userid");




       Log.d("Emp",rev);
        fire(rev);

        backprsonalinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalInfoOther.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });



    }

    private void fire(String rev){

        try {

          //  if (!FirebaseDatabase.getInstance().getReference("PersonalDetails").child(rev).equals(rev)){
         //   }else {
                try {
                    reference= FirebaseDatabase.getInstance().getReference("PersonalDetails").child(rev);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("nickName").getValue().toString().isEmpty()){
                                Toast.makeText(PersonalInfoOther.this, "empty", Toast.LENGTH_SHORT).show();
                                nike.setText("werwerrwe");
                            }else {
                                nike.setText(snapshot.child("nickName").getValue().toString());
                                //email.setText("email");
                                email.setText(snapshot.child("email").getValue().toString());
                                qulific.setText(snapshot.child("Nationality").getValue().toString());
                                DOB.setText(snapshot.child("DOB").getValue().toString());
                                gender.setText(snapshot.child("qualification").getValue().toString());
                                address.setText(snapshot.child("gender").getValue().toString());
                            }
                            //nike.setText(snapshot.child("nikaName").getValue().toString());



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } catch (Exception e) {
                    
                }

           // }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}