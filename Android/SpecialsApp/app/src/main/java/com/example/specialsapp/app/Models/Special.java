package com.example.specialsapp.app.Models;

/**
 * Created by brownea on 6/20/14.
 */
public class Special {

    private String title;
    private String description;
    private String type;
    private String dealer;

    public Special(){}

    public Special(String title, String description, String type, String dealer){
        this.title = title;
        this.description = description;
        this.type = type;
        this.dealer = dealer;
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
