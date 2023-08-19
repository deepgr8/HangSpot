package com.example.hangspot.Adapter;

import static android.app.PendingIntent.getActivity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hangspot.Models.FriendModel;
import com.example.hangspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class settingAdapter extends RecyclerView.Adapter<settingAdapter.ViewHolder> {

    ArrayList<FriendModel> frdlist;
    Context context;

    public settingAdapter(ArrayList<FriendModel> frdlist, Context context) {
        this.frdlist = frdlist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.settingadapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendModel invites = frdlist.get(position);
        holder.frdName.setText(invites.getSenderName());
        Glide.with(context).load(invites.getImageProfile())
                .circleCrop()
                .into(holder.frdImg);
        holder.acceptbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DatabaseReference invitationsRef = FirebaseDatabase.getInstance().getReference("Users");
                String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace("@gmail.com", "");
                String senderUser = invites.getSenderName();
                Toast.makeText(context, "Accept button", Toast.LENGTH_SHORT).show();
                invites.setAccept(true);
                DatabaseReference currentUserFriendsRef = invitationsRef.child(currentUser).child("friends");
                currentUserFriendsRef.child(senderUser).setValue(true);

                DatabaseReference senderFriendsRef = invitationsRef.child(senderUser).child("friends");
                senderFriendsRef.child(currentUser).setValue(true);
                // Remove the invitation from current user's Invites node
                DatabaseReference currentUserInvitesRef = invitationsRef.child(currentUser).child("Invites");
                currentUserInvitesRef.child(invites.getKey()).removeValue();

            }
        });
        holder.declinebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference invitationsRef = FirebaseDatabase.getInstance().getReference("Users");
                String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace("@gmail.com", "");
                DatabaseReference currentUserInvitesRef = invitationsRef.child(currentUser).child("Invites");
                currentUserInvitesRef.child(invites.getKey()).removeValue();
                Toast.makeText(context, "Decline button", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return frdlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView frdName;
        ImageView frdImg;
        Button acceptbtn,declinebtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            frdName = itemView.findViewById(R.id.frdUser);
            frdImg = itemView.findViewById(R.id.frduserimg);
            acceptbtn = itemView.findViewById(R.id.accept);
            declinebtn = itemView.findViewById(R.id.decline);
        }
    }
}
