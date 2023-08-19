package com.example.hangspot.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hangspot.MainActivity;
import com.example.hangspot.Models.FriendModel;
import com.example.hangspot.R;
import com.example.hangspot.databinding.FragmentFindfriendBinding;
import com.example.hangspot.databinding.FragmentFirstScreenFragBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class findfriend extends Fragment {
    FragmentFindfriendBinding binding;
    ImageView backbtn;
    FirebaseDatabase firebaseDatabase;
    LinearLayout linearLayout;
    private DatabaseReference usersRef;
    private DatabaseReference invitationsRef;
    public findfriend() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");
        invitationsRef = database.getReference("invitations");
        binding = FragmentFindfriendBinding.inflate(inflater,container,false);
        linearLayout = binding.linearfindFrd;
        binding.findbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String femail = binding.findemail.getText().toString();
                String fusername = femail.replace("@gmail.com","");
                findAndSendInvitation(fusername);
            }
        });
        binding.findbackbtn.findViewById(R.id.findbackbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    private void findAndSendInvitation(final String fusername) {
        DatabaseReference userToSearch = usersRef.child(fusername);
        String currentUsr = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace("@gmail.com","");
        userToSearch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && !fusername.equals(currentUsr)){
                    linearLayout.setVisibility(View.VISIBLE);
                    String friendUserid = fusername;
                    String currentUsrID = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace("@gmail.com","");
                    String invitationId = usersRef.child(currentUsrID).child("Invites").push().getKey();
                    String imageUrl = snapshot.child("profileImg").getValue(String.class);
                    FriendModel friendModel = new FriendModel(currentUsrID,friendUserid,imageUrl,false,false);
                    binding.findnameUser.setText(friendUserid);
                    Glide.with(requireContext()).load(imageUrl).circleCrop().into(binding.finduserimg);
                    binding.sendReq.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getContext(), "Invitation sended", Toast.LENGTH_SHORT).show();
                            usersRef.child(friendUserid).child("Invites").child(invitationId).setValue(friendModel);
                            binding.sendReq.setImageResource(R.drawable.baseline_check_circle_24);
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "Check email Id or It's You", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}