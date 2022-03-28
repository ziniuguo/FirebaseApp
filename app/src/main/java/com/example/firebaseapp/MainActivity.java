package com.example.firebaseapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    public static String THREADID = "refThreadId";

    // login status and shared preferences
    public static String loginStatus = "N";
    public static String USERID = "refUserId";
    SharedPreferences mPreferences;

    TextView adminNotice;
//    Button buttonPost;

    // toolbar
    Toolbar mainToolbar;

    // ListView
    ListView postList;
    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<String> contentList = new ArrayList<>();
    ArrayList<String> indexList = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://android-firebase-9538d-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference threadsRef = database.getReference("Threads");
    DatabaseReference adminRef = database.getReference("Admins");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // login and shared preferences
        String sharedPrefFile = "com.example.firebaseappshareprefs";
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        loginStatus = mPreferences.getString("LoginStatusKey", "N");
        USERID = mPreferences.getString("UserIDKey", "refUserId");


        postList = findViewById(R.id.simpleListView);
        adminNotice = findViewById(R.id.adminNotice);
//        buttonPost = findViewById(R.id.postData);

        // set mainToolbar so that the app name is shown
        mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);

        threadsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                titleList.clear();
                contentList.clear();
                indexList.clear();
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    if (Objects.equals(value.child("status").getValue(), "Active")) {

                        // this is stupid, it eats up time complexity O(n)
                        // and space complexity O(1).
                        // I should use a different structure like queue.
                        // But nvm, probably we can solve this problem
                        // if we want to implement sorting function later.

                        Collections.reverse(titleList);
                        titleList.add((String) value.child("title").getValue());
                        Collections.reverse(titleList);

                        Collections.reverse(contentList);
                        contentList.add((String) value.child("thread").getValue());
                        Collections.reverse(contentList);

                        Collections.reverse(indexList);
                        indexList.add(value.getKey());
                        Collections.reverse(indexList);
                    }
                }
//                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,
//                        R.layout.activity_listview, R.id.listText, titleList);

                MyAdapter myAdapter = new MyAdapter(MainActivity.this, R.layout.activity_listview,
                        R.id.listText, titleList, contentList);


                postList.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("Button", "Failed to read value.", error.toException());
            }
        });

        adminRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String change = snapshot.getValue(String.class);
                adminNotice.setText(change);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        Intent navigate = new Intent(MainActivity.this, PostActivity.class);
//        buttonPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //Eventually query for userId from firebase as well
//                navigate.putExtra(USERID, "TestUserHardCoded");
//
//                startActivity(navigate);
//            }
//        });


        // tap on the post
        Intent postDetail = new Intent(MainActivity.this, ThreadActivity.class);
        postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (indexList.get(position) != null) {
                    Log.d("Click:", indexList.get(position));
                    postDetail.putExtra("THREADID", indexList.get(position));

                    //Eventually query for userId from firebase as well
                    postDetail.putExtra("USERID", USERID);

                    startActivity(postDetail);
                }
            }
        });
    }


    // don't forget to inflate!
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                if (loginStatus.equals("N")) {
                    Toast.makeText(MainActivity.this, "Please login!", Toast.LENGTH_LONG).show();
                } else {
                    Intent postIntent = new Intent(MainActivity.this, PostActivity.class);

                    //Eventually query for userId from firebase as well
                    postIntent.putExtra("USERID", USERID);

                    startActivity(postIntent);
                }
                return true;
            case R.id.action_loginPage:
                if (loginStatus.equals("N")) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Logout " + USERID)
                            .setMessage("You are logged in already. Are you sure you want to logout?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    loginStatus = "N";
                                    USERID="refUserID";
                                    Toast.makeText(MainActivity.this, "You are logged out", Toast.LENGTH_LONG).show();
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_logout)
                            .show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // shared pref edit
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferenceEditor = mPreferences.edit();
        preferenceEditor.putString("LoginStatusKey", loginStatus);
        preferenceEditor.putString("UserIDKey", USERID);
//        Toast.makeText(MainActivity.this, loginStatus, Toast.LENGTH_LONG).show();
        preferenceEditor.apply();
    }

}