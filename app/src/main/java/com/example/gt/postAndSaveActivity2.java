package com.example.gt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gt.Adapter.PostsaveAdap;
import com.example.gt.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class postAndSaveActivity2 extends AppCompatActivity {
    ImageButton save,post;
    RecyclerView postview,saveVew;
    TextView posttitle;
    private List<String>mysave;
    private List<Post> postList_saves;

    PostsaveAdap postsaveAdap;
    ImageButton my_fotos, saved_fotos;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    List<Post>postList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_and_save2);


        saveVew = findViewById(R.id.recycler_view_save);
        postview= findViewById(R.id.recycler_view_post);
        posttitle = findViewById(R.id.posttitle);

        saveVew.setHasFixedSize(true);
        LinearLayoutManager mLayoutManagers = new GridLayoutManager(this, 3);
        saveVew.setLayoutManager(mLayoutManagers);
        postList_saves = new ArrayList<>();
        postsaveAdap = new PostsaveAdap(this,postList_saves);
        saveVew.setAdapter(postsaveAdap);

        my_fotos = findViewById(R.id.my_fotos);
       saved_fotos = findViewById(R.id.saved_fotos);

        postview.setHasFixedSize(true);
        LinearLayoutManager mLayoutManagers2 = new GridLayoutManager(this, 3);
        postview.setLayoutManager(mLayoutManagers2);
        postList = new ArrayList<>();
        postsaveAdap = new PostsaveAdap(this,postList);
        postview.setAdapter(postsaveAdap);

        mySaves();
        myFotos();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();


        my_fotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postview.setVisibility(View.VISIBLE);
                saveVew.setVisibility(View.GONE);
                my_fotos.setImageResource(R.drawable.collections_24);
                saved_fotos.setImageResource(R.drawable.savebalck_24);
                posttitle.setText("Post Image");

            }
        });

        saved_fotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postview.setVisibility(View.GONE);
                saveVew.setVisibility(View.VISIBLE);
                my_fotos.setImageResource(R.drawable.postcollections_24);
                saved_fotos.setImageResource(R.drawable.saveicon);
                posttitle.setText("Saved Image");
            }
        });
    }

    private void myFotos(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(firebaseUser.getUid())){
                        postList.add(post);
                    }
                }
                Collections.reverse(postList);
                postsaveAdap.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void mySaves(){
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        mysave = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SavePost").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    mysave.add(snapshot.getKey());
                    Log.d("LOGG",mysave.toString());
                }
                readSaves();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void readSaves(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    postList_saves.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Post post = snapshot.getValue(Post.class);

                        for (String id : mysave) {
                            if (post.getPostid().equals(id)) {
                                postList_saves.add(post);
                            }
                            Log.d("LOG",postList_saves.toString());

                        }
                    }
                    //saveVew.setAdapter(postsaveAdap);
                     postsaveAdap.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}