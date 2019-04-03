package com.example.smarthostel;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    TextView tvstudentID,tvuseremail,tvuserfloor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }

    private void init() {
        tvstudentID=(TextView)findViewById(R.id.tv_user_studentID);
        tvuseremail=(TextView)findViewById(R.id.tv_user_email);
        tvuserfloor=(TextView)findViewById(R.id.tv_user_floor);
        updateDATA();
    }

    private void updateDATA() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching User Details .....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        FirebaseApp app = FirebaseApp.getInstance();
        String USER = FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[^A-Za-z0-9]", "-");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("USERS").child(USER);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails userDetails=dataSnapshot.getValue(UserDetails.class);
                tvstudentID.setText(userDetails.getStudentID());
                tvuseremail.setText(userDetails.getEmailID());
                tvuserfloor.setText(userDetails.getFloor());
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(Profile.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
