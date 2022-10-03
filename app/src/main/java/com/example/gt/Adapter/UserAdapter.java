package com.example.gt.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gt.LoginActivity;
import com.example.gt.MessageActivity;
import com.example.gt.R;
import com.example.gt.model.Chat;
import com.example.gt.model.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.http.POST;

public class UserAdapter  extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mcontext;
    private List<User> mUser;
    private  boolean ischat;
    private  boolean online;

    //add
    private List<Chat> mChat;
    DatabaseReference reference,newchatref;
    FirebaseAuth auth;

    String theLastmsg;
    String newchat;
    String date1sd;

    FirebaseUser fuser;
    Chat chat;



    public  UserAdapter(Context mcontext,List<User>mUser,boolean ischat){
        this.mcontext=mcontext;
        this.mUser=mUser;
        this.ischat=ischat;

    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;
        public ImageView imag_on;
        public ImageView imag_off;
        public TextView last_seen,clrc;
        ImageButton RecentChtbtn;



        @SuppressLint("ResourceAsColor")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username11);
            profile_image=itemView.findViewById(R.id.profile_image);

            imag_on=itemView.findViewById(R.id.img_on);
            imag_off=itemView.findViewById(R.id.img_off);
            last_seen=itemView.findViewById(R.id.last_seen);
            last_seen.setTextColor(R.color.purple_500);
            clrc=itemView.findViewById(R.id.clrc);
            RecentChtbtn=itemView.findViewById(R.id.recentChatbtn);
            clrc.setTextColor(R.color.purple_500);
               //  clrc.setText("sdf");
            // Log.d("DATE",clrc.toString());
            //clrc.setText("gfh");

            RecentChtbtn.setVisibility(View.GONE);


            clrc.setTextColor(mcontext.getColor(R.color.red));
            fuser = FirebaseAuth.getInstance().getCurrentUser();
        }
    }
    //check last msg
    private  void  lastMsg(String userid, TextView last_msg,TextView date,ImageButton imageButton2){
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        auth=FirebaseAuth.getInstance();
        //date.setTextColor(mcontext.getColor(R.color.red));
        theLastmsg="default";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.d("neww",snapshot.child("Date").getValue().toString());
                try {
                    Date time = snapshot.child("Date").getValue(Date.class);
                    //String v = snapshot.child("Date").getValue().toString();
                    SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("HH:mm:sss'Z'");
                    String now = ISO_8601_FORMAT.format(new Date(time.getTime()));
                    //date.setText(v.toString());
                    //SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
                   // date.setText(now);
                    // Log.d("STATE",holder.clrc.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
               // date.setText("24/7");
                // date.setText(snapshot.child("sender").getValue().toString());
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Chat chat  =snapshot1.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid())&& chat.getSender().equals(userid)||
                            chat.getReceiver().equals(userid)&&chat.getSender().equals(firebaseUser.getUid())){
                        theLastmsg= chat.getMessage();

                    if(chat.isIsseen()){
                           newchat = "";

                        }else {
                        if (chat.getReceiver().equals(firebaseUser.getUid())){
                            imageButton2.setVisibility(View.GONE);
                            newchat = "New Chat";
                            Toast.makeText(mcontext, "2", Toast.LENGTH_SHORT).show();
                        }else {
                            newchat = "";
                            imageButton2.setVisibility(View.VISIBLE);
                        }
                        //imageButton2.setVisibility(View.VISIBLE);

                           // newchat = "New Chat";
                           // date.setTextColor(R.color.red);
                           // Toast.makeText(mcontext, "2", Toast.LENGTH_SHORT).show();

                        }
                       // newchat = chat.getMessage();
                    }
                }
                imageButton2.setVisibility(View.VISIBLE);

                switch (theLastmsg){
                    case "default":
                        last_msg.setText("no message");
                        last_msg.setTextColor(R.color.purple_500);
                        break;
                    default:
                        last_msg.setText(theLastmsg);
                        date.setText(newchat);


                        break;
                }
                theLastmsg="default";
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(mcontext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        auth=FirebaseAuth.getInstance();
        User m = mUser.get(position);

        holder.RecentChtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                builder.setTitle("Recent Chat");
                builder.setMessage("Do you want to Delete Recent Chat?");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid()).child(m.getId()).removeValue();

                    }
                });

                builder.show();
            }
        });


        //show users
        holder.username.setText(m.getUsername());

        if (m.getImageURL().equals("default")) {
            holder.profile_image.setImageResource(R.drawable.profileus);
        } else {
            Glide.with(mcontext).load(m.getImageURL()).into(holder.profile_image);
        }

        newchatref=FirebaseDatabase.getInstance().getReference("Chats");
        //Toast.makeText(mcontext,chat.getMessageId(),Toast.LENGTH_SHORT).show();

        newchatref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

              //  Toast.makeText(mcontext,s,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (!ischat){
            lastMsg(m.getId(),holder.last_seen,holder.clrc,holder.RecentChtbtn);
           // holder.clrc.setTextColor(R.color.red);
            //holder.clrc.setTextColor(mcontext.getColor(R.color.red));
            //holder.clrc.setText("ede");
            //Log.d("S",holder.clrc.toString());

        }else {
            holder.last_seen.setVisibility(View.GONE);
            // lastMsg(m.getId(),holder.last_seen,holder.clrc);
        }
        if (ischat){
            holder.imag_on.setVisibility(View.GONE);
            holder.imag_off.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, MessageActivity.class);
                    intent.putExtra("userid",m.getId());
                    mcontext.startActivity(intent);
                }
            });
        }else {
            if (m.getStatus().equals("Online")){
                holder.imag_on.setVisibility(View.VISIBLE);
                holder.imag_off.setVisibility(View.GONE);
                online=true;
            }else {
                holder.imag_on.setVisibility(View.GONE);
                holder.imag_off.setVisibility(View.VISIBLE);
                online=false;
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, MessageActivity.class);
                    intent.putExtra("userid",m.getId());
                    mcontext.startActivity(intent);
                }
            });
        }



        //user select on user
    }
    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());
        if (position == mChat.size()-1){
            if (chat.isIsseen()) {
                holder.text_seen.setText("Seen");
            }else {
                holder.text_seen.setText("Delivered");
            }
        }else {
            holder.text_seen.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return mUser.size();
    }
}