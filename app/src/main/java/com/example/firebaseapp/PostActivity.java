package com.example.firebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class PostActivity extends AppCompatActivity {
    EditText editText;
    EditText titleText;
    Button buttonPush;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://android-firebase-9538d-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference myRef = database.getReference("message");

    // set toolbar
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // set toolbar back arrow
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        editText = findViewById(R.id.threadContent);
        titleText = findViewById(R.id.threadTitle);
        buttonPush = findViewById(R.id.pushData);
        buttonPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference pushRef = myRef.push();
                String genKey = pushRef.getKey();
                myRef.child(genKey).child("threadContent").setValue("Content: " + editText.getText().toString());
                myRef.child(genKey).child("threadTitle").setValue("Title: " + titleText.getText().toString());
//                Intent intent = new Intent(PostActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Don't start new activity
//                startActivity(intent);
                finish();
            }
        });

    }

    // control the back arrow
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // onBackPressed();
            // i think onBackPressed also can lah
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
