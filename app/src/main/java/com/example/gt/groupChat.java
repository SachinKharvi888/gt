package com.example.gt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gt.Adapter.MessageAdapter;
import com.example.gt.Adapter.grpchatadapter;
import com.example.gt.model.Chat;
import com.example.gt.model.User;
import com.example.gt.model.grpchatmodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class groupChat extends AppCompatActivity {

    CircleImageView profil_image;
    TextView grpnametitle;
    FirebaseUser fuser;
    DatabaseReference reference;
    Intent intent,intent1;

    ImageButton btn_send;
    EditText text_send;

     grpchatadapter grpchatadapter;
    List<grpchatmodel> mchat;

    RecyclerView recyclerView;
    private List<String> followingList;

    String name;

    boolean notify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        intent = getIntent();
        String grpid = intent.getStringExtra("grpid");

        //intent = getIntent();
        //String userid = intent.getStringExtra("userid");
        User user;

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        // reference = FirebaseDatabase.getInstance().getReference("Users");

        recyclerView = findViewById(R.id.recycler_viewgroup);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        btn_send = findViewById(R.id.group_send);
        text_send = findViewById(R.id.group_txt);
        grpnametitle=findViewById(R.id.grpnametitle);
        //readMessage(fuser.getUid(),grpid,m.getImageURL());
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                name=user.getUsername();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("grp1").child(grpid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               grpnametitle.setText(snapshot.child("grpname").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        grpnametitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent=new Intent(groupChat.this,GrpEdit.class);
                intent.putExtra("grpid2",grpid);

                startActivity(intent);
            }
        });


        readMessage(grpid);


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                notify=true;
                try {
                    String msg = text_send.getText().toString();
                    if (!msg.equals("")){
                        sendMessage(fuser.getUid(),msg,grpid,name);
                        //  gettoken(msg, hisID, myImage, userid);
                       // sendnofi();
                        //
                        //
                        User user = null;
                        // sendNotification("sdsd",msg,user.getToken());

                    }else{
                        Toast.makeText(groupChat.this,"You can't send a empty Message",Toast.LENGTH_SHORT).show();

                    }
                    text_send.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private  void sendMessage(String sender,String message,String grpid,String name){
        User user;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("grpchat");

        ////
        Date date = new Date();
        //Message message = new Message(messageTxt, senderUid, date.getTime());
        //binding.messageBox.setText("");

        String references = FirebaseDatabase.getInstance().getReference().push().getKey();
        //reference.getReference().child("chats").child().updateChildren(lastMsgObj);
        //reference.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);


        //


        HashMap<String,Object> hashMap  =new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("name",name);
        hashMap.put("message",message);

        reference.child(grpid).push().setValue(hashMap);

    }
    private void checkFollowing(String  grpid) {
        followingList = new ArrayList<>();
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
                readMessage(grpid);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private  void  readMessage(String userid){
        mchat= new ArrayList<>();

        DatabaseReference reference =FirebaseDatabase.getInstance().getReference("grpchat").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot1: datasnapshot.getChildren()){
                    grpchatmodel grpchatmodel1= snapshot1.getValue(grpchatmodel.class);
                    ///if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid)||
                       //     chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){

                  //  for (String id : followingList) {

                       //// if (grpchatmodel1.getSender().equals(id)) {
                            mchat.add(grpchatmodel1);
                        }
                  //  }
                    //

                   // }
                    grpchatadapter=new grpchatadapter(groupChat.this,mchat);
                    recyclerView.setAdapter(grpchatadapter);
                }
           // }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }




}