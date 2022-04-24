package com.example.firebaseapp.thread;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.firebaseapp.Firebase;
import com.example.firebaseapp.R;
import com.example.firebaseapp.thread.models.ThreadClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

public class PostActivity extends AppCompatActivity {
    EditText contentText;
    EditText titleText;

    Firebase firebase = Firebase.getInstance();
    DatabaseReference myRef = firebase.getRef("Threads");

    // set toolbar
    Toolbar postToolbar;

    // add image to post
    ArrayList<Uri> filePath = new ArrayList<>();
    final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    // test image
    ImageView uploadImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        uploadImageView = findViewById(R.id.uploadImg);

        // set toolbar back arrow
        postToolbar = findViewById(R.id.postToolbar);
        setSupportActionBar(postToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        contentText = findViewById(R.id.threadContent);
        titleText = findViewById(R.id.threadTitle);

        uploadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath.isEmpty()) {
                    SelectImage();
                } else {
                    new AlertDialog.Builder(PostActivity.this)
                            .setTitle("Delete Thread")
                            .setMessage("Do you want to remove this image from your post?\n\n" +
                                    "For test use:"
                                    + "\n\nfilePath:\n\n"
                                    + filePath
                            )

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    filePath.clear();
                                    uploadImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_upload_img));
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_delete_black)
                            .show();
                }

            }
        });
    }

    // don't forget to inflate!
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_menu, menu);
        return true;
    }

    // control the back arrow, etc.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_send:
                if (titleText.getText().toString().equals("") || contentText.getText().toString().equals("")) {
                    Toast.makeText(PostActivity.this, R.string.empty_title_or_content, Toast.LENGTH_SHORT).show();
                } else {
                    String myKey = myRef.push().getKey();

                    /*
                    逻辑是这样的
                    upload image既上传image又上传text
                    upload text只是上传text
                    upload image会在success listener接收到成功后再upload text，Toast，再finish
                    而upload text没有toast和finish要手动加
                     */
                    if (!filePath.isEmpty()) {
                        uploadImage(myKey);
                    } else {
                        uploadText(myKey);
                        Toast.makeText(PostActivity.this, "New post created!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
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

    private void SelectImage() {
        Toast.makeText(PostActivity.this, "Select an image from your gallery", Toast.LENGTH_SHORT).show();
        Intent selectIntent = new Intent();
        selectIntent.setType("image/*");
        selectIntent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(
                Intent.createChooser(
                        selectIntent,
                        "Select Image from here..."
                ), PICK_IMAGE_REQUEST
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath.add(data.getData());
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath.get(0));

                uploadImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }

    }

    private void uploadText(String myKey) {
        Timestamp TS = new Timestamp(System.currentTimeMillis());
        ThreadClass threadClassObject = new ThreadClass(MainActivity.USERID, titleText.getText().toString(), contentText.getText().toString(), TS.toString());

        DatabaseReference pushRef = myRef.child(Objects.requireNonNull(myKey));
        pushRef.child("userId").setValue(threadClassObject.getUserId());
        pushRef.child("status").setValue(threadClassObject.getStatus());
        pushRef.child("rating").setValue(threadClassObject.getRating());
        pushRef.child("title").setValue(threadClassObject.getTitle());
        pushRef.child("thread").setValue(threadClassObject.getThread());
        pushRef.child("time").setValue(threadClassObject.getTime());
    }

    private void uploadImage(String myKey) {
        // Code for showing progressDialog while uploading
        ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        // Defining the child of storageReference
        StorageReference ref = storageReference.child(myKey);

        // adding listeners on upload
        // or failure of image
        ref.putFile(filePath.get(0))
                .addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(
                                    UploadTask.TaskSnapshot taskSnapshot) {

                                // Image uploaded successfully
                                // Dismiss dialog
                                progressDialog.dismiss();
                                uploadText(myKey);
                                Toast.makeText(PostActivity.this, "New image post created!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast
                                .makeText(PostActivity.this,
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                        finish();
                    }
                })
                .addOnProgressListener(
                        new OnProgressListener<UploadTask.TaskSnapshot>() {

                            // Progress Listener for loading
                            // percentage on the dialog box
                            @Override
                            public void onProgress(
                                    @NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                double progress
                                        = (100.0
                                        * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage(
                                        "Uploaded "
                                                + (int) progress + "%");
                            }
                        });
    }


}
