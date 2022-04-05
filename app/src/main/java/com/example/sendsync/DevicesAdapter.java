package com.example.sendsync;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DevicesViewHolder> {
    private ArrayList<String> devices;
    private OnItemClickedListener listener;

    public interface OnItemClickedListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickedListener listener){
        this.listener = listener;
    }

    public DevicesAdapter(ArrayList<String> devices){
        this.devices = devices;
    }

    @Override
    public DevicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.devices_item,parent,false);
        DevicesViewHolder vh = new DevicesViewHolder(v, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesViewHolder holder, int position) {
        String deviceName = devices.get(position);
        holder.deviceTextView.setText(deviceName);
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public static class DevicesViewHolder extends RecyclerView.ViewHolder{

        TextView deviceTextView;
        ImageView deviceImageView;
        public DevicesViewHolder(@NonNull View itemView, OnItemClickedListener listener) {
            super(itemView);
            deviceTextView = itemView.findViewById(R.id.deviceName);
            deviceImageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
