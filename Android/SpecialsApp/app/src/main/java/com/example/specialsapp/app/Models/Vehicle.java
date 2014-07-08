package com.example.specialsapp.app.Models;

import java.util.ArrayList;

/**
 * Created by brownea on 7/7/14.
 */
public class Vehicle {

    private String year;
    private String make;
    private String model;
    private String type;

    private String dealer;

    private ArrayList<Special> specials = new ArrayList<Special>();

    private String url;

    private String name;

    private String price;

    public Vehicle(){}

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVehicleType() {
        return type;
    }

    public void setVehicleType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Special> getSpecials() {
        return specials;
    }

    public void setSpecials(ArrayList<Special> specials) {
        this.specials = specials;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }
}
