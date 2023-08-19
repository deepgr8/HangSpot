package com.example.hangspot.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hangspot.Models.Users;
import com.example.hangspot.R;
import com.example.hangspot.chatting;

import java.util.ArrayList;

public class call_screenAdapter extends RecyclerView.Adapter<call_screenAdapter.ViewHolder>{
    ArrayList<Users> list;
    Context context;

    public call_screenAdapter(ArrayList<Users> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.call_screen_adapter,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = list.get(position);
        Glide.with(holder.itemView.getContext())
                .load(users.getProfileImg()).circleCrop().
                apply(new RequestOptions().placeholder(R.drawable.avatar)).into(holder.imageView);
        holder.name.setText(users.getName());
        holder.conversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, chatting.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("senderId",users.getSenderId());
                intent.putExtra("email",users.getEmail());
                intent.putExtra("profilepic",users.getProfileImg());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        Button call,conversation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.calluserimg);
            name = itemView.findViewById(R.id.callnameUser);
            call = itemView.findViewById(R.id.video_call);
            conversation = itemView.findViewById(R.id.conversation);
        }

    }
}
