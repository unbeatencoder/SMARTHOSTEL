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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView tvTemperature;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        init();
    }

    private void init() {
        tvTemperature= (TextView) findViewById(R.id.tv_temperature);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        getTemperature();
    }

    private void getTemperature(){
//        final ArrayList<String> allSubscribedNgoDetails = new ArrayList<>();
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Fetching Subscribed NGO Details .....");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//        FirebaseApp userApp = FirebaseApp.getInstance("userApp");
//        String USER = FirebaseAuth.getInstance(userApp).getCurrentUser().getEmail().replaceAll("[^A-Za-z0-9]", "-");
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance(userApp).getReference("SubscribeDetails").child(USER);
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot ngoSnapshot : dataSnapshot.getChildren()) {
//                    String temp = ngoSnapshot.getValue(String.class);
//                    allSubscribedNgoDetails.add(temp);
//                }
//                progressDialog.dismiss();
//                getAllSubscribedNgoEvents(allSubscribedNgoDetails);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                progressDialog.dismiss();
//                Toast.makeText(UserDashboard.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
        tvTemperature.setText("100dcelcius");
    }

    private void getAllSubscribedNgoEvents(final ArrayList<String> allSubscribedNgoDetails) {

//        if(allSubscribedNgoDetails.size()==0) {
//            recyclerView.setVisibility(View.GONE);
//            tvNotice.setVisibility(View.VISIBLE);
//            tvNotice.setText("You Have Not Subscribe Any NGO !!!");
//            return;
//        }
//
//        final ArrayList<NgoEvent> allSubscribedNgoEvents = new ArrayList<>();
//
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Fetching Subscribed NGO Events .....");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//
//        FirebaseApp ngoApp = FirebaseApp.getInstance("ngoApp");
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance(ngoApp).getReference("NGOEvents");
//
//        final int[] cnt = {0};
//        for(int i=0;i<allSubscribedNgoDetails.size();i++) {
//
//            String key = allSubscribedNgoDetails.get(i).replaceAll("[^A-Za-z0-9]", "-");
//            DatabaseReference tempDatabaseReference = databaseReference.child(key);
//            tempDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for(DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
//                        NgoEvent temp = eventSnapshot.getValue(NgoEvent.class);
//                        allSubscribedNgoEvents.add(temp);
//                    }
//                    cnt[0]++;
//
//                    if(cnt[0] == allSubscribedNgoDetails.size()) {
//                        progressDialog.dismiss();
//                        setRecyclerView(allSubscribedNgoEvents);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    cnt[0]++;
//
//                    if(cnt[0] == allSubscribedNgoDetails.size()) {
//                        progressDialog.dismiss();
//                        Toast.makeText(UserDashboard.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
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

                FirebaseApp userApp = FirebaseApp.getInstance("userApp");
                FirebaseAuth.getInstance(userApp).signOut();
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
}