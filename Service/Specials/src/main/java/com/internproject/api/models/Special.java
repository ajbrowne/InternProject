package com.internproject.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Special Object that stores all of the information about specials
 * Note: For some reason at the very beginning we made amount a String
 * and never bothered to change it over and it just seems annoying to do
 * now so deal with it.
 * <p/>
 * Created by maharb on 6/18/14.
 */
@Document(collection = "special")
public class Special {

    @Id
    private String id;
    private String title;
    private String dealer;
    private String amount;
    private int status;
    private Date startDate;
    private Date endDate;
    private boolean trending;
    private List<String> vehicleId;

    public Special(String id, String title, String dealer, String amount, int status, Date startDate, Date endDate, boolean trending, List<String> vehicleId) {
        this.id = id;
        this.title = title;
        this.dealer = dealer;
        this.amount = amount;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.trending = trending;
        this.vehicleId = vehicleId;
    }

    public Special() {
    }

    @Override
    public String toString() {
        return "Special{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", dealer='" + dealer + '\'' +
                ", amount='" + amount + '\'' +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", trending=" + trending +
                ", vehicleId=" + vehicleId +
                '}';
    }

    public boolean isTrending() {
        return trending;
    }

    public void setTrending(boolean trending) {
        this.trending = trending;
    }

    public List<String> getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(List<String> vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


}
