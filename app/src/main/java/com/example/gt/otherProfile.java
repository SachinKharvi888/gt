package com.example.gt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gt.model.Chat;
import com.example.gt.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class otherProfile extends AppCompatActivity {
    TextView getname,bio,likeShow,usertitle,personalinfo,report,block2,frtxt;
    ImageView imageView,imageView2;
    Button back;
    ImageButton addfrdbtn;
    ImageButton liker;
    LinearLayout show,privatelinear;
    CardView privacyaccount,privatelinear2,privatelinear3,blockbio,profilelikeinfoblock;
    Switch blockBtn;

    int i =    0;

    Boolean testclick=false;
    Boolean testclick2=false;
    Boolean showprivacy =false;
    Boolean showblock =false;
    Boolean frd = false;
    String rev;
    DatabaseReference likerref,newdata,cheak,frdrf;
    FirebaseUser fuser;
    User user;
    boolean block=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        getname=findViewById(R.id.getname);
        imageView=findViewById(R.id.imagee);
        imageView2=findViewById(R.id.imgeback);
        back=findViewById(R.id.back);
        show = findViewById(R.id.showimage);
        bio=findViewById(R.id.mybiosss);
        ImageButton button =findViewById(R.id.back1);
        usertitle=findViewById(R.id.userTitle);
        personalinfo=findViewById(R.id.personalInfo);
        report=findViewById(R.id.reportuser);
        privacyaccount=findViewById(R.id.prvicayaccount);
        privatelinear =findViewById(R.id.privatelinear);
        privatelinear2=findViewById(R.id.privatelinear2);
        privatelinear3=findViewById(R.id.privatelinear3);
        blockBtn=findViewById(R.id.blockUserbtn);
        block2=findViewById(R.id.block2);
        blockbio=findViewById(R.id.blockbio);
        profilelikeinfoblock=findViewById(R.id.profilelikeinfoblock);
        addfrdbtn=findViewById(R.id.addfrd);
        frtxt=findViewById(R.id.frdtxt);





        likeShow=findViewById(R.id.likeshow);
        liker = findViewById(R.id.liker);

        Intent intent= getIntent();

        rev = intent.getStringExtra("n4");

        newdata=FirebaseDatabase.getInstance().getReference();
        //blockBtn.setChecked(false);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Friends");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(fuser.getUid()).child("Follow").hasChild(rev)) {
                    addfrdbtn.setImageResource(R.drawable._how_to_reg_24);
                    frtxt.setText("Connected");
                }else {
                    addfrdbtn.setImageResource(R.drawable.frd_add_);
                    frtxt.setText("Connect");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        addfrdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frd=true;
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Friends");

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (frd == true) {

                            if (snapshot.child(fuser.getUid()).child("Follow").hasChild(rev)) {
                                FirebaseDatabase.getInstance().getReference("Friends").child(fuser.getUid())
                                        .child("Follow").child(rev).removeValue();
                                FirebaseDatabase.getInstance().getReference("Follower").child(rev).child(fuser.getUid()).removeValue();

                                frd=false;
                            } else {
                                FirebaseDatabase.getInstance().getReference("Friends").child(fuser.getUid())
                                        .child("Follow").child(rev).setValue(true);
                                FirebaseDatabase.getInstance().getReference("Follower").child(rev)
                                       .child(fuser.getUid()).setValue(true);
                            frd=false;
                        }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        });

        blockBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
             //   Log.i("boolean", String.valueOf(b));
              //  if(b) {
               //     block = true;
              //  }else
                //    block=false;
              //  userBlock();

             //   Log.i("boolean", String.valueOf(block));

                if (b){

                }else {
                    newdata.child(rev).child(fuser.getUid()).removeValue();
                }

            }

        });



        block2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testclick2=true;
                newdata = FirebaseDatabase.getInstance().getReference("Block12");

                newdata.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (testclick2==true){

                            //  Toast.makeText(getApplicationContext(), "adf", Toast.LENGTH_SHORT).show();
                            if (snapshot.child(fuser.getUid()).hasChild(rev)){
                                AlertDialog.Builder builder = new AlertDialog.Builder(otherProfile.this);
                                builder.setTitle("UnBlock");
                                builder.setMessage("Do you want to UnBlock User?");
                                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.setPositiveButton("UnBlock", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        newdata.child(fuser.getUid()).child(rev).removeValue();
                                        testclick2=false;
                                    }

                                });
                                builder.show();
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(otherProfile.this);
                                builder.setTitle("Block");
                                builder.setMessage("Do you want to Block User?");
                                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.setPositiveButton("Block", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        newdata.child(fuser.getUid()).child(rev).setValue(true);
                                        //   Toast.makeText(getApplicationContext(), "sdff", Toast.LENGTH_SHORT).show();
                                        testclick2=false;
                                    }
                                });
                                builder.show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        personalinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(otherProfile.this, PersonalInfoOther.class);
                String d;
                intent.putExtra("userid",rev.toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(otherProfile.this, MessageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });




        String name,bio1;

        Bundle b =getIntent().getBundleExtra("n2");
        byte[] bt=  b.getByteArray("photo");

        Bitmap btm = BitmapFactory.decodeByteArray(bt,0,bt.length);
        imageView.setImageBitmap(btm);
        imageView2.setImageBitmap(btm);

        name=intent.getStringExtra("n1");
        bio1=intent.getStringExtra("n3");



        intent.putExtra("other",rev);

        getname.setText(name);
        bio.setText(bio1);
        fuser= FirebaseAuth.getInstance().getCurrentUser();


        try1(rev);
        try2(rev,block2);
      //  block2.setText("sdas");

        // FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        //final String userid=firebaseUser.getUid();
      //  final String postkey=getRef(position).getKey();
        getlikebt(rev,fuser.getUid());

        newdata=FirebaseDatabase.getInstance().getReference("Block12");
        newdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(fuser.getUid()).child(rev).exists()){
                    block2.setText("UnBlock User");

                }else {
                    block2.setText("Block User");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        liker.setOnClickListener(this::onClick);


        if (likeShow.getText().toString().isEmpty()){
           // Toast.makeText(getApplicationContext(), "ee", Toast.LENGTH_SHORT).show();

        }else {
            int count=Integer.parseInt(likeShow.getText().toString());
            if (count<=1&&count>=10){
             //   Toast.makeText(getApplicationContext(), "10", Toast.LENGTH_SHORT).show();
            }else if (count<=11&&count>=20){
              //  Toast.makeText(getApplicationContext(), "20", Toast.LENGTH_SHORT).show();

            }
        }
        
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.setVisibility(View.VISIBLE);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.setVisibility(View.GONE);

            }
        });
    }

    private void userBlock() {
        if(block==true){
            Log.i("block", "Hai");
            newdata = FirebaseDatabase.getInstance().getReference("Block11");

            newdata.child(fuser.getUid()).child(rev).setValue(true);

        }
        else{

            newdata.child(fuser.getUid()).child(rev).removeValue();
        }
    }


    private void onClick(View view) {


        Intent intent= getIntent();
        String rev;
        rev = intent.getStringExtra("n4");
        //int i=1;
        // testclick = true;
        ///  likerref.child("like");
              //if (i == 0) {
            try {
                likerref=FirebaseDatabase.getInstance().getReference("profile").child(rev);
                HashMap<String, Object> map = new HashMap<>();
                map.put("like",fuser.getUid());

                //  likerref.child("profile").setValue(map);

               // likerref.push().setValue(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
           // i++;

      //  }  else if (i == 1) {
           // i=0;
      //  }


      

        testclick=true;
        likerref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (testclick==true){
                  //  Toast.makeText(getApplicationContext(), "adf", Toast.LENGTH_SHORT).show();
                     if (snapshot.child(fuser.getUid()).hasChild(rev)){
                    likerref.child(fuser.getUid()).removeValue();
                       //  Toast.makeText(getApplicationContext(), "wr", Toast.LENGTH_SHORT).show();
                    testclick=false;
                       }else {
                         likerref.child(fuser.getUid()).child(rev).setValue(true);
                      //   Toast.makeText(getApplicationContext(), "sdff", Toast.LENGTH_SHORT).show();
                       testclick=false;

                      }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getlikebt(final String postkey,final  String userid){
        likerref=FirebaseDatabase.getInstance().getReference("profile");
        likerref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postkey).hasChild(userid)){
                    int likecount=(int)snapshot.child(postkey).getChildrenCount();
                    likeShow.setText(likecount+"");
                    liker.setImageResource(R.drawable.likeimage4);

                    //title for user
                  if (likecount>=1&&likecount<2){
                        usertitle.setText("NOOB User");
                    } else if (likecount>=2&&likecount<50){
                       // Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
                        usertitle.setText("PRO User");

                    } else if (likecount>=51&&likecount<100){
                       // Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT).show();
                        usertitle.setText("PRO MAX User");
                    } else if (likecount>=100&&likecount<=200){
                        usertitle.setText("PRO MAX M1 User");

                    }
                } else {
                    int likecount2=(int)snapshot.child(postkey).getChildrenCount();
                    likeShow.setText(likecount2+"");
                    liker.setImageResource(R.drawable.likeimag3);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private  void try1(String rev){
        cheak= FirebaseDatabase.getInstance().getReference("Privacy").child(rev);
        cheak.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               String privcay;
               privcay = snapshot.child("Privacy").getValue().toString();
               //if (privcay=="ON"){
                //   personalinfo.setVisibility(View.GONE);
               //}
               if (snapshot.child("Privacy").getValue().equals("ON")){
                   privacyaccount.setVisibility(View.GONE);
                   privatelinear2.setVisibility(View.GONE);
                   privatelinear3.setVisibility(View.GONE);
                   privatelinear.setVisibility(View.VISIBLE);
                   showprivacy=true;
                   if (showprivacy==true){
                       Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();

                       privacyaccount.setVisibility(View.GONE);
                       privatelinear2.setVisibility(View.VISIBLE);
                       privatelinear3.setVisibility(View.VISIBLE);
                       privatelinear.setVisibility(View.VISIBLE);
                       blockbio.setVisibility(View.GONE);
                   }
                   if (showblock==true){
                       privatelinear.setVisibility(View.GONE);
                       privacyaccount.setVisibility(View.GONE);
                       privatelinear2.setVisibility(View.GONE);
                       privatelinear3.setVisibility(View.GONE);
                   }else {
                       privatelinear.setVisibility(View.GONE);
                   }


               }else if (snapshot.child("Privacy").getValue().equals("OFF")) {
                   privacyaccount.setVisibility(View.VISIBLE);
                   privatelinear2.setVisibility(View.VISIBLE);
                   privatelinear3.setVisibility(View.VISIBLE);
                   privatelinear.setVisibility(View.GONE);

                   if (showprivacy==true){
                       Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();


                      // privatelinear3.setVisibility(View.GONE);
                   }else {
                       privacyaccount.setVisibility(View.GONE);
                       privatelinear2.setVisibility(View.GONE);
                       privatelinear3.setVisibility(View.GONE);
                   }
                   if (showblock==false){
                       privacyaccount.setVisibility(View.VISIBLE);
                       privatelinear2.setVisibility(View.VISIBLE);
                       privatelinear3.setVisibility(View.VISIBLE);
                       privatelinear.setVisibility(View.GONE);

                   }

               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void try2(String rev,TextView txt){
        newdata=FirebaseDatabase.getInstance().getReference("Block12").child(rev);
        newdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(fuser.getUid()).exists()){

                        privacyaccount.setVisibility(View.GONE);
                        privatelinear2.setVisibility(View.GONE);
                        privatelinear3.setVisibility(View.GONE);
                        blockbio.setVisibility(View.GONE);
                        profilelikeinfoblock.setVisibility(View.GONE);
                        privatelinear.setVisibility(View.GONE);


                    showblock=true;

                    //imageView.setVisibility(View.GONE);
                   // imageView.setImageResource(R.drawable.privatedissatisfied_24);


                }else if (!snapshot.child(rev).exists()){


                    showblock=false;
                    if (showprivacy==true){
                        blockbio.setVisibility(View.VISIBLE);
                        privacyaccount.setVisibility(View.GONE);
                        privatelinear.setVisibility(View.VISIBLE);
                    }else {
                        privatelinear2.setVisibility(View.VISIBLE);
                        privatelinear3.setVisibility(View.VISIBLE);
                        // blockbio.setVisibility(View.VISIBLE);
                        profilelikeinfoblock.setVisibility(View.VISIBLE);
                        privacyaccount.setVisibility(View.VISIBLE);
                        privatelinear.setVisibility(View.GONE);
                    }
                       // privacyaccount.setVisibility(View.GONE);
                         //privatelinear.setVisibility(View.VISIBLE);


                      //  profilelikeinfoblock.setVisibility(View.VISIBLE);
                      //  privatelinear2.setVisibility(View.VISIBLE);
                      //  privatelinear3.setVisibility(View.VISIBLE);
                        //imageView.setVisibility(View.GONE);


                  //  privatelinear2.setVisibility(View.GONE);
                  //  privatelinear3.setVisibility(View.GONE);
                    if (showprivacy==true){
                        //privatelinear.setVisibility(View.VISIBLE);
                       // privacyaccount.setVisibility(View.VISIBLE);
                    }






                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}