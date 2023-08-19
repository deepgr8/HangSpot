package com.example.hangspot.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hangspot.Adapter.call_screenAdapter;
import com.example.hangspot.Adapter.first_screen_adapter;
import com.example.hangspot.Models.Users;
import com.example.hangspot.R;
import com.example.hangspot.databinding.FragmentCallScreenBinding;
import com.example.hangspot.databinding.FragmentFirstScreenFragBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class call_screen extends Fragment {
    FragmentCallScreenBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    public call_screen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCallScreenBinding.inflate(inflater,container,false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        call_screenAdapter call_screenAdapter = new call_screenAdapter(list,getContext());
        binding.callRecycler.setAdapter(call_screenAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.callRecycler.setLayoutManager(layoutManager);
        binding.callRecycler.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            String currentUserId = auth.getCurrentUser().getEmail().replace("@gmail.com", "");

            firebaseDatabase.getReference().child("Users").child(currentUserId).child("friends").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot friendSnapshot : snapshot.getChildren()) {
                        String friendId = friendSnapshot.getKey();
                        firebaseDatabase.getReference().child("Users").child(friendId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                Users user = userSnapshot.getValue(Users.class);
                                list.add(user);
                                call_screenAdapter.notifyDataSetChanged();
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
        }

        return binding.getRoot();

    }
}