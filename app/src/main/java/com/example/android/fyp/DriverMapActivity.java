package com.example.android.fyp;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, RoutingListener {
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.colorPrimary};
    @Override
    public void onRoutingFailure(RouteException e) {
        // The Routing request failed
        if(e != null) {
            Log.d("Eror", e.getMessage());
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingCancelled() {

    }

    public interface MyCallback {
        void onCallback(String value);
    }

    private GoogleMap mMap;
    GoogleApiClient mGoogleAPIClient;
    Location LastLocation;
    LocationRequest mLocationRequest;
    DatabaseReference mDatabase;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBar;
    private Toolbar mToolBar;
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private String driver_destination;
    private Location apuLocation = new Location("");
    private Location assignationLocation = new Location("");
    Marker mCurrLocationMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);

        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
        DatabaseReference driverDat = FirebaseDatabase.getInstance().getReference("Users").child("DriversAssignation").child(currUser.getUid());
        driverDat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> td = (HashMap<String,String>) dataSnapshot.getValue();
                String driver_destination = td.get("assignation");

                switch (driver_destination){
                    case "Endah":
                        Fragment fragmentEndah = new Endah();
                        FragmentManager fragmentManagerEndah = getSupportFragmentManager();
                        fragmentManagerEndah.beginTransaction().replace(R.id.fragmentbus, fragmentEndah).commit();
                        break;
                    case "Lrt":
                        Fragment fragmentLrt = new Lrt();
                        FragmentManager fragmentManagerLrt = getSupportFragmentManager();
                        fragmentManagerLrt.beginTransaction().replace(R.id.fragmentbus, fragmentLrt).commit();
                        break;
                    case "Vista":
                        Fragment fragmentVista = new Lrt();
                        FragmentManager fragmentManagerVista = getSupportFragmentManager();
                        fragmentManagerVista.beginTransaction().replace(R.id.fragmentbus, fragmentVista).commit();
                        break;
                    case "Southcity":
                        Fragment fragmentSouthcity = new Lrt();
                        FragmentManager fragmentManagerSouthcity = getSupportFragmentManager();
                        fragmentManagerSouthcity.beginTransaction().replace(R.id.fragmentbus, fragmentSouthcity).commit();
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        polylines = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.nav_action);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        mDrawerLayout = findViewById(R.id.drawerLayout);

        mAuth = FirebaseAuth.getInstance();
        NavigationView navigationView = (NavigationView) findViewById(R.id.menuNavigationView1);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.head_title_user);
        FirebaseUser curruser = mAuth.getCurrentUser();
        nav_user.setText("Welcome, "+curruser.getEmail().split("@")[0]);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        menuItem.setChecked(true);
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.nav_Home:
                                Intent home = new Intent(DriverMapActivity.this, DriverHome.class);
                                startActivity(home);
                                finish();
                                return true;
                            case R.id.nav_BusTracking:
                                return true;
                            case R.id.nav_Feedback:
                                Intent feedback = new Intent(DriverMapActivity.this, FeedbackDriver.class);
                                startActivity(feedback);
                                finish();
                                return true;
                            case R.id.nav_Logout:
                                FirebaseAuth.getInstance().signOut();
                                Intent signoutIntent = new Intent(DriverMapActivity.this, MainActivity.class);
                                startActivity(signoutIntent);
                                finish();
                                return true;
                            default:
                                return true;
                        }
                    }
                });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleAPIClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleAPIClient, this);
        }
    }

    public void getDriverDestination(final MyCallback myCallback) {

        DatabaseReference driverDat = FirebaseDatabase.getInstance().getReference("Users").child("DriversAssignation").child(currUser.getUid());
        driverDat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> td = (HashMap<String,String>) dataSnapshot.getValue();
                String driver_destination = td.get("assignation");
                myCallback.onCallback(driver_destination);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap=googleMap;

        getDriverDestination(new MyCallback() {
            @Override
            public void onCallback(final String value) {
                mDatabase = FirebaseDatabase.getInstance().getReference("Location");
                mDatabase.addValueEventListener(new ValueEventListener() {

                    ArrayList<LatLng> locations = new ArrayList();
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //first data =apu, second = endah, third vista, fourth lrt
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            Map<String, Double> td = (HashMap<String,Double>) postSnapshot.getValue();
                            if(postSnapshot.getKey().equals(value) || postSnapshot.getKey().equals("Apu")) {
                                Double latitude = td.get("latitude");
                                Double longitude = td.get("longitude");
                                LatLng latlong = new LatLng(latitude, longitude);
                                if(postSnapshot.getKey().equals("Apu")) {
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(latlong)
                                            .title(postSnapshot.getKey())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                }
                                else {
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(latlong)
                                            .title(postSnapshot.getKey())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                                }

                                locations.add(latlong);
                            }

                        }
                        drawRouteDriverLocations(locations.get(0), locations.get(1));
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleAPIClient();
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleAPIClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void  buildGoogleAPIClient(){
        mGoogleAPIClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleAPIClient.connect();
    }

    private void drawRouteDriverLocations(LatLng start, LatLng end) {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(start, end)
                .key("AIzaSyAumgLGtWXIz2A5ld6IIlqMxeaPI1cNkPY")
                .build();
        routing.execute();

    }

    @Override
    public void onLocationChanged(Location location) {

        LastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_school_bus));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera


        String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.setLocation(UserID, new GeoLocation(location.getLatitude(), location.getLongitude()), new
                GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {

                    }
                }
        );

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleAPIClient, mLocationRequest, this);
            LastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleAPIClient);
            if (LastLocation != null) {
                LatLng loc = new LatLng(LastLocation.getLatitude(), LastLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


                DatabaseReference drivRef = FirebaseDatabase.getInstance().getReference().child("Users").child("DriversAssignation").child(currUser.getUid());
                drivRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String assignation = (String) dataSnapshot.child("assignation").getValue();
                        float distanceAssignation = LastLocation.distanceTo(assignationLocation);
                        float distanceApu = LastLocation.distanceTo(apuLocation);


                        if(distanceAssignation<50) {
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
                            String formattedDate = df.format(c);
                            String formattedTime = tf.format(c);
                            Report report = new Report("APU", assignation, assignation, formattedTime, formattedDate);
                            mDatabase.child("Report").push().setValue(report);
                        }

                        if(distanceApu<50){
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
                            String formattedDate = df.format(c);
                            String formattedTime = tf.format(c);
                            Report report = new Report(assignation, "APU", assignation, formattedTime, formattedDate);
                            mDatabase.child("Report").push().setValue(report);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            }
        }



    @Override
    public void onConnectionSuspended(int i) {


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();

        String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(UserID, new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {

            }
        });

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(DriverMapActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mMap == null) {
                            buildGoogleAPIClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}