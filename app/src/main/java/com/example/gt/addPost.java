package com.example.gt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.oginotihiro.cropview.CropUtil;
import com.oginotihiro.cropview.CropView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;


public class addPost extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_PICK =9162 ;
    Uri imageUri;
    Uri imageUi;
    String myuri = "";
    StorageTask uploadTask;
    StorageReference storageReference;

    ImageView imageadd;
    ImageButton colse;
    TextView post;
    EditText description;
    CropView cropp;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    private Button doneBtn;
    private Button cancelBtn;
    private Bitmap croppedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        imageadd=findViewById(R.id.imageAdd);
        post=findViewById(R.id.postimage);
        description=findViewById(R.id.descriptions);
        cropp=findViewById(R.id.cropp);
        doneBtn = (Button) findViewById(R.id.doneBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        post.setVisibility(View.GONE);

        doneBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        storageReference = FirebaseStorage.getInstance().getReference("Posts");

        imageadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // uploadimage;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
                startActivityForResult(intent, REQUEST_PICK);
            }
        });


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage_10();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_PICK) {
            imageUri=data.getData();
            cropp.setVisibility(View.VISIBLE);
            // .setVisibility(View.VISIBLE);
            // cropp.ActivityResult result = CropImage.getActivityResult(data);
            cropp.of(imageUri).asSquare().initialize(addPost.this);

           // imageUi=data
            //imageadd.setImageURI();

            // String sa = cropp.of(imageUri).asSquare().initialize(addPost.this);
            //imageadd.setImageURI(cropp);

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.doneBtn) {
            final ProgressDialog dialog = ProgressDialog.show(addPost.this, null, "Please wait…", true, false);
            new Thread() {
                public void run() {
                    croppedBitmap = cropp.getOutput();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageadd.setImageBitmap(croppedBitmap);

                        }
                    });

                      imageUi= Uri.fromFile(new File(getCacheDir(), "cropped"));
                    CropUtil.saveOutput(addPost.this, imageUi, croppedBitmap, 90);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            cropp.setVisibility(View.GONE);
                            imageadd.clearFocus();
                            imageadd.setImageBitmap(croppedBitmap);
                            post.setVisibility(View.VISIBLE);
                            // imageadd.setImageURI(imageUri);


                        }
                    });
                }
            }.start();
        } else if (id == R.id.cancelBtn) {
            reset();
        }
    }
    private void reset() {

        imageadd.setImageBitmap(null);
    }
    private String  getFileExtension(Uri uri){
        ContentResolver contentResolver = addPost.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public Uri getImageUri(Context context, Bitmap bitmap){
        ByteArrayOutputStream byt = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byt);
        String  p= MediaStore.Images.Media.insertImage(context.getContentResolver(),bitmap,"title",null);
        return Uri.parse(p);
    }

    private void uploadImage_10(){

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Posting");
        pd.show();
        if (imageUri != null){
            final StorageReference fileRefrece = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUi));
                    //final StorageReference fileRefrece = storageReference.child(System.currentTimeMillis()
                    //  +"."+getImageUri(this,croppedBitmap));

            uploadTask = fileRefrece.putFile(imageUi);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRefrece.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        myuri = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

                        String postid = reference.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postid", postid);
                        hashMap.put("postimage", myuri);
                        hashMap.put("description", description.getText().toString());
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());


                        reference.child(postid).setValue(hashMap);

                        pd.dismiss();

                        startActivity(new Intent(addPost.this, PostActivity.class));
                        finish();

                    } else {
                        Toast.makeText(addPost.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(addPost.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(addPost.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

}