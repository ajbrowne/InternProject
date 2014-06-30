package com.api.models;

import java.util.List;

/**
 * This object is used to merge some dealer data and special data for
 * use in the android card layouts.
 *
 * Created by maharb on 6/24/14.
 */
public class MergerObj {
    public MergerObj(String dealerName, List<Special> specials) {
        this.dealerName = dealerName;
        this.specials = specials;
    }

    @Override
    public String toString() {
        return "MergerObj{" +
                "dealerName='" + dealerName + '\'' +
                ", specials=" + specials +
                '}';
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

    private String dealerName;
    private List<Special> specials;
}
