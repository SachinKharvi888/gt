package com.example.gt.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gt.MessageActivity;
import com.example.gt.R;
import com.example.gt.model.Chat;
import com.example.gt.model.User;
import com.example.gt.otherProfile;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
  public  static  final int MSG_TYPE_LEFT = 0;
  public  static  final int MSG_TYPE_RIGHT = 1;

  String senderRoom;
  String reciverRoom;

  private Context mcontext;
  private List<Chat> mChat;
  private String imagUrl;

  private List<User> mUser;
  private  boolean ischat;

  FirebaseUser fuser;
  DatabaseReference reference,msgDelete;
  DatabaseReference referenc;
  Chat chat ;

  public MessageAdapter(Context mcontext, List<Chat>mChat,String imagUrl){
    this.imagUrl=imagUrl;
    this.mcontext=mcontext;
    this.mChat=mChat;



  }

  public MessageAdapter(MessageActivity mcontext, String senderRoom, String reciverRoom){
    this.senderRoom=senderRoom;
    this.reciverRoom=reciverRoom;
  }
  public class ViewHolder extends RecyclerView.ViewHolder{
    public TextView show_message,name;
    public ImageView profile_image,likeImage;
    public  TextView text_seen,tex;


    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      show_message=itemView.findViewById(R.id.show_message);
      profile_image=itemView.findViewById(R.id.profile_images);
      text_seen=itemView.findViewById(R.id.text_seen);
      likeImage=itemView.findViewById(R.id.likeimage);
      tex=itemView.findViewById(R.id.likeimage3);
      name=itemView.findViewById(R.id.grpusername);


      //name.setVisibility(View.GONE);

    }
  }


  @NonNull
  @Override
  public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
    if(viewType==MSG_TYPE_RIGHT){
      View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_right,parent,false);
      return new MessageAdapter.ViewHolder(view);
    }else {
      View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_left,parent,false);
      return new MessageAdapter.ViewHolder(view);
    }

  }

  @SuppressLint({"ResourceAsColor", "RecyclerView"})
  @Override
  public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {


    chat = mChat.get(position);
    int reaction[] = new int[] {
            R.drawable.delete_outline_24,


    };
    int reaction2[] = new int[] {
            R.drawable.music,
            R.drawable.searchuser,
            R.drawable.notificone

    };

    fuser=FirebaseAuth.getInstance().getCurrentUser();


    Log.d("DLE",chat.getReceiver());




    ReactionsConfig config = new ReactionsConfigBuilder(mcontext)
            .withReactions(reaction)
            .build();
    ReactionPopup popup = new ReactionPopup(mcontext, config, (pos) -> {
    //  if (chat.getSender()==fuser.getUid()){
        holder.likeImage.setImageResource(reaction[pos]);
    //  }else {
     //   holder.likeImage.setImageResource(reaction2[pos]);
     // }

     // Toast.makeText(mcontext.getApplicationContext(),chat.getMessage(),Toast.LENGTH_SHORT).show();



      try {
        if (pos==0){
          //Toast.makeText(mcontext.getApplicationContext(),chat.getMessage(),Toast.LENGTH_SHORT).show();

          fuser= FirebaseAuth.getInstance().getCurrentUser();
         if(mChat.get(position).getSender().equals(fuser.getUid())){
           if (chat.getMessageId()!=null){

            // Toast.makeText(mcontext.getApplicationContext(),chat.getMessage(),Toast.LENGTH_SHORT).show();

           }
          // Toast.makeText(mcontext.getApplicationContext(),chat.getReceiver(),Toast.LENGTH_SHORT).show();

           if (chat.getSender().equals(fuser.getUid())){

             FirebaseDatabase.getInstance().getReference("Chats").child(chat.getMessageId()).removeValue();
           }


            //FirebaseDatabase.getInstance().getReference("Chats").child(chat.getMessageId()).removeValue();

          }else {

          }
          Log.d("chat",chat.getMessageId());

      }else {
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return true; // true is closing popup, false is requesting a new selection
    });





    holder.show_message.setText(chat.getMessage());
    holder.show_message.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
//        popup.onTouch(v,event);

        return false;
      }
    });

    holder.show_message.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //FirebaseDatabase.getInstance().getReference("Chats").child(chat.getMessageId()).removeValue();

        //delete(position);

        String chtlis = mChat.get(position).getMessageId();


        fuser= FirebaseAuth.getInstance().getCurrentUser();

        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("Delete");
        builder.setMessage("want to delete message");

        //chat delete
        if(mChat.get(position).getSender().equals(fuser.getUid())){
         // Toast.makeText(mcontext.getApplicationContext(),"adfasdf",Toast.LENGTH_SHORT).show();
          builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              FirebaseDatabase.getInstance().getReference("Chats").child(chtlis).removeValue();

            }
          });
          builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
          });
          builder.show();
        }else {
          //Toast.makeText(mcontext.getApplicationContext(),"as",Toast.LENGTH_SHORT).show();

        }
      }
    });


    if (imagUrl.equals(("default"))){
      holder.profile_image.setImageResource(R.drawable.ic_launcher_background);
    }else {
      Glide.with(mcontext).load(imagUrl).into(holder.profile_image);
    }
    if (position == mChat.size()-1){
      if (chat.isIsseen()) {
        holder.text_seen.setText("Seen");

      }else {
        holder.text_seen.setText("Delivered");
        holder.text_seen.setTextColor(R.color.red);
      }
    }else {
      holder.text_seen.setVisibility(View.GONE);
    }
  }



  //no use
  private void delete(int postion){

    String chtlis = mChat.get(postion).getMessageId();

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
    Query query = reference.orderByChild("Date").equalTo(chtlis);
    query.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot ds:snapshot.getChildren()){
          if (ds.child("sender").getValue().equals(fuser.getUid())){
            ds.getRef().removeValue();
           // Toast.makeText(mcontext.getApplicationContext(),"adfasdf",Toast.LENGTH_SHORT).show();

          }else {
         //   Toast.makeText(mcontext.getApplicationContext(),"ad",Toast.LENGTH_SHORT).show();

          }

        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });

  }



  public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {

    User m = mUser.get(position);


    holder.username.setText(m.getUsername());
    if (m.getImageURL().equals("default")) {
      holder.profile_image.setImageResource(R.drawable.ic_launcher_background);
    } else {
      Glide.with(mcontext).load(m.getImageURL()).into(holder.profile_image);
    }
    if (!ischat) {


      //holder.clrc.setText("esrdgde");

    } else {
      holder.last_seen.setVisibility(View.GONE);
    }
    if (ischat) {
      holder.imag_on.setVisibility(View.GONE);
      holder.imag_off.setVisibility(View.GONE);
    } else {
      if (m.getStatus().equals("Online")) {
        holder.imag_on.setVisibility(View.VISIBLE);
        holder.imag_off.setVisibility(View.GONE);
      } else {
        holder.imag_on.setVisibility(View.GONE);
        holder.imag_off.setVisibility(View.VISIBLE);
      }
    }
  }


  @Override
  public int getItemCount() {
    return mChat.size();


  }

  @Override
  public int getItemViewType(int position) {

    fuser= FirebaseAuth.getInstance().getCurrentUser();
    if(mChat.get(position).getSender().equals(fuser.getUid())){
      return  MSG_TYPE_RIGHT;
    }else {
      return  MSG_TYPE_LEFT;
    }
  }
}