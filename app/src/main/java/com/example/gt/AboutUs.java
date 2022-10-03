package com.example.gt;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.gt.databinding.ActivityAboutUsBinding;

public class AboutUs extends AppCompatActivity {


    String aboutus;
    ImageButton backAboutbtn;
    TextView abouttxt;
    Button feedsendbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        abouttxt=findViewById(R.id.abouttxt);
        backAboutbtn=findViewById(R.id.backAboutbtn);
        feedsendbtn=findViewById(R.id.feedsendbtn);




        aboutus ="This app is regarding global chat application. The user can post" +
                "and listen to music . " +
                "We have the unique features like profile like where any of the " +
                "social media doesn't have it." +
                "In future we will develope some extra features like group chat," +
                "reels, live location  " +
                "The user can block the users and there is a security ." +
                "user can follow others and unfollow the users as his wish" +
                "The user can report the other user " +
                "This is the dynamic application where the combination of instagram " +
                "and whatsapp ." +
                "The version will be updated further";

        abouttxt.setText(aboutus);

        backAboutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutUs.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        feedsendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutUs.this,Feedback.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


    }


}