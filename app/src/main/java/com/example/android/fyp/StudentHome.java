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
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StudentHome extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBar;
    private Toolbar mToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);


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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        int id = item.getItemId();
        switch (id){
            case R.id.nav_Home:
                return true;
            case R.id.nav_ViewBusSchedule:
                Intent busSchedule = new Intent(StudentHome.this, ViewBusSchedule.class);
                startActivity(busSchedule);
                finish();
                return true;
            case R.id.nav_BusTracking:
                Intent bustracking = new Intent(StudentHome.this, RoutePickerStud.class);
                startActivity(bustracking);
                finish();
                return true;
            case R.id.nav_Feedback:
                Intent userFeedback = new Intent(StudentHome.this, FeedbackStud.class);
                startActivity(userFeedback);
                finish();
                return true;
            case R.id.nav_Logout:
                FirebaseAuth.getInstance().signOut();
                Intent signoutIntent = new Intent(StudentHome.this, MainActivity.class);
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
