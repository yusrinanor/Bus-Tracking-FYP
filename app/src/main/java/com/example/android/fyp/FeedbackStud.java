package com.example.android.fyp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FeedbackStud extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private FirebaseAuth mAuth;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBar;
    private Toolbar mToolBar;
    private DatabaseReference mDatabaseReference;
    private Spinner mCategory;
    private EditText mMessage;
    private Button mSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_stud);
        //navigation view
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
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //initialise item
        mCategory = (Spinner) findViewById(R.id.category);
        mMessage = (EditText) findViewById(R.id.message1);
        mSubmit = (Button) findViewById(R.id.submitbutt);

        //submit button
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateText() == true){
                }
                else {
                    return;
                }
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);
                Feedback feedback = new Feedback(mMessage.getText().toString(), formattedDate, mCategory.getSelectedItem().toString());
                mDatabaseReference.child("FeedbackStud").push().setValue(feedback);
                mMessage.setText("");
                mCategory.setSelection(0);
                Toast.makeText(FeedbackStud.this, "Submitted!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean validateText() {
        boolean result = true;
        if(TextUtils.isEmpty(mMessage.getText())){
            result = false;
            Toast.makeText(this, "Message is required to fill in", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        int id = item.getItemId();
        switch (id){
            case R.id.nav_Home:
                return true;
            case R.id.nav_ViewBusSchedule:
                Intent busschedule = new Intent(FeedbackStud.this, ViewBusSchedule.class);
                startActivity(busschedule);
                finish();
                return true;
            case R.id.nav_BusTracking:
                Intent bustracking = new Intent(FeedbackStud.this, RoutePickerStud.class);
                startActivity(bustracking);
                finish();
                return true;
            case R.id.nav_Feedback:
                Intent userFeedback = new Intent(FeedbackStud.this, FeedbackStud.class);
                startActivity(userFeedback);
                finish();
                return true;
            case R.id.nav_Logout:
                FirebaseAuth.getInstance().signOut();
                Intent signoutIntent = new Intent(FeedbackStud.this, MainActivity.class);
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
