package com.example.ubertaxi;

public class LocationGeo {
   String  __type;
   double    latitude;
    double longitude;

    public String get__type() {
        return __type;
    }

    public void set__type(String __type) {
        this.__type = __type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LocationGeo(String __type, double latitude, double longitude) {
        this.__type = __type;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
