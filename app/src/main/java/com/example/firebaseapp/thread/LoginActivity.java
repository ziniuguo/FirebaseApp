package com.example.firebaseapp.thread;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.firebaseapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    // set toolbar
    Toolbar loginToolbar;

    // set text input
    EditText IDeditText;
    EditText pwd;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://android-firebase-9538d-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference usersRef = database.getReference("UserGroups");


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // set toolbar back arrow
        loginToolbar = findViewById(R.id.loginToolbar);

        // set text input
        IDeditText = findViewById(R.id.IDInput);
        pwd = findViewById(R.id.pwdInput);

        setSupportActionBar(loginToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


    }


    // don't forget to inflate!
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    // control the back arrow, etc.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_login:
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean exist = false;
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            if (Objects.equals(value.child("userID").getValue(), IDeditText.getText().toString())) {
                                // Toast.makeText(LoginActivity.this, "Account Exist", Toast.LENGTH_LONG).show();
                                exist = true;
                                if (Objects.equals(value.child("userPwd").getValue(), pwd.getText().toString())) {
                                    MainActivity.loginStatus = "Y";
                                    MainActivity.USERID = IDeditText.getText().toString();
                                    Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Wrong username or password!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        if (!exist) {
                            if (IDeditText.getText().toString().equals("") || pwd.getText().toString().equals("")) {
                                Toast.makeText(LoginActivity.this, "Username and password cannot be empty!", Toast.LENGTH_LONG).show();
                            } else if (IDeditText.getText().toString().length() != 7) {
                                Toast.makeText(LoginActivity.this, "Please use your studentID as username", Toast.LENGTH_LONG).show();
                            } else {
                                DatabaseReference pushRef = usersRef.child(Objects.requireNonNull(usersRef.push().getKey()));
                                pushRef.child("userID").setValue(IDeditText.getText().toString());
                                pushRef.child("userPwd").setValue(pwd.getText().toString());
                                Toast.makeText(LoginActivity.this, "Signup and login success", Toast.LENGTH_LONG).show();
                                MainActivity.loginStatus = "Y";
                                MainActivity.USERID = IDeditText.getText().toString();
                                finish();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Failed to read value
                        Log.w("Button", "Failed to read value.", error.toException());
                    }
                });

                return true;
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
