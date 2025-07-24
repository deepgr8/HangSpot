package com.example.hangspot.Adapter;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hangspot.Models.messagemodel;
import com.example.hangspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class chatAdapter extends RecyclerView.Adapter {
    FirebaseDatabase firebaseDatabase;
    ArrayList<messagemodel> messagemodels;
    Context context;
    AlertDialog alertDialog;
    String recId;
    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;

    public chatAdapter(ArrayList<com.example.hangspot.Models.messagemodel> messagemodels, Context context) {
        this.messagemodels = messagemodels;
        this.context = context;
    }

    public chatAdapter(ArrayList<messagemodel> messagemodels, Context context, String recId) {
        this.messagemodels = messagemodels;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sender,parent,false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace("@gmail.com", "");
        if (messagemodels.get(position).getUsername().equals(currentUser)){
            return SENDER_VIEW_TYPE;
        }else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        messagemodel mmodel = messagemodels.get(position);
        if (holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder) holder).senderText.setText(mmodel.getMessage());
            Date date = new Date(mmodel.getTimestamp());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
            String strdate = simpleDateFormat.format(date);
            ((SenderViewHolder) holder).sendertime.setText(strdate.toString());
        }

        else {
            ((ReceiverViewHolder) holder).receiverText.setText(mmodel.getMessage());
        }


    }

    @Override
    public int getItemCount() {
        return messagemodels.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView receiverText,receivertime;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverText =itemView.findViewById(R.id.receiverText);
            receivertime = itemView.findViewById(R.id.receiverTime);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderText,sendertime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderText = itemView.findViewById(R.id.senderText);
            sendertime = itemView.findViewById(R.id.senderTime);
        }
    }
}
