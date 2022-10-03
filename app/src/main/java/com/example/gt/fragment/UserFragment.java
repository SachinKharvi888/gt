package com.example.gt.fragment;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.gt.Adapter.UserAdapter;
import com.example.gt.FrdlistActiviy;
import com.example.gt.MainActivity;
import com.example.gt.MessageActivity;
import com.example.gt.PasswordResetActivity;
import com.example.gt.R;
import com.example.gt.model.User;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    private RecyclerView recyclerView;

    private EditText search;
    ImageButton frdlistbtn;

    private UserAdapter useradapter;
    private List<User> mUser;

    ImageButton sarchhide;
    int i =    0;


    FirebaseUser fireu;

    public UserFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);
        recyclerView = view.findViewById(R.id.recyclerviewUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUser = new ArrayList<>();
        //func
        readUser();

        sarchhide=view.findViewById(R.id.searchhide);
        frdlistbtn=view.findViewById(R.id.frdlistbn);
        sarchhide.setOnClickListener(this::onClick);
        search = view.findViewById(R.id.search_bar);

        frdlistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FrdlistActiviy.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toUpperCase().toLowerCase());
            }

           @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //updateToken(FirebaseMessaging.getInstance().getToken().toString());

        return view;
    }

    private void onClick(View view) {

        if (i == 0) {
            search.setVisibility(View.VISIBLE);
            sarchhide.setImageResource(R.drawable.cancealuser);

            search.requestFocus();

            i++;
        } else if (i == 1) {
            search.setVisibility(View.GONE);
            sarchhide.setImageResource(R.drawable.searchuser);

            i = 0;
        }
    }


    //func
//search bar
    private void searchUsers(String s) {

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert fuser != null;
                    if (!user.getId().equals(fuser.getUid())){
                        mUser.add(user);
                    }
                }

                useradapter = new UserAdapter(getContext(), mUser, false);
                recyclerView.setAdapter(useradapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    //func
   private void readUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                mUser.clear();
                for(DataSnapshot snapshot: datasnapshot.getChildren()){
                    User m = snapshot.getValue(User.class);

                    assert  m !=null;
                    assert  firebaseUser !=null;
                    if (!m.getId().equals(firebaseUser.getUid())){
                        mUser.add(m);
                    }
                }
                useradapter=new UserAdapter(getContext(),mUser,true);
                recyclerView.setAdapter(useradapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}