package com.example.gt.fragment;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gt.AboutUs;
import com.example.gt.LoginActivity;
import com.example.gt.MainActivity;
import com.example.gt.PasswordResetActivity;
import com.example.gt.PersonalActivity2;
import com.example.gt.R;
import com.example.gt.RegisterActivity;
import com.example.gt.model.User;
import com.example.gt.postAndSaveActivity2;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.heartbeatinfo.SdkHeartBeatResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileFragment extends Fragment {




    CircleImageView circleImageView;
    TextView username,a;
    ProgressBar prog;

    DatabaseReference reference,databaseReference;
    FirebaseUser fuser;
    Button logout;

    FirebaseAuth auth;

    TextView resetPassword,postandsave,profilelileCount,personal;
    String bioss;
    EditText bios;


    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    Button biosave,biocancel;
    Switch privcaybtn;
    LinearLayout biohide;
    CardView aboutUs;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        circleImageView=view.findViewById(R.id.profile_imageprofile);
        username = view.findViewById(R.id.usernameprofile);
        prog=view.findViewById(R.id.profilePropgress);
        logout=view.findViewById(R.id.logoutuser);
        bios = view.findViewById(R.id.aboutuser);
        profilelileCount=view.findViewById(R.id.profileLikecount);
        personal=view.findViewById(R.id.personal);

        biocancel=view.findViewById(R.id.biocancel);
        a=view.findViewById(R.id.a);
        biosave=view.findViewById(R.id.biosave);
        biohide=view.findViewById(R.id.biohide);
        postandsave=view.findViewById(R.id.poatandsave);
        privcaybtn=view.findViewById(R.id.privacySwitchbtn);
        aboutUs=view.findViewById(R.id.card7);

        logout.animate().rotationX(390).setDuration(1000);
        logout.setAlpha(1f);
        fuser=FirebaseAuth.getInstance().getCurrentUser();

        profilelikecount(profilelileCount,fuser.getUid());

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AboutUs.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biohide.setVisibility(View.VISIBLE);
            }
        });

        biocancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biohide.setVisibility(View.GONE);
            }
        });
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PersonalActivity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        reference=FirebaseDatabase.getInstance().getReference("Privacy").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Privacy").getValue().toString().equals("ON")){
                   // Toast.makeText(getContext(), "ON", Toast.LENGTH_SHORT).show();
                    privcaybtn.setChecked(true);
                }else{
                    privcaybtn.setChecked(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        privcaybtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    // The toggle is enabled
                    String s =privcaybtn.getTextOn().toString();
                    Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    reference=FirebaseDatabase.getInstance().getReference("Privacy").child(fuser.getUid());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("Privacy","ON");
                    reference.updateChildren(map);
                } else {
                    // The toggle is disabled
                    reference=FirebaseDatabase.getInstance().getReference("Privacy").child(fuser.getUid());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("Privacy","OFF");
                    reference.updateChildren(map);
                }
            }
        });

        postandsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), postAndSaveActivity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        biosave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bioss=bios.getText().toString();

                reference=FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                HashMap<String, Object> map = new HashMap<>();
                map.put("Bio",bioss);
                reference.updateChildren(map);

                biohide.setVisibility(View.GONE);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                        Toast.makeText(getContext(), "logout done",Toast.LENGTH_SHORT).show();
                        auth.getInstance().signOut();
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

                builder.show();

            }
        });

        resetPassword=view.findViewById(R.id.resetPassword);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PasswordResetActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User m = snapshot.getValue(User.class);
                username.setText(m.getUsername());
                //a.setText(m.getBio().toString());
               // String s = snapshot.child("name").getValue().toString();


                if (m.getImageURL().equals("default")){
                    circleImageView.setImageResource(R.drawable.ic_launcher_background);
                    //a.setText(m.getBio());
                }else {
                    Glide.with(getContext()).load(m.getImageURL()).into(circleImageView);
                   //a.setText(m.getBio());
                }

                reference=FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User m = snapshot.getValue(User.class);
                        if (m.getUsername()==null){
                            a.setText("hi i am using GT");
                        }else {
                            a.setText(snapshot.child("Bio").getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //m.setBio(reference.toString());
                //a.setText(m.getBio());

              //  a.setText(m.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
        return  view;
    }


    //open photo in mobile
    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);

    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    //upload
    private  void uploadimage(){
        final ProgressBar pd = new ProgressBar(getContext());
        pd.isShown();
        prog.setVisibility(View.VISIBLE);

        if (imageUri !=null){
            final StorageReference fileRefrece = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask=fileRefrece.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();

                    }
                    return fileRefrece.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri= task.getResult();
                        String muri = downloadUri.toString();
                        bioss=bios.getText().toString();

                        reference=FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL",muri);
                       // map.put("Bio",bioss);



                        reference.updateChildren(map);
                        prog.setVisibility(View.INVISIBLE);
                    }else {
                        Toast.makeText(getContext(),"failed!",Toast.LENGTH_SHORT).show();

                        prog.setVisibility(View.INVISIBLE);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    prog.setVisibility(View.INVISIBLE);

                }
            });

        }else {
            Toast.makeText(getContext(),"no image selected !",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== IMAGE_REQUEST &&  data!=null&& data.getData()!=null){
            imageUri=data.getData();
            if (uploadTask!=null&& uploadTask.isInProgress()){
                Toast.makeText(getContext(),"uplaod in progress!",Toast.LENGTH_SHORT).show();
                prog.setVisibility(View.INVISIBLE);

            }else {

                uploadimage();

            }
        }
    }



    private void  profilelikecount(TextView likes,String profileid){
        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("profile").child(profileid);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    likes.setText(snapshot.getChildrenCount() + "likes");
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