package com.example.sendsync;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    ImageButton showPassButton;
    EditText passEditText;
    boolean showPass=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //refrence
        showPassButton = findViewById(R.id.showPassButton);
        passEditText = findViewById(R.id.passwordEditText);

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

    }


}