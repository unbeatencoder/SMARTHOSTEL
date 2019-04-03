package com.example.smarthostel;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView tvTemperature,tvTommorowPreference;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Spinner spinner_tommorow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        init();
    }

    private void init() {
        tvTemperature= (TextView) findViewById(R.id.tv_temperature);
        tvTommorowPreference=(TextView) findViewById(R.id.tv_tommorowsChoice);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        spinner_tommorow=(Spinner) findViewById(R.id.spinner_tommorow);
        String[] items = new String[]{"6AM-6:30AM", "6:30AM-7AM", "7AM-7:30AM","7:30AM-8AM", "8AM-8:30AM","8:30AM-9AM", "9AM-9:30AM","9:30AM-10AM", "10AM-10:30AM",
                "10:30AM-11AM", "11AM-11:30AM","11:30AM-12PM", "5PM-5:30PM","5:30PM-6PM","6PM-6:30PM", "6:30PM-7PM", "7PM-7:30PM","7:30PM-8PM", "8PM-8:30PM","8:30PM-9PM",
                "9PM-9:30PM","9:30PM-10PM", "10PM-10:30PM", "10:30PM-11PM", "11PM-11:30PM"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        spinner_tommorow.setAdapter(adapter);
        setSupportActionBar(toolbar);
        setNavigationDrawer();
        updatePage();
    }

    public void setNavigationDrawer() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(DashBoard.this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
    }


//------------------------------------------------------------------------------------------//


    private void updatePage() {

        if(!isInternet()) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }
        getUserDetails();
    }

    private void getUserDetails(){
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
               getTemperature(userDetails);
               progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(DashBoard.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTemperature(UserDetails userDetails){
        String userFloor=userDetails.getFloor();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Temperature .....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show(); progressDialog.dismiss();
        FirebaseApp app = FirebaseApp.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DATA").child(userFloor);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TemperatureValue temperatureValue=dataSnapshot.getValue(TemperatureValue.class);
                tvTemperature.setText("Current Temperature:"+temperatureValue.getCurrent());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(DashBoard.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("TOMMOROWPREFERENCES").child(userDetails.getEmailID().replaceAll("[^A-Za-z0-9]", "-"));
        Log.d("wtf",userDetails.getEmailID().replaceAll("[^A-Za-z0-9]", "-"));
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Preference preference=dataSnapshot.getValue(Preference.class);
                tvTommorowPreference.setText("Tommorows Preference:"+preference.getCurrentpreference());
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(DashBoard.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


//------------------------------- Navigation Layout -----------------------------------------//


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.geyser){
            Intent intent=new Intent(this,Geyser.class);
            startActivity(intent);
        }
        else if(id==R.id.signOut){
            signOut();
        }
        else if(id==R.id.Profile){
            Intent intent=new Intent(this,Profile.class);

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Are you sure you want to sign out ?");

        builder.setPositiveButton(" YES ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                FirebaseApp app = FirebaseApp.getInstance();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DashBoard.this,MainActivity.class));
                finish();
            }
        });
        builder.setNegativeButton(" NO ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        builder.create().show();
    }

//------------------------------- Helper Functions ------------------------------------------//


    public boolean isInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            return true;

        return false;
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)==true)
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    public void updateTommorow(View view) {
        String USER= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String key=USER.replaceAll("[^A-Za-z0-9]", "-");
        String choice=spinner_tommorow.getSelectedItem().toString();
        Preference preference=new Preference(choice);
        DatabaseReference databaseReference2= FirebaseDatabase.getInstance().getReference("TOMMOROWPREFERENCES");
        databaseReference2.child(key).setValue(preference).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DashBoard.this,"Successfully Updated", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(DashBoard.this,"Failed"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        init();

    }
}