package com.internproject.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;

import java.util.List;

/**
 * The dealer object, contains all of the important information about a dealer
 * <p/>
 * Created by maharb on 6/18/14.
 */
public class Dealer {

    @Id
    private String id;
    private String name;
    private String admin;
    private Location loc;
    private int numSpecials;
    private List<String> make;
    private String city;
    private String state;
    @JsonIgnore
    private Point location;

    public Dealer() {
    }

    public Dealer(String id, String name, String admin, Location loc) {
        this.id = id;
        this.name = name;
        this.admin = admin;
        this.loc = loc;
    }
    public Dealer(String id, String name, String admin, Location loc, int numSpecials, List<String> make, String city, String state, Point location) {
        this.id = id;
        this.name = name;
        this.admin = admin;
        this.loc = loc;
        this.numSpecials = numSpecials;
        this.make = make;
        this.city = city;
        this.state = state;
        this.location = location;
    }
    public Dealer(String id, String name, String admin, Location loc, List<String> make, String city, String state, Point location) {
        this.id = id;
        this.name = name;
        this.admin = admin;
        this.loc = loc;
        this.make = make;
        this.city = city;
        this.state = state;
        this.location = location;
    }
    public Dealer(String name, String admin, Location loc, String city, String state, Point location) {
        this.name = name;
        this.admin = admin;
        this.loc = loc;
        this.city = city;
        this.state = state;
        this.location = location;
    }

    public Dealer(String keyword) {
        this.name = keyword;
        this.admin = keyword;
        this.city = keyword;
        this.state = keyword;
    }

    public Dealer(Point location, Location loc, String admin, String name) {
        this.location = location;
        this.loc = loc;
        this.admin = admin;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public int getNumSpecials() {
        return numSpecials;
    }

    public void setNumSpecials(int numSpecials) {
        this.numSpecials = numSpecials;
    }

    @Override
    public String toString() {
        return "Dealer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", admin='" + admin + '\'' +
                ", loc=" + loc +
                ", numSpecials=" + numSpecials +
                ", make=" + make +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", location=" + location +
                '}';
    }

    public List<String> getMake() {
        return make;
    }

    public void setMake(List<String> make) {
        this.make = make;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

}
