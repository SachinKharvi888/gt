package com.example.gt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText username,email,password;
    TextView log;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference,newdata;
    ProgressBar prog1;
    FirebaseUser firebaseUser,fuser;
    String uniquename;
    Boolean s =false;




    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser !=null){
            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
           // Toast.makeText(RegisterActivity.this,"Hello"+firebaseUser.getUid(),Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username=findViewById(R.id.name);
        email=findViewById(R.id.email);
        prog1=findViewById(R.id.prog1);



        newdata=FirebaseDatabase.getInstance().getReference();
        password=findViewById(R.id.password);
        auth=FirebaseAuth.getInstance();
        btn_register=findViewById(R.id.register);
        log=findViewById(R.id.log);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prog1.setVisibility(View.VISIBLE);
                String txtname= username.getText().toString();
                String txtemail= email.getText().toString();
                String txtpassword= password.getText().toString();
                if (!validateUsername()) {
                    prog1.setVisibility(View.GONE);

                    return;
                }
                else if (TextUtils.isEmpty(txtname)||TextUtils.isEmpty(txtemail)||TextUtils.isEmpty(txtpassword)) {
                    prog1.setVisibility(View.GONE);

                    Toast.makeText(RegisterActivity.this," all filed are reguired",Toast.LENGTH_SHORT).show();
                    return;

                }else  if (txtpassword.length()  < 6){
                    prog1.setVisibility(View.GONE);

                    Toast.makeText(RegisterActivity.this,"password must at 6 chara",Toast.LENGTH_SHORT).show();
                    return;

                }else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("unique").child(username.getText().toString());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){

                                Toast.makeText(RegisterActivity.this,"UserName Already Exists",Toast.LENGTH_SHORT).show();
                                username.setError("Exists");
                                username.getText().clear();
                                prog1.setVisibility(View.GONE);
                                return;
                            }else {
                                register(txtname,txtemail,txtpassword);
                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

    }

    private void register(String username1,String email,String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid =firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                    HashMap<String ,String> hashMap = new HashMap<>();
                    hashMap.put("id",userid);
                    hashMap.put("username",username1);
                    hashMap.put("imageURL","default");
                    hashMap.put("Bio","default");
                    hashMap.put("status","Offline");
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Personaldetails();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("unique");
                                reference.child(username1).setValue(true);

                                Privacy();
                                userBlock();
                                Toast.makeText(RegisterActivity.this,"suecss",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                }else{
                    prog1.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegisterActivity.this,"you can't use email and password",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void userBlock() {

    }

    private void Personaldetails (){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PersonalDetails");

        String gendertxt;
        FirebaseUser firebaseUser = auth.getCurrentUser();
        Date date = new Date();


        String details = reference.push().getKey();
        String userid=firebaseUser.getUid();
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put("DetailsID",details);
        hashMap.put("id",userid);
        hashMap.put("nickName","");
        hashMap.put("email","");
        hashMap.put("Nationality","");
        hashMap.put("DOB","");
        hashMap.put("qualification","");
        hashMap.put("gender","");
        reference.child(firebaseUser.getUid()).setValue(hashMap);

        }



    private void Privacy(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Privacy");
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String details = reference.push().getKey();
        String userid=firebaseUser.getUid();
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put("Privacy","OFF");
        reference.child(firebaseUser.getUid()).setValue(hashMap);

        //block();

    }
    private boolean unique(String txt){
        s=false;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("unique").child(txt);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    s=true;
                    uniquename=snapshot.getValue().toString();
                    Toast.makeText(RegisterActivity.this,"tr",Toast.LENGTH_SHORT).show();
                    username.setError("Exists");
                    return;
                }
                return;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return false;
    }
    private void block(){
        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Block");
        FirebaseUser firebaseUser = auth.getCurrentUser();
        HashMap<String, Object> map = new HashMap<>();
        map.put("Block","OFF");
        map.put("user","hi");
        reference1.child(firebaseUser.getUid()).setValue(map);
    }
    private Boolean validateUsername() {
        String val = username.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        }
        else if (val.length() >= 15) {
            username.setError("Username too long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            username.setError("Spaces are not allowed");
            return false;
        } else {
            username.setError(null);
           // username.setEnabled(false);
        return true;
        }
    }

}