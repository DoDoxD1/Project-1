package com.example.sendsync;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    ImageButton showPassButton;
    EditText passEditText, emailEditText,nameEditText;
    boolean showPass=false;
    Button signUpButton;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //refrence
        showPassButton = findViewById(R.id.showPassButton);
        passEditText = findViewById(R.id.passwordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        nameEditText = findViewById(R.id.nameEditText);
        signUpButton = findViewById(R.id.signUpButton);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("Users");

        showPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!showPass){
                    showPass=true;
                    passEditText.setTransformationMethod(null);
                }
                else{
                    showPass=false;
                    passEditText.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String pass = passEditText.getText().toString().trim();
                String name = nameEditText.getText().toString().trim();
                if(email.isEmpty()||pass.isEmpty()||name.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "You can't leave empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass.length()<8){
                    Toast.makeText(SignUpActivity.this, "Password can't be less than 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                signUp(email,pass,name);
            }
        });

    }

    private void updateUI(FirebaseUser currentUser) {
        Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void signUp(String email, String pass, String name) {
        User user = new User(email,pass);
        user.setName(name);
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("aunu", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            addUserToDB(email,pass,name,user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void addUserToDB(String email, String pass, String name, FirebaseUser user) {

        userRef.document(user.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                updateUI(user);
                startActivity(new Intent(getApplicationContext(),SplashActivity.class));
                finishAffinity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Log.w("aunu", "createUserWithEmail:failure"+ e);
            }
        });

    }

}