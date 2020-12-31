package com.example.ubertaxi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface Api {
    @Headers({
            "X-Parse-Application-Id: Emyy8PWS8PTbpppDz4C2mmBU5fnKdp9nfujIo5Xg",
            "X-Parse-REST-API-Key: 8dzADx4Y0yDnJwJ65GWdEeVyG61nqETHghHpnl7S",
            "X-Parse-Revocable-Session: 1"
    })
    @POST("/users")
    Call<RegisterResponse> signUpUser(@Body User user);

    @Headers({
            "X-Parse-Application-Id: Emyy8PWS8PTbpppDz4C2mmBU5fnKdp9nfujIo5Xg",
            "X-Parse-REST-API-Key:8dzADx4Y0yDnJwJ65GWdEeVyG61nqETHghHpnl7S",
            "X-Parse-Revocable-Session:1"
    })
   @GET("/login")
    Call<LoginResponse> loginUser(@Query("username") String username,@Query("password") String password);

    @Headers({
            "X-Parse-Application-Id: Emyy8PWS8PTbpppDz4C2mmBU5fnKdp9nfujIo5Xg",
            "X-Parse-REST-API-Key: 8dzADx4Y0yDnJwJ65GWdEeVyG61nqETHghHpnl7S",

    })
    @GET("/classes/Order")

    Call<Result> getOrders();

    @Headers({
            "X-Parse-Application-Id: Emyy8PWS8PTbpppDz4C2mmBU5fnKdp9nfujIo5Xg",
            "X-Parse-REST-API-Key: 8dzADx4Y0yDnJwJ65GWdEeVyG61nqETHghHpnl7S",

    })
    @POST("/classes/Order")
    Call<Order> addOrder(@Body Order order);

}
