package com.example.sendsync;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    Button saveChangesButton;
    CircleImageView backButton,profileImageButton;
    TextView editProfileButton;
    ProgressBar progressBar;

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
                progressBar.setVisibility(View.VISIBLE);
                saveChangesToButton();
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
    }


    private void saveChangesToButton() {
        Intent intent = new Intent(EditProfileActivity.this,ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}