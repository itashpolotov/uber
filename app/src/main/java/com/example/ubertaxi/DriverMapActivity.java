package com.example.ubertaxi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class DriverMapActivity extends FragmentActivity implements OnMapReadyCallback {
    Button accept_request;
    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng driverPosition,customerPosition;
    Location driverLocation = new Location("");
    Location userLocation = new Location("");
    String objectId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);
        accept_request = findViewById(R.id.acceptRequest);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

     //    driverLocation = (Location)getIntent().getParcelableExtra("driverLocation");

         userLocation=(Location)getIntent().getParcelableExtra("userLocation");
        Intent intent = getIntent();
        objectId=intent.getStringExtra("objectId");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_driver);
        mapFragment.getMapAsync(this);


        accept_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Uri gmmIntentUri = Uri.parse("google.navigation:q="+userLocation);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
                Toast.makeText(DriverMapActivity.this, "button check", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       getUserLocation();
       customerPosition=new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(customerPosition).title("Customer").icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker)));//custom marker can also be used.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(customerPosition, 17));

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

                    Toast.makeText(DriverMapActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
                    driverPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    driverLocation=location;

                    //driverLocation.setLatitude(location.getLatitude());
                  //  driverLocation.setLongitude(location.getLongitude());

                   mMap.addMarker(new MarkerOptions().position(driverPosition).title("Driver").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));//custom marker can also be used.
             //       mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(driverPosition, 17));

                    try {
                        Geocoder geocoder=new Geocoder(DriverMapActivity.this, Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        String address=addressList.get(0).getAddressLine(0);

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
            e.getMessage();
        }
    }
}