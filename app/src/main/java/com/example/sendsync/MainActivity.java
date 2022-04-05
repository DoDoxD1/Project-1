package com.example.sendsync;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView profileButton;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DevicesAdapter adapter;
    ArrayList<String> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //refrences
        profileButton = findViewById(R.id.profileButton);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        devices = new ArrayList<>();
        populateDevicesList();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void populateDevicesList() {
        devices.add("Realme");
        devices.add("Oppo");
        devices.add("Vivo");
        devices.add("OnePlus");
        devices.add("Desktop");
        devices.add("realme");
        devices.add("realme");
        devices.add("realme");
        devices.add("realme");

        adapter = new DevicesAdapter(devices);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new DevicesAdapter.OnItemClickedListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this,DeviceActivity.class);
                intent.putExtra("device",devices.get(position));
                startActivity(intent);
            }
        });
    }
}