package com.example.firebaseapp.match;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.firebaseapp.Firebase;
import com.example.firebaseapp.R;
import com.example.firebaseapp.thread.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MatchingActivity extends AppCompatActivity {

    Firebase firebase = Firebase.getInstance();
    DatabaseReference usersRef = firebase.getRef("UserGroups");

    Toolbar matchingToolbar;
    Button submitBtn;
    RadioGroup rG1, rG2, rG3, rG4;
    String rT1, rT2, rT3, rT4;
    EditText tgHandle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        rG1 = findViewById(R.id.radio_edu);
        rG2 = findViewById(R.id.radio_gender);
        rG3 = findViewById(R.id.radio_time);
        rG4 = findViewById(R.id.radio_style);
        tgHandle = findViewById(R.id.tgHandle);

        matchingToolbar = findViewById(R.id.matchToolbar);
        submitBtn = findViewById(R.id.btnMatch);
        setSupportActionBar(matchingToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if all button is checked
                int rG1Option = rG1.getCheckedRadioButtonId();
                int rG2Option = rG2.getCheckedRadioButtonId();
                int rG3Option = rG3.getCheckedRadioButtonId();
                int rG4Option = rG4.getCheckedRadioButtonId();
                String tgUsername = tgHandle.getText().toString();
                if (rG1Option == -1 ||rG2Option == -1 ||
                        rG3Option == -1 || rG4Option == -1 || tgUsername.equals("")){
                    Toast.makeText(MatchingActivity.this, "Please select all the options!", Toast.LENGTH_SHORT).show();
                } else {
                    RadioButton rB1 = findViewById(rG1Option);
                    RadioButton rB2 = findViewById(rG2Option);
                    RadioButton rB3 = findViewById(rG3Option);
                    RadioButton rB4 = findViewById(rG4Option);
                    rT1 = (String) rB1.getText();
                    rT2 = (String) rB2.getText();
                    rT3 = (String) rB3.getText();
                    rT4 = (String) rB4.getText();
                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot value : dataSnapshot.getChildren()) {
                                if (Objects.equals(value.child("userID").getValue(), MainActivity.USERID)) {
                                    Toast.makeText(MatchingActivity.this, "Personal info updated!", Toast.LENGTH_SHORT).show();
                                    value.getRef().child("eduLevel").setValue(rT1);
                                    value.getRef().child("gender").setValue(rT2);
                                    value.getRef().child("studyTime").setValue(rT3);
                                    value.getRef().child("studyStyle").setValue(rT4);
                                    value.getRef().child("tg").setValue(tgUsername);
                                    // An alternative
                                    break;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Failed to read value
                            Log.w("Button", "Failed to read value.", error.toException());
                        }
                    });
                    finish();
                }

            }
        });
    }

    // control the back arrow, etc.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                // onBackPressed();
                // i think onBackPressed also can lah
                finish();
                return true;
            default:
                return super.

                        onOptionsItemSelected(item);
        }
    }


}