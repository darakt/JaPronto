package com.example.darakt.japronto;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.darakt.japronto.REST.ApiService;
import com.example.darakt.japronto.REST.models.Area;
import com.example.darakt.japronto.REST.models.AreaResponse;
import com.example.darakt.japronto.REST.models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.nearby.connection.Connections;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    private final String TAG = "MapsActivity";
    private ApiService mApiService;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private LocationRequest mLocationRequest;
    protected String mLastUpdateTime ="";
    private static final int REQUEST_LOGIN = 1;
    private static final int GOT_FOOD = 2;
    User user = new User();
    public HashMap<String,Restaurant> index = new HashMap<String,Restaurant>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
        }

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent,REQUEST_LOGIN);
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    public void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


    }

    protected void startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates: ");
        Log.d(TAG, "onCreate: "+String.valueOf(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED));
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: ");
        mLastLocation = location;
        if (mLastLocation!=null) {
            putRestaurantNear();
        }else
            Log.d(TAG, "onLocationChanged: holy merde");
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        Toast.makeText(this, String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }
/*
    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
        if(mLastLocation != null & mApiService != null) {
            putRestaurantNear();
        }else
            Log.d(TAG, "onResume: holy merde");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
*/

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Log.d(TAG, "onMarkerClick: It worked");
                if (index.containsKey(marker.getTitle())) {
                    Restaurant rest = index.get(marker.getTitle());
                    Intent menu = new Intent(MapsActivity.this, MenuDisplay.class);
                    menu.putExtra("myRestaurant", rest);
                    startActivityForResult(menu, GOT_FOOD);
                }else
                    Toast.makeText(MapsActivity.this, "Strange business", Toast.LENGTH_LONG);
                return false;

            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d(TAG, "onInfoWindowClick: ???????????????????????");
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                User me = (User) data.getSerializableExtra("user");
                this.user = me;
                Log.d(TAG, "onActivityResult: "+mGoogleApiClient.isConnected());
                mApiService = ApiManager.createService(ApiService.class, user.getPseudo(), user.getPassword());
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if(mLastLocation != null) {
                    putRestaurantNear();
                    Log.d(TAG, "onActivityResult: cool?");
                }else
                    Log.d(TAG, "onActivityResult: holy merde");
            }
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location tmp = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.d(TAG, "onConnected: "+tmp.toString());
            mLastLocation = tmp;
            if (mLastLocation != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()),15));
                Log.d(TAG, "onConnected:  "+mLastLocation.toString());
                Toast.makeText(this, "Location detected!!!!", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "onConnected: fuck");
                Toast.makeText(this, "No location detected", Toast.LENGTH_SHORT).show();

            }
        }

    }
    
    public void putRestaurantNear(){
        Log.d(TAG, "putRestaurantNear: ");
        if (mLastLocation != null) {
            String lat = Double.toString(mLastLocation.getLatitude());
            String lng = Double.toString(mLastLocation.getLongitude());
            Log.d(TAG, "putRestaurantNear: "+lat+"    "+lng);
            Call<Area> call = mApiService.getRestNear(lat, lng);

            call.enqueue(new Callback<Area>() {
                @Override
                public void onResponse(Call<Area> call, Response<Area> response) {
                    Area tmp = response.body();
                    Log.d(TAG, "onResponse: "+tmp.getId());

                    Restaurant rest = new Restaurant(tmp.getChef(), tmp.getMenu());
                    index.put(tmp.getChef().getPseudo(), rest);
                    Log.d(TAG, "onResponse: "+index.containsKey(tmp.getChef().getPseudo()));
                    LatLng xy = new LatLng(tmp.getLati(), tmp.getLngi());
                    mMap.addMarker(new MarkerOptions()
                            .position(xy)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .title(tmp.getChef().getPseudo()));
                    /*
                    if (tmp!=null) {
                        for (Area ar : tmp) {
                            LatLng xy = new LatLng(ar.getLati(), ar.getLngi());
                            mMap.addMarker(new MarkerOptions()
                                    .position(xy)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    .title(Integer.toString(ar.getId())));
                        }
                    }else
                        Toast.makeText(MapsActivity.this, "Nothing found", Toast.LENGTH_LONG).show();
                        */
                }

                @Override
                public void onFailure(Call<Area> call, Throwable t) {
                    Log.d(TAG, "onFailure: "+t.getMessage());
                    Log.d(TAG, "onFailure: "+t.getStackTrace());
                }
            });
        }
}
/*
    @Override
    public boolean onMarkerClick(final Marker marker) {
        Log.d(TAG, "onMarkerClick: It worked");
        if (index.containsKey(marker.getTitle())) {
            Restaurant rest = index.get(marker.getTitle());
            Intent menu = new Intent(this, MenuDisplay.class);
            menu.putExtra("myRestaurant", rest);
            startActivityForResult(menu, GOT_FOOD);
        }else
            Toast.makeText(this, "Strange business", Toast.LENGTH_LONG);
        return false;
    }
*/
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }
}