package com.example.ubertaxi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Orders extends AppCompatActivity {
    ListView listView;
    LocationManager locationManager;
    List<String> ordersToPost = new ArrayList<String>();
//    LocationListener locationListener;

    Location driverLocation = new Location("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        listView = findViewById(R.id.listView);
        getDriverLocation();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ordersToPost);

    }

    private void getListOfOrders() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://parseapi.back4app.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<Result> call = api.getOrders();

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    Result result = response.body();
                    List<Order> orders = result.getOrders();

                    Location userLocation = new Location("");
                    //           Log.i("Ikbol",String.valueOf(orders.size()));


                    for (int i=0;i<orders.size();i++) {
                        userLocation.setLongitude(orders.get(i).getLocation().getLongitude());
                        userLocation.setLatitude(orders.get(i).getLocation().getLatitude());
                        float distance = driverLocation.distanceTo(userLocation);

                        ordersToPost.add(String.valueOf(distance / 1000) + " km");
                        Log.i("Ikbol", String.valueOf(orders.get(i).getUsername()));
                        Log.i("Ikbol", String.valueOf(orders.get(i).getAddress()));
                    }
                    ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,ordersToPost);
                    listView.setAdapter(adapter); //view list of distances
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(Orders.this, DriverMapActivity.class);
                            String objectId=orders.get(i).getObjectId();

                                intent.putExtra("driverLocation", (Location)driverLocation);
                                intent.putExtra("userLocation", (Location) userLocation);
                                intent.putExtra("objectId", (String) objectId);



                            //   intent.putExtra("position", (int) position);
                            startActivity(intent);
                        }

                    });

                }
            }
            @Override
    public void onFailure(Call<Result> call, Throwable t) {
        Log.i("Ikbol", t.getMessage());
    }
});
        }


private void getDriverLocation(){

        try{  //if GSM is not available then go to error
        locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5,5,new LocationListener(){
@Override
public void onLocationChanged(@NonNull Location location){


        //LatLng = new LatLng(location.getLatitude(), location.getLongitude());

        driverLocation.setLatitude(location.getLatitude());
        driverLocation.setLongitude(location.getLongitude());
        getListOfOrders();


        }

@Override
public void onProviderEnabled(@NonNull String provider){

        }

@Override
public void onProviderDisabled(@NonNull String provider){

        }

@Override
public void onStatusChanged(String provider,int status,Bundle extras){

        }
        });

        }catch(Exception e){
        e.getMessage();
        }
        }
        }