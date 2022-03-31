package com.example.firebaseapp.thread;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.firebaseapp.R;
import com.example.firebaseapp.thread.models.Comment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ThreadActivity extends AppCompatActivity {
    Boolean usersThread = false;
    TextView threadTitleText;
    TextView threadContentText;
    TextView threadUserIDText;
    // Button deleteButton;
    Toolbar detailToolbar;
    EditText commentText;
    Button commentButton;
    ListView postList;
    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<String> contentList = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://android-firebase-9538d-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference threadsRef = database.getReference("Threads");
    DatabaseReference commentsRef = database.getReference("Comments");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

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
                    if (Objects.equals(value.getKey(), MainActivity.THREADID)) {
                        String title = String.valueOf(value.child("title").getValue());
                        String thread = String.valueOf(value.child("thread").getValue());
                        String ID = String.valueOf(value.child("userId").getValue());
                        String time = String.valueOf(value.child("time").getValue());
                        threadTitleText.setText(title);
                        threadContentText.setText(thread);
                        String authorID = "Author: " + ID + "\n" + "Created Time: " + time.substring(0, 16);
                        threadUserIDText.setText(authorID);

                        if (String.valueOf(value.child("userId").getValue()).equals(MainActivity.USERID)) {
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

        //Post comment
        commentText = findViewById(R.id.commentText);
        commentButton = findViewById(R.id.commentButton);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentText.getText().toString().equals("")) {
                    Toast.makeText(ThreadActivity.this, R.string.empty_comment, Toast.LENGTH_SHORT).show();
                } else {
                    Timestamp TS = new Timestamp(System.currentTimeMillis());
                    Comment comment = new Comment(MainActivity.USERID, MainActivity.THREADID, commentText.getText().toString());
                    DatabaseReference pushRef = commentsRef.child(Objects.requireNonNull(commentsRef.push().getKey()));
                    pushRef.child("userId").setValue(comment.getUserId());
                    pushRef.child("threadId").setValue(comment.getThreadId());
                    pushRef.child("status").setValue(comment.getStatus());
                    pushRef.child("rating").setValue(comment.getRating());
                    pushRef.child("comment").setValue(comment.getComment());
                    pushRef.child("time").setValue(TS.getTime());
                    Toast.makeText(ThreadActivity.this, "New comment posted!", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition( 0, 0);
                    startActivity(getIntent());
                    overridePendingTransition( 0, 0);
                }
            }
        });

        //View comments list
        postList = findViewById(R.id.simpleListViewComments);

        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                titleList.clear();
                contentList.clear();
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    if (Objects.equals(value.child("status").getValue(), "Active") && Objects.equals(value.child("threadId").getValue(), MainActivity.THREADID)) {

                        // this is stupid, it eats up time complexity O(n)
                        // and space complexity O(1).
                        // I should use a different structure like queue.
                        // But nvm, probably we can solve this problem
                        // if we want to implement sorting function later.

                        Collections.reverse(titleList);
                        titleList.add((String) value.child("userId").getValue());
                        Collections.reverse(titleList);

                        Collections.reverse(contentList);
                        contentList.add((String) value.child("comment").getValue());
                        Collections.reverse(contentList);

                    }
                }
//                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,
//                        R.layout.activity_listview, R.id.listText, titleList);

                MyAdapter myAdapter = new MyAdapter(ThreadActivity.this, R.layout.activity_listviewcomments,
                        R.id.listTextComments, titleList, contentList);


                postList.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("Button", "Failed to read value.", error.toException());
            }
        });
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
                    threadsRef.child(MainActivity.THREADID).getRef().removeValue();
                    Toast.makeText(ThreadActivity.this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ThreadActivity.this, "No Permission", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
