package main.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by maharb on 6/11/14.
 */
@Document(collection = "users")
public class User {

    public User(){}

    @Id
    private String id;
    private String username;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", password='" + password + '\'' +
                '}';
    }

    private int role;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String email) {
        this.username = email;
    }



    public User(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = 1;
    }

    private String password;

}
