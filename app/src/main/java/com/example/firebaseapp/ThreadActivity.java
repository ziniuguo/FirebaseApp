package com.example.firebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ThreadActivity extends AppCompatActivity {

    // A stupid way. Got better solution?
    String publicThreadID;

    Boolean usersThread = false;
    TextView threadTitleText;
    TextView threadContentText;
    TextView threadUserIDText;
    // Button deleteButton;
    Toolbar detailToolbar;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://android-firebase-9538d-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference threadsRef = database.getReference("Threads");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        Intent intent = getIntent();
        String threadId = intent.getStringExtra("THREADID");

        // A stupid way. Got better solution?
        publicThreadID = threadId;

        String userId = intent.getStringExtra("USERID");

//        deleteButton = findViewById(R.id.deleteButton);
        threadTitleText = findViewById(R.id.threadTitleText);
        threadContentText = findViewById(R.id.threadContentText);
        threadUserIDText = findViewById(R.id.threadUserIDText);

        // show back(up) button
        detailToolbar = findViewById(R.id.detailToolbar);
        setSupportActionBar(detailToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        threadsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot value : snapshot.getChildren()) {
                    if (Objects.equals(value.getKey(), threadId)) {
                        String title = String.valueOf(value.child("title").getValue());
                        String thread = String.valueOf(value.child("thread").getValue());
                        String ID = String.valueOf(value.child("userId").getValue());
                        String time = String.valueOf(value.child("time").getValue());
                        threadTitleText.setText(title);
                        threadContentText.setText(thread);
                        String authorID = "Author: " + ID + "\n" + "Created Time: " + time.substring(0, 16);
                        threadUserIDText.setText(authorID);

                        if (String.valueOf(value.child("userId").getValue()).equals(userId)) {
                            usersThread = true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Post", "Failed to read value.", error.toException());
            }
        });

//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (usersThread) {
//                    threadsRef.child(threadId).getRef().removeValue();
//                    finish();
//                }
//            }
//        });

    }


    // don't forget to inflate!
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    // control the back arrow, and delete etc.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.deleteButton:
                if (usersThread) {
                    threadsRef.child(publicThreadID).getRef().removeValue();
                    Toast.makeText(ThreadActivity.this, "Post deleted successfully", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(ThreadActivity.this, "No Permission", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
