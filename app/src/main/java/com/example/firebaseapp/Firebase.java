package com.example.firebaseapp;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Firebase {

    private static Firebase firebase;

    private static final FirebaseDatabase database = FirebaseDatabase.getInstance("https://android-firebase-9538d-default-rtdb.asia-southeast1.firebasedatabase.app");

    private Firebase() {
    }

    public static Firebase getInstance() {
        if (firebase == null) {
            firebase = new Firebase();
        }
        return firebase;
    }

    public DatabaseReference getRef(String reference) {
        return database.getReference(reference);
    }



    public void setTextView (TextView textView, String reference) {
        DatabaseReference ref = database.getReference(reference);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String change = snapshot.getValue(String.class);
                textView.setText(change);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
