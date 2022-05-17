package com.example.sendsync;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeviceActivity extends AppCompatActivity {

    TextView titleTextView;
    EditText getMessageEditText;
    CircleImageView sendMessageButton;

    private String newMessage;
    Intent intent;
    String receiverName, receiverID, senderName, senderID;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabse;
    String  senderRoom,receiverRoom;

    RecyclerView recyclerView;

    String currentTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    Message message;
    MessagesAdapter messagesAdapter;
    ArrayList<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        //refrences
        titleTextView = findViewById(R.id.titleBar);
        getMessageEditText = findViewById(R.id.get_message);
        sendMessageButton = findViewById(R.id.send_msg);
        intent = getIntent();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabse =  FirebaseDatabase.getInstance("https://sendsync-3367c-default-rtdb.firebaseio.com");
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");

        messages = new ArrayList<>();
        recyclerView = findViewById(R.id.device_recyler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        messagesAdapter = new MessagesAdapter( this,messages);
        recyclerView.setAdapter(messagesAdapter);



        senderID = firebaseAuth.getUid()+"Device";
        receiverID = firebaseAuth.getUid()+"Device 2";
        receiverName = "Device 2";
        senderName = "Device 1";
        senderRoom = senderID+receiverID;
        receiverRoom = receiverID+senderID;
        fetchMessages();
        String title = "";
        title = getIntent().getExtras().getString("device");

        titleTextView.setText(title);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newMessage = getMessageEditText.getText().toString();
                if(newMessage.isEmpty()){
                    Toast.makeText(DeviceActivity.this, "Enter message first", Toast.LENGTH_SHORT).show();
                }
                else{
                    sendMessage();
                }

            }
        });

    }

    private void fetchMessages() {
        DatabaseReference reference = firebaseDatabse.getReference().child("chats").child(senderRoom).child("messages");
        messagesAdapter = new MessagesAdapter(DeviceActivity.this,messages);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Message message = dataSnapshot1.getValue(Message.class);
                    messages.add(message);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage() {

        Date date = new Date();
        currentTime = simpleDateFormat.format(calendar.getTime());
        Message message = new Message(newMessage,senderID,date.getTime(),currentTime);
        firebaseDatabse.getReference().child("chats")
            .child(senderRoom)
            .child("messages")
            .push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firebaseDatabse.getReference().child("chats").child(receiverRoom).child("messages").push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(DeviceActivity.this, "message sent", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        getMessageEditText.setText(null);
    }
}