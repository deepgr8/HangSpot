package com.example.hangspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hangspot.Models.Users;
import com.example.hangspot.fragments.call_screen;
import com.example.hangspot.fragments.findfriend;
import com.example.hangspot.fragments.first_screen_frag;
import com.example.hangspot.fragments.setting_screen;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextView username,useremail;
    ImageView userimage;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    NavigationView nav;
    Toolbar toolbar;

    BottomNavigationView bottomNavigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    ImageView addfriendd;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://hangspot-d420f-default-rtdb.firebaseio.com/");
    private long backPressedTime = 0;
    private static final long DOUBLE_PRESS_INTERVAL = 2000;
    Button logout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.FrameContainer,new first_screen_frag()).commit();
        nav = findViewById(R.id.navigation_drawer);
        View view = nav.getHeaderView(0);
        toolbar = findViewById(R.id.toolbar);
        username = view.findViewById(R.id.user_name);
        addfriendd = findViewById(R.id.addfriend);
        addfriendd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.FrameContainer,new findfriend()).commit();
                toggle.setDrawerIndicatorEnabled(false);
                toolbar.setVisibility(View.INVISIBLE);
            }
        });
        useremail = view.findViewById(R.id.user_email);
        userimage = view.findViewById(R.id.circularImageView_nav);
        Intent intent = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences("userdata",MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String email = sharedPreferences.getString("email", "");
        String imageUrl = sharedPreferences.getString("image", "");
//        String name= intent.getStringExtra("name");
//        String email = intent.getStringExtra("email");
        username.setText(name);
        useremail.setText(email);
//        String  imageUrl = intent.getStringExtra("image");
        Glide.with(this).load(imageUrl).circleCrop().into(userimage);
        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottomNav);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment temp;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.chat_bubble){
                    temp = new first_screen_frag();
                    toggle.setDrawerIndicatorEnabled(true);
                    toolbar.setVisibility(View.VISIBLE);
                }
                else if (item.getItemId()==R.id.users){
                    temp = new call_screen();
                    toggle.setDrawerIndicatorEnabled(false);
                    toolbar.setVisibility(View.INVISIBLE);
                }
                else if (item.getItemId()==R.id.setting) {
                    temp = new setting_screen();
                    toggle.setDrawerIndicatorEnabled(false);
                    toolbar.setVisibility(View.INVISIBLE);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.FrameContainer,temp).commit();

                return true;
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        gsc= GoogleSignIn.getClient(this,gso);
        
    }

    public void setDrawerEnabled(boolean enabled) {
        if (enabled) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - backPressedTime < DOUBLE_PRESS_INTERVAL) {
            finishAffinity();
            finish();
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            backPressedTime = currentTime;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment newTemp;
        if (item.getItemId()==R.id.no_notification){
            Toast.makeText(this, "No notification", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId()==R.id.feedback) {
            Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId()==R.id.invitations) {
            newTemp = new setting_screen();
            toggle.setDrawerIndicatorEnabled(false);
            toolbar.setVisibility(View.INVISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameContainer,newTemp).commit();
            drawerLayout.close();
        } else if (item.getItemId()==R.id.logout) {
            gsc.signOut();
            SharedPreferences.Editor editor = getSharedPreferences("userdata",MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
            Intent intent1 = new Intent(getApplicationContext(),SplashScreen.class);
            startActivity(intent1);
            finish();
        }
        return true;
    }
}