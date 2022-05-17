package com.example.sendsync;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messages;

    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    public MessagesAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_chat_layout,parent,false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciever_chat_layout,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message message =  messages.get(position);
        if(holder.getClass()==SenderViewHolder.class){
            SenderViewHolder  viewHolder = (SenderViewHolder)holder;
            viewHolder.msgTextView.setText(message.getMessage());
            viewHolder.timeTextView.setText(message.getCurrentTime());
        }
        else{
            ReceiverViewHolder  viewHolder = (ReceiverViewHolder) holder;
            viewHolder.msgTextView.setText(message.getMessage());
            viewHolder.timeTextView.setText(message.getCurrentTime());
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderID())){
            return ITEM_SEND;
        }else {
            return ITEM_RECEIVE;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView msgTextView, timeTextView;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            msgTextView = itemView.findViewById(R.id.sender_message);
            timeTextView = itemView.findViewById(R.id.msg_time);

        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView msgTextView, timeTextView;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            msgTextView = itemView.findViewById(R.id.sender_message);
            timeTextView = itemView.findViewById(R.id.msg_time);

        }
    }
}
