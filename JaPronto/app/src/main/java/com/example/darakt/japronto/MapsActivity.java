package com.example.darakt.japronto;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.darakt.japronto.REST.ApiService;
import com.example.darakt.japronto.REST.ApiManager;
import com.example.darakt.japronto.REST.models.Area;
import com.example.darakt.japronto.REST.models.Client;
import com.example.darakt.japronto.REST.models.Dish;
import com.example.darakt.japronto.REST.models.Order;
import com.example.darakt.japronto.REST.models.Restaurant;
import com.example.darakt.japronto.historic.ListOrder;
import com.example.darakt.japronto.order.MenuDisplay;
import com.example.darakt.japronto.order.MyDatePickerFragment;
import com.example.darakt.japronto.order.MyTimePickerFragment;
import com.example.darakt.japronto.order.Summary;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, MyTimePickerFragment.TimePickerListener, MyDatePickerFragment.DatePickerListener
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
    private static final int WORKED = 8;
    private static final int SUMMARY = 16;
    private static final int NOTWORKED = 13;
    private static final int CLEAN = 22;
    Client client = new Client();
    private HashMap<String,Restaurant> index = new HashMap<String,Restaurant>();
    private Order wants = new Order();
    private String dateFor, timeFor = "";
    private Marker deliverHere;

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

        FloatingActionButton when = (FloatingActionButton) findViewById(R.id.when);
        when.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialogFragment = new MyTimePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), "datePicker");
                DialogFragment dialogFr = new MyDatePickerFragment();
                dialogFr.show(getSupportFragmentManager(), "datePicker");
            }
        });

        FloatingActionButton summary = (FloatingActionButton) findViewById(R.id.order);
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wants.getWanted().getDishes().size() != 0 && wants.getFor_the_date()!=null) {
                    Intent intent = new Intent(MapsActivity.this, Summary.class);
                    intent.putExtra("myOrder", wants);
                    intent.putExtra("Client", client);
                    startActivityForResult(intent, SUMMARY);
                }else if (wants.getWanted().getDishes().size() == 0 && wants.getFor_the_date() ==null)
                    Toast.makeText(MapsActivity.this, "Vc precisa de escolher pratos, uma data e uma hora de entrega", Toast.LENGTH_LONG).show();
                else if(wants.getWanted().getDishes().size() == 0)
                    Toast.makeText(MapsActivity.this, "Vc precisa de escolher pratos", Toast.LENGTH_LONG).show();
                else if (wants.getFor_the_date() == null)
                    Toast.makeText(MapsActivity.this, "Vc precisa de escolher uma data e uma hora de entrega", Toast.LENGTH_LONG).show();
            }
        });

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
        }else {
            Log.d(TAG, "onLocationChanged: holy merde");
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            //wants.setLat(Double.toString(location.getLatitude()));
            //wants.setLng(Double.toString(location.getLongitude()));
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the client will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the client has
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
                if (index.containsKey(marker.getTitle())) {
                    Restaurant rest = index.get(marker.getTitle());
                    Intent menu = new Intent(MapsActivity.this, MenuDisplay.class);
                    menu.putExtra("myRestaurant", rest);
                    menu.putExtra("myOrder", wants);
                    Log.d(TAG, "onMarkerClick: "+rest.getMenu().get(0).getName());
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
        switch (requestCode) {
            case REQUEST_LOGIN: {
                if (resultCode == RESULT_OK) {
                    Client me = (Client) data.getSerializableExtra("client");
                    this.client = me;
                    Log.d(TAG, "onActivityResult: " + mGoogleApiClient.isConnected());
                    wants.setCustomer_pseudo(client.getPseudo());
                    mApiService = ApiManager.createService(ApiService.class, client.getPseudo(), client.getPassword());
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (mLastLocation != null) {
                        putRestaurantNear();
                    } else
                        Log.d(TAG, "onActivityResult: holy merde");

                    FloatingActionButton old = (FloatingActionButton) findViewById(R.id.old);
                    old.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Call<List<Order>> call = mApiService.getMyOrders(client.getPseudo());
                            call.enqueue(new Callback<List<Order>>() {
                                @Override
                                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                                    List<Order> all = response.body();
                                    Log.d(TAG, "onResponse: "+ all.get(0).getWanted().getDishes().get(0).getNumber());
                                    Intent intent = new Intent(MapsActivity.this, ListOrder.class);
                                    Order[] o = all.toArray(new Order[all.size()]);
                                    intent.putExtra("orders", o);
                                    intent.putExtra("size", all.size());
                                    Log.d(TAG, "onResponse: " + o[0].getCustomer_pseudo());
                                    Log.d(TAG, "onResponse: List");
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(Call<List<Order>> call, Throwable t) {
                                    Log.d(TAG, "onFailure: " + t.getMessage());
                                }
                            });

                        }
                    });

                }

            }
            break;
            case GOT_FOOD:
                if (resultCode == WORKED) {
                    wants = (Order) data.getSerializableExtra("myOrder");
                    for (Dish d : wants.getWanted().getDishes()) {
                        Log.d(TAG, "onActivityResult: " + d.getName() + "   x  " + d.getNumber());
                    }
                }
            break;
            case SUMMARY:
                if (resultCode == CLEAN) {
                    wants = (Order) data.getSerializableExtra("myOrder");

                }
                break;
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
                wants.setLat(Double.toString(mLastLocation.getLatitude()));
                wants.setLng(Double.toString(mLastLocation.getLongitude()));
                deliverHere =  mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                                            .title("Entregar aqui!")
                                            .draggable(true)
                                            .icon(BitmapDescriptorFactory.defaultMarker(336)));
                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {
                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {
                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        Log.d(TAG, "onMarkerDragEnd: "+marker.getPosition());
                        wants.setLat(Double.toString(marker.getPosition().latitude));
                        wants.setLng(Double.toString(marker.getPosition().longitude));
                    }
                });
                deliverHere.showInfoWindow();
                Log.d(TAG, "onConnected:  "+mLastLocation.toString());
            } else {
                Log.d(TAG, "onConnected: fuck");
                Toast.makeText(this, "No location detected", Toast.LENGTH_SHORT).show();

            }
        }

    }
    
    public void putRestaurantNear(){
        if (mLastLocation != null) {
            String lat = Double.toString(mLastLocation.getLatitude());
            String lng = Double.toString(mLastLocation.getLongitude());
            Log.d(TAG, "putRestaurantNear: "+lat+"    "+lng);
            Call<List<Area>> call = mApiService.getRestNear(lat, lng);

            call.enqueue(new Callback<List<Area>>() {
                @Override
                public void onResponse(Call<List<Area>> call, Response<List<Area>> response) {
                    List<Area> all = response.body();
                    for (Area tmp : all) {
                        Restaurant rest = new Restaurant(tmp.getChef(), tmp.getMenu());
                        index.put(tmp.getChef().getPseudo(), rest);
                        LatLng xy = new LatLng(tmp.getLati(), tmp.getLngi());
                        mMap.addMarker(new MarkerOptions()
                                .position(xy)
                                .icon(BitmapDescriptorFactory.defaultMarker(101))
                                .title(tmp.getChef().getPseudo()));
                    }
                }

                @Override
                public void onFailure(Call<List<Area>> call, Throwable t) {
                    Log.d(TAG, "onFailure: "+t.getMessage());
                    Log.d(TAG, "onFailure: "+t.getStackTrace());
                }
            });
        }
    }

    @Override
    public void OnfinshDatePick(String date) {
        Log.d(TAG, "OnfinshDatePick: "+date);
        wants.setFor_the_date(date);
    }

    @Override
    public void OnfinshTimePick(String time) {
        Log.d(TAG, "OnfinshTimePick: "+time);
        wants.setFor_the_time(time);
    }

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