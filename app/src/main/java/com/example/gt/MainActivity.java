package com.example.gt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.gt.fragment.ChatFragment;
import com.example.gt.fragment.UserFragment;
import com.example.gt.fragment.profileFragment;
import com.example.gt.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    CircleImageView profile;
    TextView username,Gttitle;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    DatabaseReference reference;
    Toolbar tlbar;
    int i =    0;
    ImageButton imageButton,groupchatBtn,postbtn,sidemenu,musicbtn;
    RelativeLayout sidemenus;
    TabLayout tab;
    ViewPager viewPager;
    CardView logoutbtn,feedbackbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profile=findViewById(R.id.profile_imageMain);
        username=findViewById(R.id.username);
        //imageButton = findViewById(R.id.profileshow);
        groupchatBtn=findViewById(R.id.groupchat);
        postbtn=findViewById(R.id.postBtn);
        sidemenus=findViewById(R.id.sidemenuhide);
        musicbtn=findViewById(R.id.musicBtn);
        sidemenu=findViewById(R.id.sidemenuicon);
        sidemenu.setOnClickListener(this::onClick);
        viewPager=findViewById(R.id.view_pager);
        tab=findViewById(R.id.tab_layout);
        Gttitle=findViewById(R.id.gtTitletxt);
        feedbackbtn=findViewById(R.id.feedbackbtn);
        logoutbtn=findViewById(R.id.logoutbtn);
        Gttitle.setSelected(true);



        sidemenus.setAlpha(0f);
        sidemenus.animate().alpha(1f).setDuration(1500);


        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("LOG OUT");
                builder.setMessage("Do you want to LogOut?");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "logout done",Toast.LENGTH_SHORT).show();
                        auth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

                builder.show();

            }
        });
        feedbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Feedback.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        musicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,music.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,PostActivity.class);
               // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        groupchatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,grp.class);
                startActivity(intent);

            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());

                if (user.getImageURL().equals("default")){
                    profile.setImageResource(R.drawable.ic_launcher_background);
                }else {

                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        Viewpageadapter viewpageadapter = new Viewpageadapter(getSupportFragmentManager());

        //adding title
        viewpageadapter.addFragment(new ChatFragment(),"chats");
        viewpageadapter.addFragment(new UserFragment(),"user");
        viewpageadapter.addFragment(new profileFragment(),"Profile");

        viewPager.setAdapter(viewpageadapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public void onClick(View v) {
        if (i == 0) {
            sidemenus.setVisibility(View.VISIBLE);
            tab.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            sidemenus.animate().rotationY(360).setDuration(1000);
            sidemenus.setAlpha(1f);
            i++;
        } else if (i == 1) {
            sidemenus.setVisibility(View.GONE);
            sidemenus.animate().rotationX(360).setDuration(1000);
            sidemenus.setAlpha(1f);
           // sidemenus.animate().alpha(1f).setDuration(500);
            tab.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            i = 0;
        }
    }

    private void setSupportActionBar(Toolbar toolbar) {

    }

    //fragments
    class Viewpageadapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public Viewpageadapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }



        @Override
        public int getCount() {
            return fragments.size();
        }
        public  void  addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout1:
                Toast.makeText(MainActivity.this,"logout done",Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this,MessageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    }
    /////////status online
    private void  status(String status){
        try {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

            HashMap<String,Object> hashMap =  new HashMap<>();
            hashMap.put("status",status);

            reference.updateChildren(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("Offline");
    }

}