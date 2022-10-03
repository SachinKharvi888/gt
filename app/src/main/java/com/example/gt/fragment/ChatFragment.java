package com.example.gt.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gt.Adapter.UserAdapter;
import com.example.gt.FrdlistActiviy;
import com.example.gt.R;
import com.example.gt.model.Chat;
import com.example.gt.model.Chatlist;
import com.example.gt.model.User;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {
    private UserAdapter userAdapters;

    private List<User> mUser;

    private RecyclerView recyclerView;

    FirebaseUser fuser;
    DatabaseReference reference;

    private  List<String> userslist;
    private List<User> tList2;
    private List<String> followingList;
    private List<String> followingList2;

    String rev;
    TextView se;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);


        recyclerView = view.findViewById(R.id.recyclerviewChat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        userslist = new ArrayList<>();
        mUser = new ArrayList<>();
        tList2 = new ArrayList<>();


        checkFollowing2();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userslist.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Chat chat = snapshot1.getValue(Chat.class);
                   // rev = chat.getReceiver().toString();
                    if (chat.getSender().equals(fuser.getUid())) {
                        userslist.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(fuser.getUid())) {
                        userslist.add(chat.getSender());
                    }
                }
                //readChatss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return view;
    }

    private void checkFollowing2() {
        followingList2 = new ArrayList<>();

       // DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(rev);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followingList2.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    followingList2.add(snapshot.getKey());
                    //  String s = snapshot.getChildren()

                   // Toast.makeText(FrdlistActiviy.this, followingList2.toString(), Toast.LENGTH_SHORT).show();

                }
                // String s = dataSnapshot.getChildren().toString();


                readUser2();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void readUser2() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                tList2.clear();
                for(DataSnapshot snapshot: datasnapshot.getChildren()){
                    User m = snapshot.getValue(User.class);
                    for (String id : followingList2){
                        if (m.getId().equals(id)){
                            tList2.add(m);

                        }
                    }
                }
                userAdapters= new UserAdapter(getContext(),tList2,false);
                recyclerView.setAdapter(userAdapters);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private  void  readChatss(){

        try {


            reference=FirebaseDatabase.getInstance().getReference("Users");

            reference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUser.clear();
                    for (DataSnapshot snapshot1:dataSnapshot.getChildren()){
                        User user = snapshot1.getValue(User.class);


                        try {
                            for (String id: userslist){
                                if (user.getId().equals(id)){
                                    if (mUser.size()!=0){
                                        for (User user1 :  mUser){
                                           // mUser.clear();
                                            if (!id.equals(user1.getId())){

                                               // mUser.add(user1);
                                                mUser.add(user);
                                                //Log.d("POST",mUser.toString());
                                            }else {



                                            }
                                        }   //mUser.add(user);

                                    }else {
                                       // mUser.clear();

                                         mUser.add(user);




                                        // mUser.add(user);
                                    }
                                  // mUser.add(user);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    userAdapters= new UserAdapter(getContext(),mUser,false);
                    recyclerView.setAdapter(userAdapters);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {

            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}