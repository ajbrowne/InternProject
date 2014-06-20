package com.example.specialsapp.app.Models;

/**
 * Created by brownea on 6/20/14.
 */
public class Dealer {

    private String name;
    private String city;
    private String state;
    private int numSpecials;
    private double distanceFrom;

    public Dealer() {
    }

    public Dealer(String name, String city, String state, int numSpecials, double distanceFrom) {
        this.name = name;
        this.city = city;
        this.state = state;
        this.numSpecials = numSpecials;
        this.distanceFrom = distanceFrom;
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

    @Override
    public String toString() {
        return "Dealer{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
