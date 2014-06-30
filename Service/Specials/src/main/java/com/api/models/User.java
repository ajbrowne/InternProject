package com.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by maharb on 6/18/14.
 */
@Document(collection = "users")
public class User {

    public User(){}

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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
    private String phone;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", zip='" + zip + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    private String zip;

    public User(String username, int role, String password, String phone, String zip, String firstName, String lastName) {
        this.username = username;
        this.role = role;
        this.password = password;
        this.phone = phone;
        this.zip = zip;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private String firstName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String lastName;


}

