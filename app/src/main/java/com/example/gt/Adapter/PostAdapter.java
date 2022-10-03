package com.example.gt.Adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gt.MessageActivity;
import com.example.gt.PasswordResetActivity;
import com.example.gt.R;
import com.example.gt.model.Post;
import com.example.gt.model.User;
import com.example.gt.otherProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public Context mcontext;
    public List<Post>mPost;
    Post post;
    DatabaseReference likerref,newdata,reference;
    Boolean testclick=false;
    String name;

    private FirebaseUser firebaseUser;
    public PostAdapter(Context mcontext,List<Post> mPost){
        this.mcontext=mcontext;
        this.mPost=mPost;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.potst_item,parent,false);
        return new PostAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post = mPost.get(position);
        Glide.with(mcontext).load(post.getPostimage()).into(holder.post_image);
        if (post.getDescription().equals("")){
            holder.description.setVisibility(View.GONE);
        }else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(post.getDescription());
        }
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("id").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        publisherIfno(holder.image_Pofile,holder.username,holder.publisher,post.getPublisher());
       // getlikebt(post.getPostid(),firebaseUser.getUid(),holder.like,holder.likes);



        islike(post.getPostid(),holder.like);
        nulike(holder.likes,post.getPostid());
        issavePost(post.getPostid(), holder.save);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(mcontext.getApplicationContext(), "clike",Toast.LENGTH_SHORT).show();
                if (holder.like.getTag().equals("likes")){
                  FirebaseDatabase.getInstance().getReference("PostLike").child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);

                }else {
                  //  FirebaseDatabase.getInstance().getReference("PostLike").child(post.getPostid()).removeValue();
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.equals(post.getPublisher())){

                }else {
                    Intent intent = new Intent(mcontext, MessageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("userid",post.getPublisher());
                    mcontext.startActivity(intent);
                }

            }
        });

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.save.getTag().equals("save")){
                    FirebaseDatabase.getInstance().getReference().child("SavePost").child(firebaseUser.getUid()).child(post.getPostid()).setValue(true);
                }else {
                    FirebaseDatabase.getInstance().getReference().child("SavePost").child(firebaseUser.getUid()).child(post.getPostid()).removeValue();

                }

            }
        });

        if(firebaseUser.getUid().equals(post.getPublisher())){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mcontext.getApplicationContext(),R.array.menus, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinner.setAdapter(adapter);
        }else {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mcontext.getApplicationContext(),R.array.menus2, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinner.setAdapter(adapter);
        }




        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choic = parent.getItemAtPosition(position).toString();
                holder.spinner.clearFocus();
                
                if (position==0){



                }else if (position==1){
                    if(firebaseUser.getUid().equals(post.getPublisher())){
                        //Toast.makeText(mcontext.getApplicationContext(), "b",Toast.LENGTH_SHORT).show();
                        FirebaseDatabase.getInstance().getReference("Posts").child(post.getPostid()).removeValue();

                    }


                }else if (position==2){


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void getlikebt(final String postkey, final  String userid, ImageView imageView, TextView postlikecount){
        likerref=FirebaseDatabase.getInstance().getReference("PostLike");
        likerref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postkey).hasChild(userid)){
                    int likecount=(int)snapshot.child(postkey).getChildrenCount();
                    postlikecount.setText(likecount+"");
                    imageView.setImageResource(R.drawable.likeimage4);
                    //Toast.makeText(mcontext.getApplicationContext(), "add",Toast.LENGTH_SHORT).show();
                } else {
                    int likecount2 = (int) snapshot.child(postkey).getChildrenCount();
                    postlikecount.setText(likecount2 + "");
                    // imageView.setImageResource(R.drawable.likeimag3);
                     //Toast.makeText(mcontext.getApplicationContext(), "cfghfghount",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_Pofile,post_image,like,comment,save;
        public TextView username,likes,publisher,description,comments;
        public Spinner spinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_Pofile = itemView.findViewById(R.id.userpostimage);
            post_image=itemView.findViewById(R.id.post_image);
            post_image.setImageResource(R.drawable.profileus);
            like=itemView.findViewById(R.id.postlike);
            comment=itemView.findViewById(R.id.postcomment);
            save=itemView.findViewById(R.id.postsave);
            username=itemView.findViewById(R.id.usernamepost);
            likes=itemView.findViewById(R.id.postlikecount);
            publisher=itemView.findViewById(R.id.publisher);
            description=itemView.findViewById(R.id.description);
            comments=itemView.findViewById(R.id.comments);
            spinner = itemView.findViewById(R.id.spinner);

        }
    }
    private  void  islike(String postid,ImageView imageView){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("PostLike").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.likeimage4);
                    imageView.setTag("like");

                }else {
                    imageView.setImageResource(R.drawable.likeimag3);
                    imageView.setTag("likes");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void  nulike(final TextView likes,String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("PostLike").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount() + "likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void  issavePost(String postid,ImageView imageView){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("SavePost").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postid).exists()){
                    imageView.setImageResource(R.drawable.done_24);
                    imageView.setTag("saved");

                }else {
                    imageView.setImageResource(R.drawable.saveicon);
                    imageView.setTag("save");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private  void publisherIfno(ImageView image_profile,TextView username,TextView publisher,String userid){
        try {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);


            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        User user = snapshot.getValue(User.class);
                        if (user.getImageURL().equals("defalut")){
                            image_profile.setImageResource(R.drawable.profileus);
                        }else {
                            Glide.with(mcontext).load(user.getImageURL()).into(image_profile);


                        }
                        username.setText(user.getUsername());
                        publisher.setText(user.getUsername());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
