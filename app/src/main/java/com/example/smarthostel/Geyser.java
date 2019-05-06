package com.example.smarthostel;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.Calendar;
import java.util.TimeZone;

public class Geyser extends AppCompatActivity {
    Spinner spinner_monday;
    Spinner spinner_tuesday;
    Spinner spinner_wednesday;
    Spinner spinner_thursday;
    Spinner spinner_friday;
    Spinner spinner_saturday;
    Spinner spinner_sunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geyser);
        spinner_monday= findViewById(R.id.spinner_monday);
        spinner_tuesday= findViewById(R.id.spinner_tuesday);
        spinner_wednesday= findViewById(R.id.spinner_wednesday);
        spinner_thursday= findViewById(R.id.spinner_thursday);
        spinner_friday= findViewById(R.id.spinner_friday);
        spinner_saturday= findViewById(R.id.spinner_saturday);
        spinner_sunday= findViewById(R.id.spinner_sunday);

//set the spinners adapter to the previously created one.
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching User Details .....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        FirebaseApp app = FirebaseApp.getInstance();
        String USER = FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[^A-Za-z0-9]", "-");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DEFAULT_PREFERENCES").child(USER);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Preferences preferences1=dataSnapshot.getValue(Preferences.class);
                Log.d("TAG","WTFFF!");
                updateDefaults(preferences1);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(Geyser.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        });


    }
    public void updateDefaults(Preferences preferences){
        //create a list of items for the spinner.
        String[] items1 = new String[]{"DEFAULTS1111","6AM-6:30AM", "6:30AM-7AM", "7AM-7:30AM","7:30AM-8AM", "8AM-8:30AM","8:30AM-9AM", "9AM-9:30AM","9:30AM-10AM", "10AM-10:30AM",
                "10:30AM-11AM", "11AM-11:30AM","11:30AM-12PM", "5PM-5:30PM","5:30PM-6PM","6PM-6:30PM", "6:30PM-7PM", "7PM-7:30PM","7:30PM-8PM", "8PM-8:30PM","8:30PM-9PM",
                "9PM-9:30PM","9:30PM-10PM", "10PM-10:30PM", "10:30PM-11PM", "11PM-11:30PM"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.

        items1[0]= preferences.getMonday();
        Log.d("LLL",items1[0]);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        spinner_monday.setAdapter(adapter1);
        String[] items2 = new String[]{"DEFAULTS1111","6AM-6:30AM", "6:30AM-7AM", "7AM-7:30AM","7:30AM-8AM", "8AM-8:30AM","8:30AM-9AM", "9AM-9:30AM","9:30AM-10AM", "10AM-10:30AM",
                "10:30AM-11AM", "11AM-11:30AM","11:30AM-12PM", "5PM-5:30PM","5:30PM-6PM","6PM-6:30PM", "6:30PM-7PM", "7PM-7:30PM","7:30PM-8PM", "8PM-8:30PM","8:30PM-9PM",
                "9PM-9:30PM","9:30PM-10PM", "10PM-10:30PM", "10:30PM-11PM", "11PM-11:30PM"};
        items2[0]=preferences.getTuesday();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        spinner_tuesday.setAdapter(adapter2);
        String[] items3 = new String[]{"DEFAULTS1111","6AM-6:30AM", "6:30AM-7AM", "7AM-7:30AM","7:30AM-8AM", "8AM-8:30AM","8:30AM-9AM", "9AM-9:30AM","9:30AM-10AM", "10AM-10:30AM",
                "10:30AM-11AM", "11AM-11:30AM","11:30AM-12PM", "5PM-5:30PM","5:30PM-6PM","6PM-6:30PM", "6:30PM-7PM", "7PM-7:30PM","7:30PM-8PM", "8PM-8:30PM","8:30PM-9PM",
                "9PM-9:30PM","9:30PM-10PM", "10PM-10:30PM", "10:30PM-11PM", "11PM-11:30PM"};
        items3[0]=preferences.getWednesday();
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items3);
        spinner_wednesday.setAdapter(adapter3);
        String[] items4 = new String[]{"DEFAULTS1111","6AM-6:30AM", "6:30AM-7AM", "7AM-7:30AM","7:30AM-8AM", "8AM-8:30AM","8:30AM-9AM", "9AM-9:30AM","9:30AM-10AM", "10AM-10:30AM",
                "10:30AM-11AM", "11AM-11:30AM","11:30AM-12PM", "5PM-5:30PM","5:30PM-6PM","6PM-6:30PM", "6:30PM-7PM", "7PM-7:30PM","7:30PM-8PM", "8PM-8:30PM","8:30PM-9PM",
                "9PM-9:30PM","9:30PM-10PM", "10PM-10:30PM", "10:30PM-11PM", "11PM-11:30PM"};
        items4[0]=preferences.getThursday();
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items4);
        spinner_thursday.setAdapter(adapter4);
        String[] items5 = new String[]{"DEFAULTS1111","6AM-6:30AM", "6:30AM-7AM", "7AM-7:30AM","7:30AM-8AM", "8AM-8:30AM","8:30AM-9AM", "9AM-9:30AM","9:30AM-10AM", "10AM-10:30AM",
                "10:30AM-11AM", "11AM-11:30AM","11:30AM-12PM", "5PM-5:30PM","5:30PM-6PM","6PM-6:30PM", "6:30PM-7PM", "7PM-7:30PM","7:30PM-8PM", "8PM-8:30PM","8:30PM-9PM",
                "9PM-9:30PM","9:30PM-10PM", "10PM-10:30PM", "10:30PM-11PM", "11PM-11:30PM"};
        items5[0]=preferences.getFriday();
        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items5);
        spinner_friday.setAdapter(adapter5);
        String[] items6 = new String[]{"DEFAULTS1111","6AM-6:30AM", "6:30AM-7AM", "7AM-7:30AM","7:30AM-8AM", "8AM-8:30AM","8:30AM-9AM", "9AM-9:30AM","9:30AM-10AM", "10AM-10:30AM",
                "10:30AM-11AM", "11AM-11:30AM","11:30AM-12PM", "5PM-5:30PM","5:30PM-6PM","6PM-6:30PM", "6:30PM-7PM", "7PM-7:30PM","7:30PM-8PM", "8PM-8:30PM","8:30PM-9PM",
                "9PM-9:30PM","9:30PM-10PM", "10PM-10:30PM", "10:30PM-11PM", "11PM-11:30PM"};
        items6[0]=preferences.getSaturday();
        ArrayAdapter<String> adapter6 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items6);
        spinner_saturday.setAdapter(adapter6);
        String[] items7 = new String[]{"DEFAULTS1111","6AM-6:30AM", "6:30AM-7AM", "7AM-7:30AM","7:30AM-8AM", "8AM-8:30AM","8:30AM-9AM", "9AM-9:30AM","9:30AM-10AM", "10AM-10:30AM",
                "10:30AM-11AM", "11AM-11:30AM","11:30AM-12PM", "5PM-5:30PM","5:30PM-6PM","6PM-6:30PM", "6:30PM-7PM", "7PM-7:30PM","7:30PM-8PM", "8PM-8:30PM","8:30PM-9PM",
                "9PM-9:30PM","9:30PM-10PM", "10PM-10:30PM", "10:30PM-11PM", "11PM-11:30PM"};
        items7[0]=preferences.getSunday();
        ArrayAdapter<String> adapter7 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items7);
        spinner_sunday.setAdapter(adapter7);

    }
    public void setChoices(View view) {
        final String choiceMonday;
        final String choiceTuesday;
        final String choiceWednesday ;
        final String choiceThursday ;
        final String choiceFriday;
        final String choiceSaturday ;
        final String choiceSunday;
        choiceMonday=spinner_monday.getSelectedItem().toString();
        choiceTuesday=spinner_tuesday.getSelectedItem().toString();
        choiceWednesday=spinner_wednesday.getSelectedItem().toString();
        choiceThursday=spinner_thursday.getSelectedItem().toString();
        choiceFriday=spinner_friday.getSelectedItem().toString();
        choiceSaturday=spinner_saturday.getSelectedItem().toString();
        choiceSunday=spinner_sunday.getSelectedItem().toString();
        String USER= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String key=USER.replaceAll("[^A-Za-z0-9]", "-");
        Preferences preferences=new Preferences(choiceMonday,choiceTuesday,choiceWednesday,choiceThursday,choiceFriday,choiceSaturday,choiceSunday);
        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference("DEFAULT_PREFERENCES");
        databaseReference1.child(key).setValue(preferences).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Geyser.this,"Successfully Updated", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Geyser.this,"Failed"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        Preference preference=new Preference();
        Calendar calendar=Calendar.getInstance(TimeZone.getDefault());
        int DAYOFWEEK=calendar.get(Calendar.DAY_OF_WEEK);
        Log.d("StringValue",""+DAYOFWEEK);
        switch (DAYOFWEEK){
            case 1:
                preference.setCurrentpreference(choiceTuesday);
            case 2:
                preference.setCurrentpreference(choiceWednesday);
            case 3:
                preference.setCurrentpreference(choiceThursday);
            case 4:
                preference.setCurrentpreference(choiceFriday);
            case 5:
                preference.setCurrentpreference(choiceSaturday);
            case 6:
                preference.setCurrentpreference(choiceSunday);
            case 7:
                preference.setCurrentpreference(choiceMonday);
        }
        DatabaseReference databaseReference2= FirebaseDatabase.getInstance().getReference("TOMMOROWPREFERENCES");
        databaseReference2.child(key).setValue(preference).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Geyser.this,"Successfully Updated", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Geyser.this,"Failed"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
