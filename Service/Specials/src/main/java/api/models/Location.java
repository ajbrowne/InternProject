package api.models;

import java.util.Arrays;

/**
 * Created by maharb on 6/18/14.
 */
public class Location {

    public Location(){}

    public Location(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    private double[] coordinates;

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

    public Location(double[] coordinates, String type) {
        this.coordinates = coordinates;
        this.type = type;
    }

    private String type;
}
