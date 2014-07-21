package com.example.specialsapp.app.Models;

/**
 * Basic user object used for login and registration.
 */
public class User {

    private String zip;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public User() {

    }

    public String getZip() {
        return zip;
    }

    // Getters and setters used for signup
    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
