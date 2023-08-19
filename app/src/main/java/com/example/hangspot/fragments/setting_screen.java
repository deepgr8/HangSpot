package com.example.hangspot.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;

import com.example.hangspot.Adapter.settingAdapter;
import com.example.hangspot.Models.FriendModel;
import com.example.hangspot.R;
import com.example.hangspot.databinding.FragmentSettingScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class setting_screen extends Fragment {
    FragmentSettingScreenBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference invitationRef;
    public setting_screen() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void fetchInvites(String currentUsr) {
        DatabaseReference invitess = invitationRef.child(currentUsr).child("Invites");
        invitess.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<FriendModel> invitationList = new ArrayList<>();
                for (DataSnapshot inviteSnapshot : snapshot.getChildren()) {
                    FriendModel friendModel = inviteSnapshot.getValue(FriendModel.class);
                    friendModel.setKey(inviteSnapshot.getKey());
                    invitationList.add(friendModel);
                }
                settingAdapter adapter = new settingAdapter(invitationList,getContext());
                binding.inviteRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingScreenBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        firebaseDatabase = FirebaseDatabase.getInstance();
        invitationRef = firebaseDatabase.getReference("Users");
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.inviteRecycler.setLayoutManager(layoutManager);
        binding.inviteRecycler.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        settingAdapter adapter = new settingAdapter(new ArrayList<>(), getContext());
        binding.inviteRecycler.setAdapter(adapter);

        String currentUsr = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace("@gmail.com", "");
        fetchInvites(currentUsr);
        return rootView;
    }
}