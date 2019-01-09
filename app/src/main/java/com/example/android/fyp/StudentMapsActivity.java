package com.example.android.fyp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
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
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
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

import static com.example.android.fyp.DriverMapActivity.MY_PERMISSIONS_REQUEST_LOCATION;

public class StudentMapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, RoutingListener {

    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.colorPrimary};
    @Override
    public void onRoutingFailure(RouteException e) {
        // The Routing request failed
        if(e != null) {
            Log.d("Error", e.getMessage());
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
    private GoogleMap mMap;
    GoogleApiClient mGoogleAPIClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBar;
    private Toolbar mToolBar;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String location;
    private FirebaseUser currUser;
    private String driver_destination;
    private Location apuLocation = new Location("");
    private Location assignationLocation = new Location("");
    Marker mCurrLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_maps);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            location = extras.getString("location");
        }
        polylines = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.nav_action);
        setSupportActionBar(toolbar);
        //setSupportActionBar(toolbar);
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
                                Intent home = new Intent(StudentMapsActivity.this, StudentHome.class);
                                startActivity(home);
                                finish();
                                return true;
                            case R.id.nav_BusTracking:
                                return true;
                            case R.id.nav_Feedback:
                                Intent feedback = new Intent(StudentMapsActivity.this, FeedbackStud.class);
                                startActivity(feedback);
                                finish();
                                return true;
                            case R.id.nav_Logout:
                                FirebaseAuth.getInstance().signOut();
                                Intent signoutIntent = new Intent(StudentMapsActivity.this, MainActivity.class);
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


    protected void onPause() {
        super.onPause();

        if (mGoogleAPIClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleAPIClient, this);
        }
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mDatabase = FirebaseDatabase.getInstance().getReference("Location");
        mDatabase.addValueEventListener(new ValueEventListener() {

            ArrayList<LatLng> locations = new ArrayList();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //first data =apu, second = endah, third vista, fourth lrt
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Map<String, Double> td = (HashMap<String,Double>) postSnapshot.getValue();
                    if(postSnapshot.getKey().equals(location) || postSnapshot.getKey().equals("Apu")) {
                        Double latitude = td.get("latitude");
                        Double longitude = td.get("longitude");
                        LatLng latlong = new LatLng(latitude, longitude);
                        if(postSnapshot.getKey().equals("Apu")) {
                            apuLocation.setLongitude(longitude);
                            apuLocation.setLatitude(latitude);
                            mMap.addMarker(new MarkerOptions()
                                    .position(latlong)
                                    .title(postSnapshot.getKey())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        }
                        else {
                            assignationLocation.setLatitude(latitude);
                            assignationLocation.setLongitude(longitude);
                            mMap.addMarker(new MarkerOptions()
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



        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleAPIClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleAPIClient.connect();
    }

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
                                ActivityCompat.requestPermissions(StudentMapsActivity.this,
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
                            buildGoogleApiClient();
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

    public void pushNotif(String title, String message) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "push")
                .setSmallIcon(R.drawable.logo1)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
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
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleAPIClient);
            if (mLastLocation != null) {
                LatLng loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");
                GeoFire geoFire = new GeoFire(databaseReference);
                GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 5);
                final HashMap<String, Marker> markers = new HashMap<String, Marker>();
                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                    @Override
                    public void onKeyEntered(final String key, final GeoLocation geolocation) {
                        DatabaseReference drivRef = FirebaseDatabase.getInstance().getReference().child("Users").child("DriversAssignation").child(key);
                        drivRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String assignation = (String) dataSnapshot.child("assignation").getValue();
                                if(assignation.equals(location)) {

                                    LatLng latLng = new LatLng(geolocation.latitude, geolocation.longitude);
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(latLng);
                                    markerOptions.title("Driver");
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_school_bus));
                                    Marker marker = mMap.addMarker(markerOptions);
                                    markers.put(key, marker);
                                    Location driverLocation = new Location("");
                                    driverLocation.setLatitude(geolocation.latitude);
                                    driverLocation.setLongitude(geolocation.longitude);

                                    float distanceAssignation = driverLocation.distanceTo(assignationLocation);
                                    float distanceApu = driverLocation.distanceTo(apuLocation);

                                    if(distanceAssignation<100){
                                        pushNotif("APU's Shuttle Bus Service", "Bus Arriving To Destination");
                                    } else if(distanceAssignation<50) {
                                        pushNotif("APU's Shuttle Bus Service", "Bus Has Arrived At Destination");
                                        mDatabase = FirebaseDatabase.getInstance().getReference();
                                        Date c = Calendar.getInstance().getTime();
                                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
                                        String formattedDate = df.format(c);
                                        String formattedTime = tf.format(c);
                                        Report report = new Report("APU", assignation, assignation, formattedTime, formattedDate);
                                        mDatabase.child("Report").push().setValue(report);
                                    }

                                    if(distanceApu<100){
                                        pushNotif("APU's Shuttle Bus Service", "Bus Arriving To APU");
                                    } else if(distanceApu<50){
                                        pushNotif("APU's Shuttle Bus Service", "Bus Has Arrived At APU");
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
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onKeyExited(String key) {
                        Marker marker = markers.get(key);
                        if(marker != null) {
                            marker.remove();
                            markers.remove(key);
                        }

                    }

                    @Override
                    public void onKeyMoved(String key, GeoLocation location) {

                    }

                    @Override
                    public void onGeoQueryReady() {

                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {

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

    protected void onStop() {
        super.onStop();
    }

    private void drawRouteDriverLocations(LatLng start, LatLng end) {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(start, end)
                .key("AIzaSyBdTa0sWj5MiqGbUF14lE6odAvQdIFUL88")
                .build();
        routing.execute();

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());

        CameraPosition camPos = new CameraPosition.Builder()
                .target(latlng)
                .zoom(18)
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(camUpd3);





    }




}