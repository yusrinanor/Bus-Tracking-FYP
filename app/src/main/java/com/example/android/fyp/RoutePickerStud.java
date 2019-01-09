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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RoutePickerStud extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBar;
    private Toolbar mToolBar;
    private Spinner mCategory;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_picker_stud);

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

        //select item from spinner

        mCategory = (Spinner) findViewById(R.id.category);
        mButton = (Button) findViewById(R.id.TrackBus);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected_item = mCategory.getSelectedItem().toString().split("-")[0];
                Intent map_intent = new Intent(RoutePickerStud.this, StudentMapsActivity.class);
                map_intent.putExtra("location", selected_item);
                startActivity(map_intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        int id = item.getItemId();
        switch (id){
            case R.id.nav_Home:
                Intent home = new Intent(RoutePickerStud.this, StudentHome.class);
                startActivity(home);
                finish();
                return true;
            case R.id.nav_ViewBusSchedule:
                Intent busschedule = new Intent(RoutePickerStud.this, ViewBusSchedule.class);
                startActivity(busschedule);
                finish();
                return true;
            case R.id.nav_BusTracking:
                return true;
            case R.id.nav_Feedback:
                Intent userFeedback = new Intent(RoutePickerStud.this, FeedbackStud.class);
                startActivity(userFeedback);
                finish();
                return true;
            case R.id.nav_Logout:
                FirebaseAuth.getInstance().signOut();
                Intent signoutIntent = new Intent(RoutePickerStud.this, MainActivity.class);
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
