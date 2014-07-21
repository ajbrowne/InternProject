package com.example.specialsapp.app.Models;

/**
 * Basic dealer object with fields necessary for app.
 */
public class Dealer {

    private String name;
    private String city;
    private String state;
    private int numSpecials;
    private double distanceFrom;
    private double latitude;
    private double longitude;

    public Dealer() {
    }

    public Dealer(String name, String city, String state, int numSpecials, double latitude, double longitude) {
        this.name = name;
        this.city = city;
        this.state = state;
        this.numSpecials = numSpecials;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getNumSpecials() {
        return numSpecials;
    }

    public void setNumSpecials(int numSpecials) {
        this.numSpecials = numSpecials;
    }

    public double getDistanceFrom() {
        return distanceFrom;
    }

    public void setDistanceFrom(double distanceFrom) {
        this.distanceFrom = distanceFrom;
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

    @Override
    public String toString() {
        return "Dealer{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
