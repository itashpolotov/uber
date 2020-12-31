package com.example.ubertaxi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng sydney;
    Button order_taxi;
    String destination_address="";
    TextView current_location;

    Location myLocation = new Location("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        order_taxi = findViewById(R.id.orderTaxi);
        current_location=findViewById(R.id.current_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        order_taxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getUserLocation();

                    getDestionationAddress();



            }
        });
    }



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


   //     mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); //set map type displayed per default
        // Add a marker in Sydney and move the camera
        getUserLocation();

    }


    private void getUserLocation() {

        try {  //if GSM is not available then go to error
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 5, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {

                    Toast.makeText(MapsActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
                    sydney = new LatLng(location.getLatitude(), location.getLongitude());
                    myLocation.setLatitude(location.getLatitude());
                    myLocation.setLongitude(location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));//custom marker can also be used.
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));

                    try {
                        Geocoder geocoder=new Geocoder(MapsActivity.this, Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        String address=addressList.get(0).getAddressLine(0);
                        current_location.setText(address);
                        Log.i("Location",address);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    ; //convert coordinates to address

                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {

                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(MapsActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            e.getMessage();
        }
    }

    private void getDestionationAddress() {

        AlertDialog.Builder dialogAdress = new AlertDialog.Builder(this);

        dialogAdress.setTitle("Please enter the destination address");

        View order_layout = LayoutInflater.from(this).inflate(R.layout.ordert_layout, null);


        dialogAdress.setView(order_layout);

        dialogAdress.setPositiveButton("Order taxi", new DialogInterface.OnClickListener() {



          MaterialEditText order_address = order_layout.findViewById(R.id.orderAddress);


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (TextUtils.isEmpty(order_address.getText().toString())) {
                    Toast.makeText(MapsActivity.this, "Please enter the address", Toast.LENGTH_SHORT).show();
                    return;
                }
                destination_address = order_address.getText().toString();
               if(!TextUtils.isEmpty(destination_address)) {
                    Intent intent = getIntent();
                    String phone = intent.getStringExtra("phone");
                    String username = intent.getStringExtra("username");

                    postOrder(myLocation, destination_address, phone, username);
                }


            }
        });
        dialogAdress.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialogAdress.show();


    }
    private void postOrder(Location myLocation, String destination_address,String phone, String username) {



            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://parseapi.back4app.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Api api = retrofit.create(Api.class);
            LocationGeo locationGeo=new LocationGeo("GeoPoint",myLocation.getLatitude(),myLocation.getLongitude());

            Call<Order> call = api.addOrder(new Order(locationGeo, destination_address, phone, username));

            call.enqueue(new Callback<Order>() {

                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "order has been created", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), String.valueOf(response.code()), Toast.LENGTH_LONG).show();
                        Log.i("postIkbol", String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    Log.i("ikbol", t.getMessage());
                }
            });
        }
    }

