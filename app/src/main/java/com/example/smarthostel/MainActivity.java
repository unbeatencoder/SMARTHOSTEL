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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText et_emailID,et_password;
    private Button btn_signIn,btn_signUp;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_emailID=(EditText) findViewById(R.id.et_emailID);
        et_password=(EditText) findViewById(R.id.et_password);
        btn_signIn=(Button)findViewById(R.id.btn_signIn);
        btn_signUp=(Button)findViewById(R.id.btn_signUp);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        if(firebaseUser!=null){
            UpdateUI();
        }
    }

    private void UpdateUI() {
        Intent intent=new Intent(this,DashBoard.class);
        startActivity(intent);
        finish();
    }

    public void signIn(View view) {
        String email = et_emailID.getText().toString();
        String password = et_password.getText().toString();

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All Fields Are Mandatory", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    UpdateUI();
                }
                else{
                    Toast.makeText(MainActivity.this, "Failed"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void signUp(View view){
        Intent intent=new Intent(this,SignUp.class);
        startActivity(intent);
    }
}
