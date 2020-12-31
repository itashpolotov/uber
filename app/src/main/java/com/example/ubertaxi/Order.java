package com.example.ubertaxi;

import android.location.Location;

public class Order {

    LocationGeo location;

    String address, phone, username,objectId;


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Order(LocationGeo location, String address, String phone, String username) {
        this.location = location;
        this.address = address;
        this.phone = phone;
        this.username = username;
    }

    public Order(LocationGeo location, String address, String phone, String username, String objectId) {
        this.location = location;
        this.address = address;
        this.phone = phone;
        this.username = username;
        this.objectId = objectId;
    }

    public LocationGeo getLocation() {
        return location;
    }

    public void setLocation(LocationGeo location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}