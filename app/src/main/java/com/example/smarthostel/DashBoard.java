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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextClock;
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

    TextView tvTemperature,tvValveStatus,tvGeyserStatus,tvSlotStatus,tvTiming;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Spinner spinner_tommorow;
    Button btnSlot,btnturnON,btnOFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        init();
    }


    private void init() {
        tvTemperature= (TextView) findViewById(R.id.tv_temperature);
        tvSlotStatus=(TextView) findViewById(R.id.tv_slot_status);
        tvGeyserStatus=(TextView)findViewById(R.id.tv_geyser_status);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        spinner_tommorow=(Spinner) findViewById(R.id.spinner_tommorow);
        tvValveStatus=(TextView) findViewById(R.id.tv_valve_status);
        btnSlot=(Button) findViewById(R.id.btn_book);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        tvTiming=(TextView) findViewById(R.id.tv_timing);
        btnturnON=(Button) findViewById(R.id.btn_turnon);
        btnOFF=(Button) findViewById(R.id.btn_turnoff);
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
        btnSlot.setEnabled(true);
        btnturnON.setEnabled(false);
        btnOFF.setEnabled(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        FirebaseApp app = FirebaseApp.getInstance();
        String USER = FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[^A-Za-z0-9]", "-");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("USERS").child(USER);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails userDetails=dataSnapshot.getValue(UserDetails.class);
                getStatusofGeyser(userDetails);
                getTemperature(userDetails);
                getTommorowsTime(userDetails);
                getStatusofValve(userDetails);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(DashBoard.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTommorowsTime(UserDetails userDetails) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("schedule").child(userDetails.getFloor());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer value=dataSnapshot.getValue(Integer.class);
                String fin="";
                switch (value){
                    case 0:
                        fin="12AM-2AM";
                        break;
                    case 1:
                        fin="1AM-3AM";
                        break;
                    case 2:
                        fin="2AM-4AM";
                        break;
                    case 3:
                        fin="3AM-5AM";
                        break;
                    case 4:
                        fin="4AM-6AM";
                        break;
                    case 5:
                        fin="5AM-7AM";
                        break;
                    case 6:
                        fin="6AM-8AM";
                        break;
                    case 7:
                        fin="7AM-9AM";
                        break;
                    case 8:
                        fin="8AM-10AM";
                        break;
                    case 9:
                        fin="9AM-11AM";
                        break;
                    case 10:
                        fin="10AM-12PM";
                        break;
                    case 11:
                        fin="11AM-1PM";
                        break;
                    case 12:
                        fin="12PM-2PM";
                        break;
                    case 13:
                        fin="1PM-3PM";
                        break;
                    case 14:
                        fin="2PM-4PM";
                        break;
                    case 15:
                        fin="3PM-5PM";
                        break;
                    case 16:
                        fin="4PM-6PM";
                        break;
                    case 17:
                        fin="5PM-7PM";
                        break;
                    case 18:
                        fin="6PM-8PM";
                        break;
                    case 19:
                        fin="7PM-9PM";
                        break;
                    case 20:
                        fin="8PM-10PM";
                        break;
                    case 21:
                        fin="9PM-11PM";
                        break;
                    case 22:
                        fin="10PM-12AM";
                        break;
                    case 23:
                        fin="11PM-1AM";
                        break;

                }
                tvTiming.setText("Today's timing "+fin);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DashBoard.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getStatusofGeyser(UserDetails userDetails) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Current Status of Geyser.....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        FirebaseApp app = FirebaseApp.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("GEYSER_STATUS").child(userDetails.getFloor());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status_geyser=dataSnapshot.getValue(String.class);
                tvGeyserStatus.setText("Current Status of Geyser : "+status_geyser);
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
                tvTemperature.setText("Current Temperature : "+temperatureValue.getCurrent()+"\u00B0C");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(DashBoard.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("FLOORMEMBERS").child(userDetails.getFloor()).child(userDetails.getEmailID().replaceAll("[^A-Za-z0-9]", "-"));
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Effective effective=dataSnapshot.getValue(Effective.class);
               updateScroller(effective.getPreference());
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(DashBoard.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getStatusofValve(final UserDetails userDetails) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Current Status of Valve .....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        FirebaseApp app = FirebaseApp.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PRIORITY_VAL_STATUS").child(userDetails.getFloor());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status_valve=dataSnapshot.getValue(String.class);
                tvValveStatus.setText("Current Status of Valve : "+status_valve);
                getStatusOfSlotUsed(userDetails);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(DashBoard.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getStatusOfSlotUsed(final UserDetails userDetails){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Current Status of Valve .....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        FirebaseApp app = FirebaseApp.getInstance();
        String key=userDetails.getEmailID().replaceAll("[^A-Za-z0-9]", "-");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SLOTUSED").child(key);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String slotUsed=dataSnapshot.getValue(String.class);
                if(slotUsed.compareTo("0")==0){
                    tvSlotStatus.setText("You have already used your slot for today");
                    btnSlot.setEnabled(false);
                    progressDialog.dismiss();
                    return ;
                }
                else{
                    tvSlotStatus.setText("You have 1 slot available for 5 min");
                }
                setSlots(userDetails);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(DashBoard.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setSlots(final UserDetails userDetails) {
        String status_valve=tvValveStatus.getText().toString();
        String status_geyser=tvGeyserStatus.getText().toString();
        if(status_geyser.compareTo("Current Status of Geyser : OFF")==0){
            tvSlotStatus.setText("Geyser is off! Please Check the Timings");
            btnSlot.setEnabled(false);
            btnturnON.setEnabled(false);
            btnOFF.setEnabled(false);
            return;
        }
        else if(status_valve.compareTo("Current Status of Valve : ON")==0){
            tvSlotStatus.setText("Valve is On! Please Try Again Later");
            btnSlot.setEnabled(false);
            btnturnON.setEnabled(false);
            btnOFF.setEnabled(false);
        }
    }
    private  void bookSlots1(final UserDetails userDetails){
        FirebaseApp app = FirebaseApp.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("GEYSER_STATUS").child(userDetails.getFloor());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status_geyser=dataSnapshot.getValue(String.class);
                if(status_geyser.compareTo("OFF")==0){
                    Toast.makeText(DashBoard.this, "Unable to Book Slot, Geyser was OFF", Toast.LENGTH_SHORT).show();
                    init();
                    return ;
                }
                finalValidations(userDetails);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DashBoard.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void finalValidations(final UserDetails userDetails){

        FirebaseApp app = FirebaseApp.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PRIORITY_VAL_STATUS").child(userDetails.getFloor());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status_valve=dataSnapshot.getValue(String.class);
                if(status_valve.compareTo("ON")==0){
                    Toast.makeText(DashBoard.this, "Someone else is using the facility", Toast.LENGTH_SHORT).show();
                    init();
                    return ;
                }
                bookSlotFinal(userDetails);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DashBoard.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void bookSlotFinal(final UserDetails userDetails) {
        String key=userDetails.getFloor();
        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference("PRIORITY_VAL_STATUS");
        databaseReference1.child(key).setValue("ON").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DashBoard.this,"Successfully Turned On Valve", Toast.LENGTH_SHORT).show();
                    btnSlot.setEnabled(false);
                    btnturnON.setEnabled(true);
                    btnOFF.setEnabled(true);
                    tvSlotStatus.setText("You have 5 minutes from now");
                    removeSlot(userDetails);
                }
                else{
                    Toast.makeText(DashBoard.this,"Failed"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void turnONVALVE(UserDetails userDetails) {
        String key=userDetails.getFloor();
        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference("VAL_STATUS");
        databaseReference1.child(key).setValue("ON").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DashBoard.this,"Successfully Turned On Valve", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(DashBoard.this,"Failed"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void removeSlot(UserDetails userDetails) {
        String key=userDetails.getEmailID().replaceAll("[^A-Za-z0-9]", "-");
        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference("SLOTUSED");
        databaseReference1.child(key).setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DashBoard.this,"Successfully Booked Slot1", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(DashBoard.this,"Failed"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void bookSlot(View view) {
        getUserDetails2();
    }
    private void getUserDetails2() {
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
                bookSlots1(userDetails);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(DashBoard.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void updateScroller(Preference preference) {
        String[] items = new String[]{"DEFAULTS","6AM-6:30AM", "6:30AM-7AM", "7AM-7:30AM","7:30AM-8AM", "8AM-8:30AM","8:30AM-9AM", "9AM-9:30AM","9:30AM-10AM", "10AM-10:30AM",
                "10:30AM-11AM", "11AM-11:30AM","11:30AM-12PM", "5PM-5:30PM","5:30PM-6PM","6PM-6:30PM", "6:30PM-7PM", "7PM-7:30PM","7:30PM-8PM", "8PM-8:30PM","8:30PM-9PM",
                "9PM-9:30PM","9:30PM-10PM", "10PM-10:30PM", "10:30PM-11PM", "11PM-11:30PM"};
        items[0]=preference.getCurrentpreference();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner_tommorow.setAdapter(adapter);
    }

    public void updateTommorow(UserDetails userDetails) {
        String USER= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String key=USER.replaceAll("[^A-Za-z0-9]", "-");
        String choice=spinner_tommorow.getSelectedItem().toString();
        Preference preference=new Preference(choice);
        DatabaseReference databaseReference3= FirebaseDatabase.getInstance().getReference("FLOORMEMBERS").child(userDetails.getFloor()).child(key);
        databaseReference3.child("preference").setValue(preference).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    public void updateTommorow1(View view) {
        getUserDetails1();
        init();
    }
    private void getUserDetails1() {
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
                updateTommorow(userDetails);
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
            startActivity(intent);
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
//--------------------------------- Option Menu ----------------------------------------------//

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_dash_board_option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.Refresh) {
            updatePage();
        }

        return super.onOptionsItemSelected(item);
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

    public void turnONTAP1(final UserDetails userDetails){
        String key=userDetails.getFloor();
        FirebaseApp app=FirebaseApp.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("PRIORITY_VAL_STATUS");
        databaseReference.child(key).setValue("ON").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DashBoard.this,"Successfully Booked Slot", Toast.LENGTH_SHORT).show();
                    btnturnON.setEnabled(false);
                    turnONVALVE(userDetails);
                    tvSlotStatus.setText("You have 5 minutes from now");
                }
                else{
                    Toast.makeText(DashBoard.this,"Failed"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void turnONTAP(View view) {
        getUserDetails3();
    }

    private void getUserDetails3() {
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
                checkPriority(userDetails);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(DashBoard.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkPriority(final UserDetails userDetails) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching User Details .....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PRIORITY_VAL_STATUS").child(userDetails.getFloor());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String string=dataSnapshot.getValue(String.class);
                if(string.compareTo("OFF")==0){
                    Toast.makeText(DashBoard.this, "You have crossed 5 min limit", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    init();
                    return;
                }
                turnONTAP1(userDetails);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(DashBoard.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void turnOFFTAP1(UserDetails userDetails){
        String key=userDetails.getFloor();
        FirebaseApp app=FirebaseApp.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("PRIORITY_VAL_STATUS");
        databaseReference.child(key).setValue("OFF").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DashBoard.this,"Successfully Stopped VALVE", Toast.LENGTH_SHORT).show();
                    btnOFF.setEnabled(false);
                    tvSlotStatus.setText("You have 5 minutes from now");
                }
                else{
                    Toast.makeText(DashBoard.this,"Failed"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference("VAL_STATUS");
        databaseReference1.child(key).setValue("OFF").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DashBoard.this,"Successfully Stopped VALVE", Toast.LENGTH_SHORT).show();
                    btnOFF.setEnabled(false);
                    tvSlotStatus.setText("You have 5 minutes from now");
                }
                else{
                    Toast.makeText(DashBoard.this,"Failed"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void turnOFFTAP(View view) {
        getUserDetails4();

    }

    private void getUserDetails4() {
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
                turnOFFTAP1(userDetails);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(DashBoard.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
