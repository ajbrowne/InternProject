package com.internproject.api.models;

import java.util.List;

/**
 * Unused object to store specials and dealers and vehicles would have been added later if needed
 * This would have been used to return results of a key word search but that was never implemented.
 * <p/>
 * Created by maharb on 6/24/14.
 */
public class SpecialsDealers {
    private List<Special> specials;
    private List<Dealer> dealers;

    public SpecialsDealers(List<Special> specials, List<Dealer> dealers) {
        this.specials = specials;
        this.dealers = dealers;
    }

    public SpecialsDealers() {
    }

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
}
