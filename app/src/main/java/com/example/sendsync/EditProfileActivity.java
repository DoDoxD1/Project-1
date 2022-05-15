package com.example.sendsync;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    Button saveChangesButton;
    CircleImageView backButton,profileImageButton;
    TextView editProfileButton;
    EditText nameEditText;
    ProgressBar progressBar;

    CollectionReference userRef;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    String uID;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // refrences
        saveChangesButton = findViewById(R.id.saveChangesButton);
        backButton = findViewById(R.id.backButton);
        editProfileButton = findViewById(R.id.editProfileButton);
        progressBar = findViewById(R.id.progressbar);
        profileImageButton = findViewById(R.id.circleImageView);
        nameEditText = findViewById(R.id.nameEditText);

        db = FirebaseFirestore.getInstance();
        userRef = db.collection("Users");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();

        if(mUser.getPhotoUrl()!=null){
            Glide.with(this)
                    .load(mUser.getPhotoUrl()).into(profileImageButton);
        }
        LoadUserData();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                EditProfileActivity.super.onBackPressed();
            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = "";
                name = nameEditText.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                saveChangesToDB(name);
                if (bitmap!=null) {
                    uploadProfileToDB(bitmap);
                }

            }
        });

        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (intent.resolveActivity(getPackageManager())!= null){
                    startActivityForResult(intent,1001);
//                }

            }
        });
    }

    private void LoadUserData() {
        User user = User.getUser();
        nameEditText.setText(user.getName());
    }

    private void uploadProfileToDB(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        final StorageReference reference = FirebaseStorage.getInstance().getReference().child("profileImages").child(uID+".jpeg");
        reference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        User CurrentUser = User.getUser();
                        CurrentUser.setPhotoUri(uri.toString());
                        userRef.document(user.getUid()).set(CurrentUser);
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
                        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("aunu", "onFailure: "+e);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1001){
            switch (resultCode){
                case RESULT_OK:
                    bitmap = (Bitmap) data.getExtras().get("data");
                    profileImageButton.setImageBitmap(bitmap);
            }
        }
        else{
            Toast.makeText(this, "error!", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveChangesToDB(String name) {
        User user = User.getUser();
        user.setName(name);
        userRef.document(uID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                user.setName(name);
                userRef.document(uID).set(user);
                startActivity(new Intent(getApplicationContext(),SplashActivity.class));
                finishAffinity();

            }
        });
        Intent intent = new Intent(EditProfileActivity.this,ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}