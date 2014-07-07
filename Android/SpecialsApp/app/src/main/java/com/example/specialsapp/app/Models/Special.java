package com.example.specialsapp.app.Models;

import android.graphics.Bitmap;

/**
 * Created by brownea on 6/20/14.
 **/
public class Special {

    private String title;
    private String description;
    private String type;
    private String dealer;
    private int price = -1000;
    private String amount;

    private String url;

    public Special(){}

    public Special(String title, String description, String type, String dealer, String amount){
        this.title = title;
        this.description = description;
        this.type = type;
        this.dealer = dealer;
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Special{" +
                "dealer='" + dealer + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
