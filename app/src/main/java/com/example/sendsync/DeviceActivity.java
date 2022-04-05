package com.example.sendsync;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DeviceActivity extends AppCompatActivity {

    TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        //refrences
        titleTextView = findViewById(R.id.titleBar);

        String title = "";
        title = getIntent().getExtras().getString("device");

        titleTextView.setText(title);
    }
}