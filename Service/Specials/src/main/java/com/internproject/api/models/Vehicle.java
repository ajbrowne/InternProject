package com.internproject.api.models;

import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Vehicle object that stores all of the information about a vehicle.
 *
 * Created by maharb on 6/30/14.
 */
public class Vehicle {

    @Id
    private String id;
    private int year;
    private String make;
    private String model;
    private String trim;
    private int price;
    private String urlImage;
    private String type;
    private List<String> specs;

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Vehicle(String id, int year, String make, String model, String trim, int price, String urlImage) {
        this.id = id;
        this.year = year;
        this.make = make;
        this.model = model;
        this.trim = trim;
        this.price = price;
        this.urlImage = urlImage;
    }


    public Vehicle(String id, int year, String make, String model, String trim, int price, String urlImage, String type, List<String> specs) {
        this.id = id;
        this.year = year;
        this.make = make;
        this.model = model;
        this.trim = trim;
        this.price = price;
        this.urlImage = urlImage;
        this.type = type;
        this.specs = specs;
    }

    public List<String> getSpecs() {
        return specs;
    }

    public void setSpecs(List<String> specs) {
        this.specs = specs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id='" + id + '\'' +
                ", year=" + year +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", trim='" + trim + '\'' +
                ", price=" + price +
                ", urlImage='" + urlImage + '\'' +
                ", type='" + type + '\'' +
                ", specs=" + specs +
                '}';
    }

    public Vehicle(int year, String make, String model, String trim, int price) {
        this.year = year;
        this.make = make;
        this.model = model;
        this.trim = trim;
        this.price = price;
    }

    public Vehicle(){}

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTrim() {
        return trim;
    }

    public void setTrim(String trim) {
        this.trim = trim;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
