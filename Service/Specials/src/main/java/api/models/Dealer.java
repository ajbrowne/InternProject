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

    @Override
    public String toString() {
        return "Dealer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", admin='" + admin + '\'' +
                ", loc=" + loc +
                ", location=" + location +
                '}';
    }

    @JsonIgnore
    private Point location;

}
