//package com.example.firebaseapp.thread.models;
//
//import android.util.Log;
//import android.widget.RadioGroup;
//
//import androidx.annotation.NonNull;
//
//import com.example.firebaseapp.R;
//import com.example.firebaseapp.thread.MainActivity;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.Objects;
//
//public class MatchForm<education, gender, time, learning> {
////    FirebaseDatabase database = FirebaseDatabase.getInstance("https://android-firebase-9538d-default-rtdb.asia-southeast1.firebasedatabase.app");
////    DatabaseReference usersRef = database.getReference("UserGroups");
//
//    RadioGroup education;
//    RadioGroup gender;
//    RadioGroup time;
//    RadioGroup learning;
//
//    public MatchForm( RadioGroup education, RadioGroup gender, RadioGroup time, RadioGroup learning) {
//        this.education =  education;
//        this.gender = gender;
//        this.time = time;
//        this.learning = learning;
//    }
//
//    public void setEducation(RadioGroup education) {
//        this.education = education;
//    }
//
//    public void setGender(RadioGroup gender) {
//        this.gender = gender;
//    }
//
//    public void setTime(RadioGroup time) {
//        this.time = time;
//    }
//
//    public void setLearning(RadioGroup learning) {
//        this.learning = learning;
//    }
//
//    public RadioGroup getEducation() {
//        return this.education;
//    }
//
//    public RadioGroup getGender() {
//        return this.gender;
//    }
//
//    public RadioGroup getTime() {
//        return this.time;
//    }
//
//    public RadioGroup getLearning() {
//        return this.learning;
//    }
//
//////    @Override
////    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////        boolean exist = false;
////        for (DataSnapshot value : dataSnapshot.getChildren()) {
////            // Toast.makeText(LoginActivity.this, "Account Exist", Toast.LENGTH_SHORT).show();
////            exist = true;
////            DatabaseReference pushRef = usersRef.child(Objects.requireNonNull(usersRef.push().getKey()));
////            pushRef.child("education").setValue(MainActivity.USERID);
////            pushRef.child("gender").setValue(MainActivity.USERID);
////            pushRef.child("time").setValue(MainActivity.USERID);
////            pushRef.child("learning").setValue(MainActivity.USERID);
//    FirebaseDatabase database = FirebaseDatabase.getInstance("https://android-firebase-9538d-default-rtdb.asia-southeast1.firebasedatabase.app");
//    DatabaseReference usersRef = database.getReference("UserGroups");
//    usersRef.addListenerForSingleValueEvent(new void ValueEventListener() {
//        education = education.findViewById(R.id.education);
//        gender = gender.findViewById(R.id.gender);
//        time = time.findViewById(R.id.time);
//        learning = learning.findViewById(R.id.learning);
//
//        )
//    }
//        @Override
//        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//            for (DataSnapshot value : dataSnapshot.getChildren()) {
//                if (Objects.equals(value.child("userID").getValue(), MainActivity.USERID)) {
//                    usersRef.child(Objects.requireNonNull(value.getKey())).child("Education").setValue("education");
//                    usersRef.child(Objects.requireNonNull(value.getKey())).child("Gender").setValue("gender");
//                    usersRef.child(Objects.requireNonNull(value.getKey())).child("Time").setValue("time");
//                    usersRef.child(Objects.requireNonNull(value.getKey())).child("Learning").setValue("learning");
//
//                    break;
//                }
//            }
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//            // Failed to read value
//            Log.w("Button", "Failed to read value.", error.toException());
//        }
//    });
//
//
//
//                if (Objects.equals(value.child("userPwd").getValue(), pwd.getText().toString())) {
//                    MainActivity.loginStatus = "Y";
//                    MainActivity.USERID = IDeditText.getText().toString();
//                    Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
//
//                    finish();
//                    // login以后自动去Profile界面。这里还是要override transition，不然动画不对（因为是新创建一个activity去ProfileActivity界面）
//                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
//                    overridePendingTransition(0,0);
//            }
//            }
//};
//
//
//
//    @Override
//    public void onCancelled(@NonNull DatabaseError error) {
//        // Failed to read value
//        Log.w("Button", "Failed to read value.", error.toException());
//    }
//});
//}
