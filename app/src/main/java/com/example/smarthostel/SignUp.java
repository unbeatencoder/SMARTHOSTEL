package com.example.smarthostel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private EditText et_emailID,et_password,et_studentID,et_floor;
    private Button btn_signIn,btn_signUp;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        et_emailID=(EditText) findViewById(R.id.et_emailID);
        et_password=(EditText) findViewById(R.id.et_password);
        et_studentID=(EditText)findViewById(R.id.et_studentID);
        et_floor=(EditText)findViewById(R.id.et_floor);
        btn_signIn=(Button)findViewById(R.id.btn_signIn);
        btn_signUp=(Button)findViewById(R.id.btn_signUp);
        mAuth=FirebaseAuth.getInstance();
    }

    public void signIn(View view) {
        Intent intent= new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void signUp(View view) {
         final String email,password,studentID,floor;
         email=et_emailID.getText().toString();
         password=et_password.getText().toString();
         studentID=et_studentID.getText().toString();
         floor=et_floor.getText().toString();
         if(email.isEmpty()||password.isEmpty()||studentID.isEmpty()||floor.isEmpty()){
             Toast.makeText(this,"All Details are necessary",Toast.LENGTH_SHORT).show();
             return;
         }
         mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if(task.isSuccessful()){
                    uploadDetails(email,studentID,floor);
                 }
                 else{
                     Toast.makeText(SignUp.this, "error"+task.getException(), Toast.LENGTH_SHORT).show();
                 }
             }
         });

    }
    public void uploadDetails(String email,String studentID,String floor){
        UserDetails user=new UserDetails(email,studentID,floor);
        String key=user.getEmailID().replaceAll("[^A-Za-z0-9]", "-");
        FirebaseApp app=FirebaseApp.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("USERS");
        databaseReference.child(key).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUp.this,"Successfully Created", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(SignUp.this,"Failed"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        Preferences preferences=new Preferences("6AM:6:30AM","6AM-6:30AM","6AM-6:30AM","6AM-6:30AM","6AM-6:30AM","6AM-6:30AM","6AM-6:30AM");
        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference("DEFAULT_PREFERENCES");
        databaseReference1.child(key).setValue(preferences).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUp.this,"Successfully Created", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(SignUp.this,"Failed"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        Preference preference=new Preference("6AM-6:30AM");
        DatabaseReference databaseReference2= FirebaseDatabase.getInstance().getReference("TOMMOROWPREFERENCES");
        databaseReference2.child(key).setValue(preference).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUp.this,"Successfully Created", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(SignUp.this,"Failed"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        DatabaseReference databaseReference3= FirebaseDatabase.getInstance().getReference("FLOORMEMBERS").child(user.getFloor());
        databaseReference3.child(key).setValue(key).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUp.this,"Successfully Created", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(SignUp.this,"Failed"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
