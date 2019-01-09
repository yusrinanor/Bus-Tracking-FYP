package com.example.android.fyp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Ad_AddBusSchedule extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBar;
    private Toolbar mToolBar;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private Spinner mFromLocation;
    private Spinner mToLocation;
    private TimePicker mTiming;
    private Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad__add_bus_schedule);

        //drawer layout
        mToolBar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolBar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mActionBar = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mActionBar);
        mActionBar.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.menuNavigationView);

        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }

        mAuth = FirebaseAuth.getInstance();
        NavigationView navigationView = (NavigationView) findViewById(R.id.menuNavigationView);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.head_title_user);
        FirebaseUser curruser = mAuth.getCurrentUser();
        nav_user.setText("Welcome, "+curruser.getEmail().split("@")[0]);

        //firebase database
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Schedules");

        //initialise item
        mFromLocation = (Spinner) findViewById(R.id.FromLocation);
        mToLocation = (Spinner) findViewById(R.id.ToLocation);
        mTiming = (TimePicker) findViewById(R.id.BusTiming);
        mSubmit = (Button) findViewById(R.id.submitbutt);

        //submit button
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String location = mFromLocation.getSelectedItem().toString().toLowerCase().substring(0,1).toUpperCase()
                        + mFromLocation.getSelectedItem().toString().toLowerCase().substring(1)
                        + "-to-" +
                        mToLocation.getSelectedItem().toString().toLowerCase();
                if(mTiming.getCurrentHour() <10) {
                    mDatabaseReference.child(location).child("0"+String.valueOf(mTiming.getCurrentHour())+":"+String.valueOf(mTiming.getCurrentMinute())).setValue("true");
                }
                else {
                    mDatabaseReference.child(location).child(String.valueOf(mTiming.getCurrentHour())+":"+String.valueOf(mTiming.getCurrentMinute())).setValue("true");
                }

                mFromLocation.setSelection(0);
                mToLocation.setSelection(0);
                Toast.makeText(Ad_AddBusSchedule.this, "Submitted!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        int id = item.getItemId();
        switch (id){
            case R.id.nav_Home:
                Intent AdminHome = new Intent(Ad_AddBusSchedule.this, AdminHome.class);
                startActivity(AdminHome);
                finish();
                return true;
            case R.id.nav_AddBusSchedule:
                return true;
            case R.id.nav_EditSchedule:
                Intent editschedule = new Intent(Ad_AddBusSchedule.this, Ad_EditBusSchedule.class);
                startActivity(editschedule);
                finish();
                return true;
            case R.id.nav_BusReport:
                return true;
            case R.id.nav_ViewFeedback:
                Intent feedback = new Intent(Ad_AddBusSchedule.this, ViewFeedback.class);
                startActivity(feedback);
                finish();
                return true;
            case R.id.nav_Logout:
                FirebaseAuth.getInstance().signOut();
                Intent signoutIntent = new Intent(Ad_AddBusSchedule.this, MainActivity.class);
                startActivity(signoutIntent);
                finish();
                return true;
            default:
                return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(mActionBar.onOptionsItemSelected(item)){

            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}




