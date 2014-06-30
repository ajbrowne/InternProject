package com.api.models;

import java.util.List;

/**
 * Created by maharb on 6/24/14.
 */
public class SpecialsDealers {
    @Override
    public String toString() {
        return "SpecialsDealers{" +
                "specials=" + specials +
                ", dealers=" + dealers +
                '}';
    }

    public List<Special> getSpecials() {
        return specials;
    }

    public void setSpecials(List<Special> specials) {
        this.specials = specials;
    }

    public List<Dealer> getDealers() {
        return dealers;
    }

    public void setDealers(List<Dealer> dealers) {
        this.dealers = dealers;
    }

    private List<Special> specials;

    public SpecialsDealers(List<Special> specials, List<Dealer> dealers) {
        this.specials = specials;
        this.dealers = dealers;
    }

    public SpecialsDealers(){}

    private List<Dealer> dealers;
}
