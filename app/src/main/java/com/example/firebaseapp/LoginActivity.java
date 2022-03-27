package com.example.firebaseapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.models.ThreadClass;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    // set toolbar
    Toolbar loginToolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // set toolbar back arrow
        loginToolbar = findViewById(R.id.loginToolbar);
        setSupportActionBar(loginToolbar);
        if (getSupportActionBar() != null){
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
                Toast.makeText(LoginActivity.this, "Login Attempt", Toast.LENGTH_LONG).show();
                MainActivity.loginStatus = true;
                finish();
                return true;
            case android.R.id.home:
                // onBackPressed();
                // i think onBackPressed also can lah
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
