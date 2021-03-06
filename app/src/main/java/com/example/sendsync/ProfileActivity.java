package com.example.sendsync;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    Button signOutButton;
    CircleImageView backButton,profileImageView;
    TextView editProfileButton,nameTextView;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseUser user;
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // signOutButton
        signOutButton = findViewById(R.id.signOutButton);
        backButton = findViewById(R.id.backButton);
        editProfileButton = findViewById(R.id.editProfileButton);
        nameTextView = findViewById(R.id.nameTextView);
        progressBar = findViewById(R.id.progressBar);
        mUser = User.getUser();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(mUser!=null)
            nameTextView.setText(mUser.getName());
        else
            Toast.makeText(this, "Failed to load resources!", Toast.LENGTH_SHORT).show();

        if(mUser.getProfileUriString()=="None"){

            Drawable myDrawable = getResources(). getDrawable(R. drawable. account);
            profileImageView.setImageDrawable(myDrawable);
            progressBar.setVisibility(View.GONE);
        }
        else if(mUser.getProfileUriString()!=null){
//            Glide.with(this)
//                    .load(user.getPhotoUrl()).into(profile);
        }



        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                ProfileActivity.super.onBackPressed();
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}