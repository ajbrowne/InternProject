package api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;

/**
 * Created by maharb on 6/18/14.
 */
public class Dealer {

    public Dealer(){}

    public Dealer(String id, String name, String admin, Location loc) {
        this.id = id;
        this.name = name;
        this.admin = admin;
        this.loc = loc;
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

    @Id
    private String id;
    private String name;
    private String admin;
    private Location loc;

    public Dealer(String name, String admin, Location loc, String city, String state, Point location) {
        this.name = name;
        this.admin = admin;
        this.loc = loc;
        this.city = city;
        this.state = state;
        this.location = location;
    }

    public Dealer(String keyword){
        this.name = keyword;
        this.admin = keyword;
        this.city = keyword;
        this.state = keyword;
    }

    @Override
    public String toString() {
        return "Dealer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", admin='" + admin + '\'' +
                ", loc=" + loc +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", location=" + location +
                '}';
    }

    private String city;

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

    private String state;

    public Dealer(Point location, Location loc, String admin, String name) {
        this.location = location;
        this.loc = loc;
        this.admin = admin;
        this.name = name;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    @JsonIgnore
    private Point location;

}
