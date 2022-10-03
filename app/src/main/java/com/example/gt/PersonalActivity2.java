package com.example.gt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gt.Adapter.UserAdapter;
import com.example.gt.model.Details;
import com.example.gt.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeUtils;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PersonalActivity2 extends AppCompatActivity {

    EditText nikename,qualification,address,email;
    EditText DoB;
    Spinner datepicker;
    Button Addbtn,change;
    TextView gender;
    RadioGroup gandergroup;
    RadioButton male,female;
    ImageButton editgender;
    TextView gender_show;
    LinearLayout genderopen;
    FirebaseAuth auth;
    DatabaseReference reference;

    FirebaseUser firebaseUser;

    Details detail;
    final Calendar myCalendar= Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal2);

        nikename=findViewById(R.id.nikename);
        qualification=findViewById(R.id.qualification);
        address=findViewById(R.id.address);
        email=findViewById(R.id.emailshow);
        datepicker=findViewById(R.id.datepicker);
        DoB=findViewById(R.id.dateofbirth);
        Addbtn=findViewById(R.id.addbtn);
        change=findViewById(R.id.change);

        gender=findViewById(R.id.gender);
        gandergroup=findViewById(R.id.gendergroup);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        editgender=findViewById(R.id.editgender);
        gender_show=findViewById(R.id.gender_show);
        genderopen=findViewById(R.id.genderhide);


        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        auth=FirebaseAuth.getInstance();
        fire(nikename,email,address,DoB,qualification,gender_show);
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        DoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PersonalActivity2.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        Addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nikename.getText().toString().isEmpty()){
                    Toast.makeText(PersonalActivity2.this,"Enter the name",Toast.LENGTH_SHORT).show();
                }else if (email.getText().toString().isEmpty()){
                    Toast.makeText(PersonalActivity2.this,"Enter the email",Toast.LENGTH_SHORT).show();

                }else if (DoB.getText().toString().isEmpty()) {
                    Toast.makeText(PersonalActivity2.this, "Enter the Date of Birth", Toast.LENGTH_SHORT).show();

                }else if (qualification.getText().toString().isEmpty()){
                    Toast.makeText(PersonalActivity2.this,"Enter the qualification",Toast.LENGTH_SHORT).show();

                }else if (address.getText().toString().isEmpty()){
                    Toast.makeText(PersonalActivity2.this,"Enter the email",Toast.LENGTH_SHORT).show();

                }else if (gandergroup.getCheckedRadioButtonId()==-1){
                    Toast.makeText(PersonalActivity2.this,"select gender",Toast.LENGTH_SHORT).show();

                }else {
                    //Personaldetails(nikename.getText().toString(),email.getText().toString(),qualification.getText().toString(),address.getText().toString(),DoB.getText().toString());

                    cheak();
                }
            }
        });
        editgender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderopen.setVisibility(View.VISIBLE);
                gender_show.setVisibility(View.GONE);
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String userid =firebaseUser.getUid();



                    String nike = nikename.getText().toString();


                    reference=FirebaseDatabase.getInstance().getReference("PersonalDetails").child(firebaseUser.getUid());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("nickName",nikename.getText().toString());
                    map.put("email",email.getText().toString());
                    map.put("Nationality",address.getText().toString());
                    map.put("DOB",DoB.getText().toString());
                    map.put("qualification",qualification.getText().toString());
                    String gendertxt;

                    if (male.isChecked()){
                        gendertxt = "male";
                    }else {
                        gendertxt="female";
                    }
                    map.put("gender",gendertxt.toString());
                    reference.updateChildren(map);
                    editgender.setVisibility(View.VISIBLE);
                    gender_show.setVisibility(View.VISIBLE);
                    genderopen.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


      //  readUser();


    }

    //dateselecter
    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        DoB.setText(dateFormat.format(myCalendar.getTime()));
    }


    private void cheak(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Cheak");
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String details = reference.push().getKey();
        String userid=firebaseUser.getUid();
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put("on","ON");
        reference.child(firebaseUser.getUid()).setValue(hashMap);

    }

    private void Personaldetails (String nikname,String email,String qualification,String Address,String DOB){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PersonalDetails");

        String gendertxt;
        FirebaseUser firebaseUser = auth.getCurrentUser();
        Date date = new Date();
        if (male.isChecked()){
            gendertxt = "male";
        }else {
            gendertxt="female";
        }

        String details = reference.push().getKey();
        String userid=firebaseUser.getUid();
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put("DetailsID",details);
        hashMap.put("id",userid);
        hashMap.put("nikaName",nikname);
        hashMap.put("email",email);
        hashMap.put("Address",Address);
        hashMap.put("DOB",DOB);
        hashMap.put("qualification",qualification);
        hashMap.put("gender",gendertxt);
        reference.child(firebaseUser.getUid()).setValue(hashMap);


    }
    private void fire(TextView nickname,TextView email ,TextView nationality,TextView DOB,TextView qualification,TextView gender){

        try {

            //  if (!FirebaseDatabase.getInstance().getReference("PersonalDetails").child(rev).equals(rev)){
            //   }else {
            try {
                reference= FirebaseDatabase.getInstance().getReference("PersonalDetails").child(firebaseUser.getUid());

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            nickname.setText(snapshot.child("nickName").getValue().toString());
                            email.setText(snapshot.child("email").getValue().toString());
                            nationality.setText(snapshot.child("Nationality").getValue().toString());
                            DOB.setText(snapshot.child("DOB").getValue().toString());
                            qualification.setText(snapshot.child("qualification").getValue().toString());
                            gender.setText(snapshot.child("gender").getValue().toString());
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

    private void readUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PersonalDetails");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for(DataSnapshot snapshot: datasnapshot.getChildren()){
                    Details m = snapshot.getValue(Details.class);
                  //  a.setText(snapshot.child("Bio").getValue().toString());


                    String nike = snapshot.child("nikaName").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}