package com.example.hangspot.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hangspot.Adapter.first_screen_adapter;
import com.example.hangspot.MainActivity;
import com.example.hangspot.Models.Users;
import com.example.hangspot.R;
import com.example.hangspot.databinding.FragmentFirstScreenFragBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class first_screen_frag extends Fragment {
    public first_screen_frag(){

    }
    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    FragmentFirstScreenFragBinding binding;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFirstScreenFragBinding.inflate(inflater,container,false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        first_screen_adapter adapter = new first_screen_adapter(list,getContext());
        binding.chatListRecycler.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatListRecycler.setLayoutManager(layoutManager);
        binding.chatListRecycler.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace("@gmail.com", "");
        DatabaseReference currentUserChatsRef = firebaseDatabase.getReference().child("Users").child(currentUser).child("chats");
        currentUserChatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    String otherUserId = chatSnapshot.getKey();
                    DatabaseReference otherUserRef = firebaseDatabase.getReference().child("Users").child(otherUserId);
                    otherUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            Users user = userSnapshot.getValue(Users.class);
                            if (user != null) {
                                list.add(user);
                                adapter.notifyDataSetChanged();
                            }
//                            if (list.isEmpty()){
//                                binding.startConversationTextView.setVisibility(View.VISIBLE);
//                                binding.welcome.setVisibility(View.VISIBLE);
//                            }
                            if (!list.isEmpty()){
                                binding.startConversationTextView.setVisibility(View.GONE);
                                binding.welcome.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//
//        firebaseDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list.clear();
//                for (DataSnapshot snapshot1:snapshot.getChildren()){
//                    Users users = snapshot1.getValue(Users.class);
//                    users.setName(users.getName());
//                    list.add(users);
//
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        return binding.getRoot();
    }

}