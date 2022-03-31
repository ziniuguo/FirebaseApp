package com.example.firebaseapp.profile;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.firebaseapp.R;
import com.example.firebaseapp.thread.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Toolbar profileToolbar;
    TextView usernameTextView;
    Button formButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        formButton = findViewById(R.id.buttonprofile);
//        formButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        profileToolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(profileToolbar);
        // We don't need the up button anymore!
//        if (getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }

        usernameTextView = findViewById(R.id.usernameTextView);
        String welcomeText = "Welcome " + MainActivity.USERID + "!";
        usernameTextView.setText(welcomeText);

        // Initialize and assign variable
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.profile);

        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.dashboard:
                        Toast.makeText(ProfileActivity.this, "Not implemented yet. You can go and do your part there!", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.thread:
//                        startActivity(new Intent(getApplicationContext(),MainActivity.class)
//                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();  // optional depending on your needs
        finish();
        overridePendingTransition(0, 0);

    }

    // don't forget to inflate!
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    // control the back arrow, etc.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.profile_logout:
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Logout " + MainActivity.USERID)
                        .setMessage("You are logged in already. Are you sure you want to logout?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.loginStatus = "N";
                                MainActivity.USERID="refUserID";
                                Toast.makeText(ProfileActivity.this, "You are logged out", Toast.LENGTH_SHORT).show();

                                // logout以后别忘了finish，回Main
                                finish();
                                overridePendingTransition(0, 0);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.ic_logout_black)
                        .show();
                return true;
//                we don't need up button anymore!
//            case android.R.id.home:
//                // onBackPressed();
//                // i think onBackPressed also can lah
//                finish();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // shared pref edit. If you quit on this page, you still need to store login status!
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferenceEditor = MainActivity.mPreferences.edit();
        preferenceEditor.putString("LoginStatusKey", MainActivity.loginStatus);
        preferenceEditor.putString("UserIDKey", MainActivity.USERID);
//        Toast.makeText(MainActivity.this, loginStatus, Toast.LENGTH_SHORT).show();
        preferenceEditor.apply();
    }
}
