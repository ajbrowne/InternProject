package com.example.specialsapp.app.Models;

/**
 * Basic location object that stores latitude and longitude.
 */
public class LocationObject {

    private double latitude;
    private double longitude;

    public LocationObject() {

    }

    public LocationObject(double latititude, double longitude) {
        this.latitude = latititude;
        this.longitude = longitude;
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
}
