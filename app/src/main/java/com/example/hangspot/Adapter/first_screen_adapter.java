package com.example.hangspot.Adapter;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hangspot.Models.Users;
import com.example.hangspot.Models.messagemodel;
import com.example.hangspot.R;
import com.example.hangspot.chatting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class first_screen_adapter extends RecyclerView.Adapter<first_screen_adapter.ViewHolder>{
    ArrayList<Users> list;
    Context context;
    String fcmToken;

    public first_screen_adapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.first_screen_adapter,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = list.get(position);
        SharedPreferences.Editor firstScnPreferences = context.getSharedPreferences("fchat",Context.MODE_PRIVATE).edit();
        firstScnPreferences.putString("imageUrl", users.getProfileImg()).commit();
        SharedPreferences sharedPreferences = context.getSharedPreferences("fchat",Context.MODE_PRIVATE);
        String imageurl = sharedPreferences.getString("imageurl","");
        Glide.with(holder.itemView.getContext())
                .load(users.getProfileImg())
                .circleCrop()
                .apply(new RequestOptions().placeholder(R.drawable.avatar))
                .into(holder.image);
        holder.name.setText(users.getName());
        String lastmsg=sharedPreferences.getString("lmessage","");
        long defaultValue = 0L;
        long timestamp = sharedPreferences.getLong("timestamp",defaultValue);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        String formattedTime = sdf.format(new Date(timestamp));
        holder.lastmessage.setText(lastmsg);
        holder.lsttime.setText(formattedTime);
        FirebaseDatabase.getInstance().getReference().child("Users")
                           .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace("@gmail.com","")).child("chats").child(users.getEmail().replace("@gmail.com",""))
                   .orderByChild("timestamp")
                   .limitToLast(1)
                   .addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if (snapshot.hasChildren()){

                               for (DataSnapshot snapshot1: snapshot.getChildren()
                                    ) {
                                   firstScnPreferences.putString("lmessage",snapshot1.child("message").getValue(String.class)).commit();
                                   firstScnPreferences.putLong("timestamp",snapshot1.child("timestamp").getValue(long.class)).commit();
                                   users.setLastmessage(snapshot1.child("message").getValue(String.class));
                                   holder.lastmessage.setText(users.getLastmessage());
                                   SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                                   long time = snapshot1.child("timestamp").getValue(long.class);
                                   String formattedTime = sdf.format(new Date(time));
                                   holder.lsttime.setText(formattedTime);
                               }
                           }
                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("email", "onClick: "+users.getEmail());
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
        ImageView image;
        TextView name, lastmessage,lsttime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.userimg);
            name = itemView.findViewById(R.id.nameUser);
            lsttime = itemView.findViewById(R.id.lsttime);
            lastmessage = itemView.findViewById(R.id.lstmsg);

        }
    }
}
