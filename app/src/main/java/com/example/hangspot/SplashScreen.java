package com.example.hangspot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hangspot.Models.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashScreen extends AppCompatActivity {

    TextView title,signup;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GoogleSignInClient signInClient;
    Button signUpbtn;
    ImageView applogo;
    ProgressDialog progressDialog;
    private static final int SIGN_IN=1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        SharedPreferences prefs = getSharedPreferences("userdata",MODE_PRIVATE);
        String name = prefs.getString("name",null);
        String email = prefs.getString("email",null);
        String image = prefs.getString("photo",null);
        if(name!=null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("name",name);
            intent.putExtra("email",email);
            intent.putExtra("image",image);
            startActivity(intent);
        }
        title = findViewById(R.id.hangspot);
        applogo = findViewById(R.id.applogo);
        signUpbtn = (Button) findViewById(R.id.signupbtn);
        progressDialog = new ProgressDialog(SplashScreen.this);
        progressDialog.setTitle("Creating Account....");
        progressDialog.setMessage("Hold on!! creating your Account");
        auth  = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Authentication();
                Toast.makeText(SplashScreen.this, "Creating account", Toast.LENGTH_SHORT).show();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        signInClient = GoogleSignIn.getClient(getApplicationContext(),gso);
        signUpbtn.setOnClickListener(view -> {
            Authentication();
            Toast.makeText(SplashScreen.this, "Signing..", Toast.LENGTH_SHORT).show();
        });
    }

    private void Authentication() {
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent,SIGN_IN);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                HomeActivity(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void HomeActivity(GoogleSignInAccount account) {
        if (account!=null){
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
            auth.signInWithCredential(firebaseCredential).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    String name = user.getDisplayName();
                    String email=user.getEmail();
                    String username = email.replace("@gmail.com","");
                    Log.d("username",username);
                    String image  = String.valueOf(user.getPhotoUrl());
                    DatabaseReference userRef = database.getReference("Users").child(username);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // User data already exists, you can update specific fields here
                                Users existingUser = snapshot.getValue(Users.class);
                                if (!existingUser.getProfileImg().equals(image)) {
                                    existingUser.setProfileImg(image);
                                    userRef.setValue(existingUser);
                                }
                            } else {
                                // User data doesn't exist, create a new user entry
                                Users muser = new Users(name, email);
                                muser.setProfileImg(image);
                                userRef.setValue(muser);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
//                    Users muser = new Users(name,email);
//                    muser.setProfileImg(image);
//                    database.getReference("Users").child(username).setValue(muser);
                    SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_PRIVATE).edit();
                    editor.putString("name", user.getDisplayName());
                    editor.putString("email", user.getEmail());
                    editor.putString("image", String.valueOf(user.getPhotoUrl()));
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
// Remove the intent.putExtra lines for "name", "email", and "image"
                    startActivity(intent);

                }
            });
        }
    }

}