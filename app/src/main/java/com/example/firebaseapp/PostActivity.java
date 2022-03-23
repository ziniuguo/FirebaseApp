package com.example.firebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends AppCompatActivity {
    EditText editText;
    Button buttonPush;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://android-firebase-9538d-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference myRef = database.getReference("message");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        editText = findViewById(R.id.post);
        buttonPush = findViewById(R.id.pushData);
        buttonPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference pushRef = myRef.push();
                myRef.child(pushRef.getKey()).setValue("Push test: " + editText.getText().toString());
//                Intent intent = new Intent(PostActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Don't start new activity
//                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
