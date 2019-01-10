package com.example.android.fyp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewFeedback extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView mUserType;
    private RecyclerView mRecylerFeed;
    private FloatingActionButton mSwitch;
    private FirebaseDatabase mFB;
    private DatabaseReference mDR;
    public RecyclerView.LayoutManager layoutManager;
    private String userSwitch = "";
    private FirebaseAuth mAuth;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBar;
    private Toolbar mToolBar;

    FirebaseRecyclerAdapter<Feedback, FeedbackViewHolder> adapterStud;
    FirebaseRecyclerAdapter<Feedback, FeedbackViewHolder> adapterDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);
        //drawer layout
        mToolBar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolBar);
        userSwitch = "FeedbackStud";
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


        //display data in cardview
        mUserType = (TextView) findViewById(R.id.UserType);
        mRecylerFeed = (RecyclerView) findViewById(R.id.recycler_feedback);
        mSwitch = (FloatingActionButton) findViewById(R.id.switchbutt);
        mFB = FirebaseDatabase.getInstance();
        mDR = mFB.getReference();
        mRecylerFeed.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecylerFeed.setLayoutManager(layoutManager);


        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userSwitch == "FeedbackStud"){
                    userSwitch = "FeedbackDriver";
                    mRecylerFeed.swapAdapter(adapterDriver, false);
                    mUserType.setText("Driver");
                } else {
                    userSwitch = "FeedbackStud";
                    mRecylerFeed.swapAdapter(adapterStud, false);
                    mUserType.setText("Student");

                }
            }
        });
        adapterStud = new FirebaseRecyclerAdapter<Feedback, FeedbackViewHolder>(
                Feedback.class,
                R.layout.card_feedback,
                FeedbackViewHolder.class,
                mDR.child("FeedbackStud")
        ) {
            @Override
            protected void populateViewHolder(FeedbackViewHolder viewHolder, Feedback model, int position) {
                viewHolder.txtFeedbackID.setText(adapterStud.getRef(position).getKey());
                viewHolder.txtFeedbackDate.setText(model.getDate());
                viewHolder.txtCategory.setText(model.getCategory());
                viewHolder.txtMessage.setText(model.getMessage());
            }
        };
        adapterDriver = new FirebaseRecyclerAdapter<Feedback, FeedbackViewHolder>(
                Feedback.class,
                R.layout.card_feedback,
                FeedbackViewHolder.class,
                mDR.child("FeedbackDriver")
        ) {
            @Override
            protected void populateViewHolder(FeedbackViewHolder viewHolder, Feedback model, int position) {
                viewHolder.txtFeedbackID.setText(adapterDriver.getRef(position).getKey());
                viewHolder.txtFeedbackDate.setText(model.getDate());
                viewHolder.txtCategory.setText(model.getCategory());
                viewHolder.txtMessage.setText(model.getMessage());
            }
        };
        mRecylerFeed.setAdapter(adapterStud);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        int id = item.getItemId();
        switch (id){
            case R.id.nav_Home:
                Intent AdminHome = new Intent(ViewFeedback.this, AdminHome.class);
                startActivity(AdminHome);
                finish();
                return true;
            case R.id.nav_AddBusSchedule:
                Intent addschedule = new Intent(ViewFeedback.this, Ad_AddBusSchedule.class);
                startActivity(addschedule);
                finish();
                return true;
            case R.id.nav_EditSchedule:
                Intent editschedule = new Intent(ViewFeedback.this, Ad_EditBusSchedule.class);
                startActivity(editschedule);
                finish();
                return true;
            case R.id.nav_BusReport:
                Intent busreport = new Intent(ViewFeedback.this, Ad_Bus_Report.class);
                startActivity(busreport);
                finish();
                return true;
            case R.id.nav_ViewFeedback:
                return true;
            case R.id.nav_Logout:
                FirebaseAuth.getInstance().signOut();
                Intent signoutIntent = new Intent(ViewFeedback.this, MainActivity.class);
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

