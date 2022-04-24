package com.example.firebaseapp.thread;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.firebaseapp.Firebase;
import com.example.firebaseapp.R;
import com.example.firebaseapp.thread.models.Comment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.Objects;

public class ThreadActivity extends AppCompatActivity {
    Boolean usersThread = false;

    // Check image post
    Boolean isImagePost;


    TextView threadTitleText;
    TextView threadContentText;
    TextView threadUserIDText;
    ImageView threadImage;
    Toolbar detailToolbar;
    EditText commentText;
    Button commentButton;


    LinearLayout dynamicLayout;

    Firebase firebase = Firebase.getInstance();
    DatabaseReference threadsRef = firebase.getRef("Threads");
    DatabaseReference commentsRef = firebase.getRef("Comments");

    // Storage database
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        threadTitleText = findViewById(R.id.threadTitleText);
        threadContentText = findViewById(R.id.threadContentText);
        threadUserIDText = findViewById(R.id.threadUserIDText);
        threadImage = findViewById(R.id.pstImg);
        dynamicLayout = findViewById(R.id.LLayout);


        // show back(up) button
        detailToolbar = findViewById(R.id.detailToolbar);
        setSupportActionBar(detailToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        threadsRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        setImage(MainActivity.THREADID);
                        if (String.valueOf(value.child("userId").getValue()).equals(MainActivity.USERID)) {
                            usersThread = true;
                        }
                        break;
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
                    commentText.setText("");
                }
            }
        });


        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                int viewCount = dynamicLayout.getChildCount();
                if (viewCount != 3) {
                    dynamicLayout.removeViews(3,
                            dynamicLayout.getChildCount() - 3);
                }
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    if (Objects.equals(value.child("status").getValue(), "Active") && Objects.equals(value.child("threadId").getValue(), MainActivity.THREADID)) {

                        TextView commentView = new TextView(ThreadActivity.this);
                        commentView.setText(
                                value.child("userId").getValue()
                                        + ":\n"
                                        + value.child("comment").getValue()
                                        + "\n"
                        );

                        if (Objects.equals(value.child("userId").getValue(), MainActivity.USERID)) {
                            commentView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new AlertDialog.Builder(ThreadActivity.this)
                                            .setTitle("Delete Comment")
                                            .setMessage("Do you want to delete this comment?\n\n" +
                                                    "For test use:\n\ncomment firebase ref:\n\n" + value.getRef()
                                                    + "\n\nComment KEY:\n\n" + value.getKey()
                                            )

                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    value.getRef().removeValue();
                                                    Toast.makeText(ThreadActivity.this, "you want to delete: " + value.getKey(), Toast.LENGTH_SHORT).show();
                                                    view.setOnClickListener(null);
                                                }
                                            })
                                            // A null listener allows the button to dismiss the dialog and take no further action.
                                            .setNegativeButton(android.R.string.no, null)
                                            .setIcon(R.drawable.ic_delete_black)
                                            .show();
                                }
                            });
                        }
                        dynamicLayout.addView(commentView);

                    }
                }

                if (dynamicLayout.getChildCount() == 3) {
                    TextView placeholderView = new TextView(ThreadActivity.this);
                    placeholderView.setText(R.string.comment_placeholder);
                    dynamicLayout.addView(placeholderView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("Button", "Failed to read value.", error.toException());
            }
        });
    }

    private void setImage(String key) {
        storageReference.child(key).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for
                // set image to imageview
                isImagePost = true;
                Picasso.get().load(uri).into(threadImage);
                // threadImage.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                isImagePost = false;
            }
        });
    }

    private void delImage(String key) {
        storageReference.child(key).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
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
                    new AlertDialog.Builder(ThreadActivity.this)
                            .setTitle("Delete Thread")
                            .setMessage("Do you want to delete this Thread?\n\n" +
                                    "For test use:"
                                    + "\n\nMainAc ThreadID:\n\n"
                                    + MainActivity.THREADID
                            )

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    commentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot value : snapshot.getChildren()) {

                                                if (Objects.equals(value.child("threadId").getValue(), MainActivity.THREADID)) {
                                                    value.getRef().removeValue();
                                                    break;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    threadsRef.child(MainActivity.THREADID).getRef().removeValue();
                                    if (isImagePost) {
                                        delImage(MainActivity.THREADID);
                                    }
                                    Toast.makeText(ThreadActivity.this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_delete_black)
                            .show();

                } else {
                    Toast.makeText(ThreadActivity.this, "No Permission", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
