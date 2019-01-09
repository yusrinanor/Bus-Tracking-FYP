package com.example.android.fyp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ViewBusSchedule extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

  /**
   * The {@link android.support.v4.view.PagerAdapter} that will provide
   * fragments for each of the sections. We use a
   * {@link FragmentPagerAdapter} derivative, which will keep every
   * loaded fragment in memory. If this becomes too memory intensive, it
   * may be best to switch to a
   * {@link android.support.v4.app.FragmentStatePagerAdapter}.
   */
  private SectionsPagerAdapter mSectionsPagerAdapter;

  /**
   * The {@link ViewPager} that will host the section contents.
   */
  private ViewPager mViewPager;
  private DrawerLayout mDrawerLayout;
  private ActionBarDrawerToggle mActionBar;
  private Toolbar mToolBar;
  private FirebaseAuth mAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_bus_schedule);

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

    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    // Set up the ViewPager with the sections adapter.
    mViewPager = (ViewPager) findViewById(R.id.container);
    mViewPager.setAdapter(mSectionsPagerAdapter);

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_view_bus_schedule, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    if(mActionBar.onOptionsItemSelected(item)){

      return true;

    }
    return super.onOptionsItemSelected(item);
  }
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    item.setChecked(true);
    int id = item.getItemId();
    switch (id){
      case R.id.nav_Home:
        Intent busSchedule = new Intent(ViewBusSchedule.this, StudentHome.class);
        startActivity(busSchedule);
        finish();
      case R.id.nav_ViewBusSchedule:
        return true;
      case R.id.nav_BusTracking:
        Intent bustracking = new Intent(ViewBusSchedule.this, RoutePickerStud.class);
        startActivity(bustracking);
        finish();
        return true;
      case R.id.nav_Feedback:
        Intent userFeedback = new Intent(ViewBusSchedule.this, FeedbackStud.class);
        startActivity(userFeedback);
        finish();
        return true;
      case R.id.nav_Logout:
        FirebaseAuth.getInstance().signOut();
        Intent signoutIntent = new Intent(ViewBusSchedule.this, MainActivity.class);
        startActivity(signoutIntent);
        finish();
        return true;
      default:
        return true;
    }
  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
      PlaceholderFragment fragment = new PlaceholderFragment();
      Bundle args = new Bundle();
      args.putInt(ARG_SECTION_NUMBER, sectionNumber);
      fragment.setArguments(args);
      return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_view_bus_schedule, container, false);
      TextView textView = (TextView) rootView.findViewById(R.id.section_label);
      textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
      return rootView;
    }
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
   * one of the sections/tabs/pages.
   */
  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position){
        case 0:
          Vista vista = new Vista();
          return vista;
        case 1:
          Endah endah = new Endah();
          return endah;
        case 2:
          Southcity southcity = new Southcity();
          return southcity;
        case 3:
          Lrt lrt = new Lrt();
          return lrt;

      }
      return null;
    }


    @Override
    public int getCount() {
      // Show 3 total pages.
      return 4;
    }
  }
}
