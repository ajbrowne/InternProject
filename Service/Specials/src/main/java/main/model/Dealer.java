package main.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by maharb on 6/12/14.
 */
@Document(collection="dealers")
public class Dealer {

    public Dealer(){}

    public Dealer(String id, String name, String admin, Point loc) {
        this.id = id;
        this.name = name;
        this.admin = admin;
        this.loc = loc;
    }

    @Override
    public String toString() {
        return "Dealer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", admin='" + admin + '\'' +
                ", loc=" + loc +
                '}';
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

    public Point getLoc() {
        return loc;
    }

    public void setLoc(Point loc) {
        this.loc = loc;
    }

    @Id
    private String id;
    private String name;
    private String admin;
    private Point loc;

}
