package com.example.firebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ThreadActivity extends AppCompatActivity {


    TextView threadTitleText;
    TextView threadContentText;
    Toolbar detailToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        Intent postDetailIntent = getIntent();
        String title = postDetailIntent.getStringExtra("titleKey");
        String content = postDetailIntent.getStringExtra("contentKey");
        String index = postDetailIntent.getStringExtra("indexKey");

        threadTitleText = findViewById(R.id.threadTitleText);
        threadContentText = findViewById(R.id.threadContentText);

        // show back(up) button
        detailToolbar = findViewById(R.id.detailToolbar);
        setSupportActionBar(detailToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        threadTitleText.setText(title);
        threadContentText.setText(content);

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
