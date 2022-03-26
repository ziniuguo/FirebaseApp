package com.example.firebaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    TextView textView;
    Button buttonPost;

    // ListView
    ListView simpleList;
    ArrayList<String> titleList = new ArrayList<String>();
    ArrayList<String> contentList = new ArrayList<String>();
    ArrayList<String> indexList = new ArrayList<String>();

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://android-firebase-9538d-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference myRef = database.getReference("message");
    DatabaseReference myRef2 = database.getReference("2");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        simpleList = findViewById(R.id.simpleListView);
        textView = findViewById(R.id.testText);
        buttonPost = findViewById(R.id.postData);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                titleList.clear();
                titleList.add("Posts:");
                contentList.clear();
                contentList.add("Posts");
                indexList.clear();
                indexList.add("Posts");
                for(DataSnapshot value: dataSnapshot.getChildren()){
                    titleList.add((String) value.child("threadTitle").getValue());
                    contentList.add((String) value.child("threadContent").getValue());
                    indexList.add(value.getKey());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this,
                        R.layout.activity_listview, R.id.listText, titleList);
                simpleList.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("Button", "Failed to read value.", error.toException());
            }
        });

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String change = snapshot.getValue(String.class);
                textView.setText(change);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Intent navigate = new Intent(MainActivity.this, PostActivity.class);
        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(navigate);
            }
        });


        // tap on the post
        Intent postDetail = new Intent(MainActivity.this, ThreadActivity.class);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // delete post
                // myRef.child(indexList.get(position)).removeValue();
                postDetail.putExtra("titleKey", titleList.get(position));
                postDetail.putExtra("contentKey", contentList.get(position));
                postDetail.putExtra("indexKey", indexList.get(position));
                startActivity(postDetail);
            }
        });
    }


}