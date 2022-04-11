package com.example.firebaseapp.match;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.firebaseapp.R;
import com.example.firebaseapp.profile.ProfileActivity;
import com.example.firebaseapp.thread.LoginActivity;
import com.example.firebaseapp.thread.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MatchActivity extends AppCompatActivity {

    // toolbar
    Toolbar matchToolbar;

    // bottom bar
    BottomNavigationView bottomNavigationView;

    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<Users> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        recyclerView = findViewById(R.id.matches);
        database = FirebaseDatabase.getInstance().getReference("UserGroups");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<Users>();
        myAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        // bottom bar
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        matchToolbar = findViewById(R.id.matchToolbar);
        setSupportActionBar(matchToolbar);

        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.dashboard:
                        return true;
                    case R.id.thread:
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        finish();
                        startActivity(new Intent(MatchActivity.this, ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (Objects.equals(dataSnapshot.child("userID").getValue(), MainActivity.USERID)){
                        String self_matched = (String) dataSnapshot.child("matched").getValue();
                        String[] elements = self_matched.split(",");
                        List<String> fixedLengthList = Arrays.asList(elements);
                        ArrayList<String> matchedlist = new ArrayList<String>(fixedLengthList);
                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                            if (matchedlist.contains((String) dataSnapshot1.child("userID").getValue())){
                                Users user = dataSnapshot1.getValue(Users.class);
                                list.add(user);

                            }
                        }
                        break;
                    }


                }
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();  // optional depending on your needs
        finish();
        overridePendingTransition(0, 0);
    }

    // shared pref edit. If you quit on this page, you still need to store login status!
    // don't delete this.
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferenceEditor = MainActivity.mPreferences.edit();
        preferenceEditor.putString("LoginStatusKey", MainActivity.loginStatus);
        preferenceEditor.putString("UserIDKey", MainActivity.USERID);
//        Toast.makeText(MainActivity.this, loginStatus, Toast.LENGTH_SHORT).show();
        preferenceEditor.apply();
    }
}