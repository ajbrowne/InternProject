package com.internproject.api.models;

import java.util.Arrays;

/**
 * Custom location object that allows us to store dealer location without breaking
 * the service because Spring doesn't know how to serialize a Point object
 * <p/>
 * Created by maharb on 6/18/14.
 */
public class Location {

    private double[] coordinates;
    private String type;

    public Location() {
    }

    public Location(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public Location(double[] coordinates, String type) {
        this.coordinates = coordinates;
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Location{" +
                "coordinates=" + Arrays.toString(coordinates) +
                ", type='" + type + '\'' +
                '}';
    }
}
