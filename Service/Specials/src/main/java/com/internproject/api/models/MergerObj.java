package com.internproject.api.models;

import java.util.List;

/**
 * This object is used to merge some dealer data and special data for
 * use in the android card layouts.
 * <p/>
 * Created by maharb on 6/24/14.
 */
public class MergerObj {
    private String dealerName;
    private List<Special> specials;
    private List<Vehicle> vehicles;

    public MergerObj(String dealerName, List<Special> specials, List<Vehicle> vehicles) {
        this.dealerName = dealerName;
        this.specials = specials;
        this.vehicles = vehicles;
    }

    public MergerObj(String dealerName, List<Special> specials) {
        this.dealerName = dealerName;
        this.specials = specials;
    }

    public MergerObj() {
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public List<Special> getSpecials() {
        return specials;
    }

    public void setSpecials(List<Special> specials) {
        this.specials = specials;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public String toString() {
        return "MergerObj{" +
                "dealerName='" + dealerName + '\'' +
                ", specials=" + specials +
                ", vehicles=" + vehicles +
                '}';
    }
}
