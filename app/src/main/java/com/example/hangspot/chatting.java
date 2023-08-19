package com.example.hangspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hangspot.Adapter.chatAdapter;
import com.example.hangspot.Models.messagemodel;
import com.example.hangspot.databinding.ActivityChattingBinding;
import com.example.hangspot.fragments.first_screen_frag;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class chatting extends AppCompatActivity {
    FirebaseDatabase database;
    TextView username,useremail;
    ImageView chatprofile,backbtn,searchbtn;
    ImageButton sendbtn;
    EditText messgesArea;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        username = findViewById(R.id.chatUserName);
        chatprofile = findViewById(R.id.chatprofile);
        backbtn = findViewById(R.id.backbutton);
        searchbtn = findViewById(R.id.searchButton);
        sendbtn = findViewById(R.id.sendmsg);
        messgesArea = findViewById(R.id.typemsg);
        recyclerView =findViewById(R.id.recyclerViewchat);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        String receiveEmail = getIntent().getStringExtra("email");
        String receiveId = receiveEmail.replace("@gmail.com","");
        String name = getIntent().getStringExtra("name");
        String senderId = getIntent().getStringExtra("senderId");
        String profilepic = getIntent().getStringExtra("profilepic");
        username.setText(name);
        Glide.with(getApplicationContext()).load(profilepic).circleCrop().into(chatprofile);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        final ArrayList<messagemodel> messagemodelArrayList = new ArrayList<>();
        final chatAdapter chatAdapter = new chatAdapter(messagemodelArrayList,getApplicationContext(),receiveId);
        recyclerView.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DatabaseReference invitationsRef = FirebaseDatabase.getInstance().getReference("Users");
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace("@gmail.com", "");
        final String senderRoom = receiveId;
        final String reciveroom = currentUser;
        DatabaseReference senderChat=invitationsRef.child(currentUser).child("chats");
        senderChat.child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messagemodelArrayList.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            messagemodel model = snapshot1.getValue(messagemodel.class);
                            model.setMessageid(snapshot1.getKey());
                            messagemodelArrayList.add(model);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference invitationsRef = FirebaseDatabase.getInstance().getReference("Users");
                String message = messgesArea.getText().toString();
                final messagemodel messagemodel = new messagemodel(currentUser,receiveId,message,new Date().getTime());
                if(message.isEmpty()){
                    Toast.makeText(chatting.this, "Type message", Toast.LENGTH_SHORT).show();
                }
                else {
                    messgesArea.setText("");
                    DatabaseReference senderChatRef = invitationsRef.child(currentUser).child("chats").child(senderRoom).push();
                    senderChatRef.setValue(messagemodel);

                    DatabaseReference receiverChatRef = invitationsRef.child(receiveId).child("chats").child(reciveroom).push();
                    receiverChatRef.setValue(messagemodel);
                    messagemodelArrayList.add(messagemodel);
                    chatAdapter.notifyDataSetChanged();

                }

            }

        });
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(layoutManager.findLastCompletelyVisibleItemPosition()<chatAdapter.getItemCount()-1){
                    layoutManager.smoothScrollToPosition(recyclerView,new RecyclerView.State(),layoutManager.findLastCompletelyVisibleItemPosition());
                }
            }
        }, 50);
    }
}