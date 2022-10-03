package com.example.gt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gt.Adapter.PostAdapter;
import com.example.gt.Adapter.UserAdapter;
import com.example.gt.model.Chat;
import com.example.gt.model.Post;
import com.example.gt.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    ImageView addpost;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private List<String> followingList;
    FirebaseUser fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        addpost = findViewById(R.id.addpost);
        recyclerView = findViewById(R.id.recycler_view_post);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(PostActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(PostActivity.this, postList);
        recyclerView.setAdapter(postAdapter);

        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, addPost.class);
                startActivity(intent);
            }
        });

        checkFollowing();
      //readPosts();
    }
    private void checkFollowing() {
        followingList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Friends")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Follow");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    followingList.add(snapshot.getKey());
                }
                readPosts();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void readPosts(){
        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    postList.clear();
                    try {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Post post = snapshot.getValue(Post.class);
                            for (String id : followingList){

                                if (post.getPublisher().equals(id)){
                                        postList.add(post);

                                }
                                //postList.add(post);
                            }
                            //postList.add(post);



                            }
                            //Log.d("POST",post.getPublisher());
                        //Toast.makeText(PostActivity.this, postList.toString(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(PostActivity.this, postAdapter.mPost.toString(), Toast.LENGTH_SHORT).show();

                       ;

                        //recyclerView.setAdapter(postAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }





                    postAdapter.notifyDataSetChanged();

                    //progress_circular.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}