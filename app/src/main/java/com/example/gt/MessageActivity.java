package com.example.gt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.gt.Adapter.MessageAdapter;
import com.example.gt.model.Chat;
import com.example.gt.model.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
  CircleImageView profil_image;
  TextView usrname,active;
  FirebaseUser fuser;
  DatabaseReference reference,newdata;
  Intent intent;

  ImageButton btn_send;
  EditText text_send;

  MessageAdapter messageAdapter;
  List<Chat> mchat;
  Chat chat;

  ValueEventListener seenListener;

  RecyclerView recyclerView;



  String revi, name1,img,bio1;
  boolean notify = false;
  private String hisID, hisImage, myID, chatID = null, myImage, myName, audioPath;





  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_message);

    recyclerView=findViewById(R.id.recycler_views);
    recyclerView.setHasFixedSize(true);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
    linearLayoutManager.setStackFromEnd(true);
    recyclerView.setLayoutManager(linearLayoutManager);

    profil_image=findViewById(R.id.userporfile1);
    usrname = findViewById(R.id.username1);
    btn_send=findViewById(R.id.btn_send);
    text_send=findViewById(R.id.txt_btn);
    active=findViewById(R.id.active);




    usrname.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MessageActivity.this, otherProfile.class);
        try {

          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          Bitmap bitmap = ((BitmapDrawable)profil_image.getDrawable()).getBitmap();
          ByteArrayOutputStream strea = new ByteArrayOutputStream();
          bitmap.compress(Bitmap.CompressFormat.PNG,100,strea);
          byte[] bytes =strea.toByteArray();
          Bundle bundle  =new Bundle();
          bundle.putByteArray("photo",bytes);

          if (bundle!=null){
            intent.putExtra("n2",bundle);
          }else {
            Toast.makeText(MessageActivity.this,"no image" ,Toast.LENGTH_SHORT).show();

          }

          intent.putExtra("n1",name1);
          intent.putExtra("n3",bio1);
          intent.putExtra("n4",revi);
          intent.putExtra("rev3",revi);

          startActivity(intent);

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    intent = getIntent();
    String userid = intent.getStringExtra("userid");

    fuser= FirebaseAuth.getInstance().getCurrentUser();
    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);



    profil_image.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (profil_image!=null){
          reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              User m = snapshot.getValue(User.class);
              String s;
              // s= m.getUsername().toString();


              try {
                Intent intent = new Intent(MessageActivity.this, otherProfile.class);
                Bitmap bitmap = ((BitmapDrawable)profil_image.getDrawable()).getBitmap();
                ByteArrayOutputStream strea = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,strea);
                byte[] bytes =strea.toByteArray();
                Bundle bundle  =new Bundle();
                bundle.putByteArray("photo",bytes);

                if (bundle!=null){
                  intent.putExtra("n2",bundle);
                }else {
                  Toast.makeText(MessageActivity.this,"no image" ,Toast.LENGTH_SHORT).show();

                }

                intent.putExtra("n1",name1);
                intent.putExtra("n3",bio1);
                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // startActivity(intent);

              } catch (Exception e) {
                e.printStackTrace();
              }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
          });
        }else {
          Toast.makeText(MessageActivity.this,"no image found!",Toast.LENGTH_SHORT).show();

        }


      }
    });



    btn_send.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        notify=true;
        try {
          String msg = text_send.getText().toString();
          if (!msg.equals("")){
            sendMessage(fuser.getUid(),userid,msg);
            //  gettoken(msg, hisID, myImage, userid);
            sendnofi();
            //



            //
            User user = null;
            // sendNotification("sdsd",msg,user.getToken());

          }else{
            Toast.makeText(MessageActivity.this,"You can't send a empty Message",Toast.LENGTH_SHORT).show();

          }
          text_send.setText("");
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
    });

    reference.addValueEventListener(new ValueEventListener() {
      @SuppressLint("ResourceAsColor")
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        User m = snapshot.getValue(User.class);
        usrname.setText(m.getUsername());

        if (m.getStatus().equals("Online")){
          active.setText("Active");
          active.setTextColor(R.color.black);

        }else {
          active.setText("Offline");
          //active.setTextColor(R.color.red);
        }
        name1=m.getUsername().toString();
        bio1=snapshot.child("Bio").getValue().toString();
        revi=snapshot.child("id").getValue().toString();

         // chat.setMessageId(snapshot.getKey());

        if (m.getImageURL().equals("default")) {
          profil_image.setImageResource(R.drawable.profileus);
        }else {
          //change
          Glide.with(getApplicationContext()).load(m.getImageURL()).into(profil_image);
          img = profil_image.toString();
        }
        readMessage(fuser.getUid(),userid,m.getImageURL());
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
      }
    });
    SeenMessage(userid);

  }

  private  void  SeenMessage(final String userid){
    reference = FirebaseDatabase.getInstance().getReference("Chats");
    seenListener=reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        for(DataSnapshot snapshot1:snapshot.getChildren()){
          Chat chat = snapshot1.getValue(Chat.class);
          if(chat.getReceiver().equals(fuser.getUid())&& chat.getSender().equals(userid)){
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("isseen",true);
            snapshot1.getRef().updateChildren(hashMap);
            messageAdapter=new MessageAdapter(MessageActivity.this,chat.getSender(),chat.getReceiver());
          }
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });


  }

  private void sendNotification(String name, String message, String token) {
    try {
      RequestQueue queue = Volley.newRequestQueue(this);

      String url = "https://fcm.googleapis.com/fcm/send";

      JSONObject data = new JSONObject();
      data.put("title", name);
      data.put("body", message);
      JSONObject notificationData = new JSONObject();
      notificationData.put("notification", data);
      notificationData.put("to",token);

      JsonObjectRequest request = new JsonObjectRequest(url, notificationData
              , new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
          // Toast.makeText(ChatActivity.this, "success", Toast.LENGTH_SHORT).show();
        }
      }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
          //Toast.makeText(ChatActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
      }) {
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
          HashMap<String, String> map = new HashMap<>();
          String key = "Key=AAAASn2Fs4A:APA91bGdTVxFBP-V0NN_zLjQTUb7yr9Shy0sYcSN2MvHxTksz11FktDxUt44hKD3CyD2ghCX61RGJW25F0mBPpTBrSArmo9emaKP8HqRQGe5A8vrdygKbY-Kfph9YvaeQnPmif5a1Zr7";
          map.put("Content-Type", "application/json");
          map.put("Authorization", key);

          return map;
        }
      };

      queue.add(request);


    } catch (Exception ex) {

    }


  }


  private  void sendMessage(String sender,String receiver,String message){
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

    ////
    Date date = new Date();
    //Message message = new Message(messageTxt, senderUid, date.getTime());
    //binding.messageBox.setText("");
    //String messageid = FirebaseDatabase.getInstance().getReference().push().getKey();
    //DatabaseReference referenc = FirebaseDatabase.getInstance().getReference();

    String messageid = reference.push().getKey();

    //chat.setMessageId(messageid);
    //reference.getReference().child("chats").child().updateChildren(lastMsgObj);
    //reference.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);
    //

    HashMap<String,Object> hashMap  =new HashMap<>();
    hashMap.put("sender",sender);
    hashMap.put("receiver",receiver);
    hashMap.put("messageid",messageid);
    hashMap.put("message",message);
    hashMap.put("isseen",false);
    hashMap.put("feeling",0);
    hashMap.put("Date",date.getTime());
    reference.child(messageid).setValue(hashMap);

    ////
   // reference.getDatabase().getReference().child("Chats").updateChildren(hashMap);
   // reference.getDatabase().getReference().child("Chats").child(receiver).updateChildren(hashMap);
    //

    final String msg = message;

    FirebaseDatabase.getInstance().getReference("Chatlist").child(receiver).child(fuser.getUid()).setValue(true);
    FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid()).child(receiver).setValue(true);



    reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
    reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        if (notify) {

        }
        notify = false;
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });


  }

  private  void  readMessage(String myid,String userid,String imageurl){
    mchat= new ArrayList<>();
    reference =FirebaseDatabase.getInstance().getReference("Chats");
    reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot datasnapshot) {
        mchat.clear();
        for(DataSnapshot snapshot1: datasnapshot.getChildren()){
          Chat chat= snapshot1.getValue(Chat.class);
          if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid)||
                  chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
            mchat.add(chat);

          }
          messageAdapter=new MessageAdapter(MessageActivity.this,mchat,imageurl);
          recyclerView.setAdapter(messageAdapter);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });




  }

  /////////status online
  private void  status(String status){
    reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

    HashMap<String,Object>hashMap =  new HashMap<>();
    hashMap.put("status",status);

    reference.updateChildren(hashMap);
  }

  @Override
  protected void onResume() {
    super.onResume();
    status("Online");
   // active.setText("werew");
  }

  @Override
  protected void onPause() {
    super.onPause();
    reference.removeEventListener(seenListener);
    status("Offline");
    //active.setText("werfsdrfew");

  }





  private void sendnofi(){
    String ms = "";
    NotificationCompat.Builder builder = new NotificationCompat.Builder(MessageActivity.this)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("new ")
            .setContentText(ms)
            .setAutoCancel(true);

    NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
  }







}