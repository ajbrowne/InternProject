package api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by maharb on 6/18/14.
 */
@Document(collection = "special")
public class Special{

    @Id
    private String id;

    public Special(){}

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public String toString() {
        return "Special{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", dealer='" + dealer + '\'' +
                ", type='" + type + '\'' +
                ", amount='" + amount + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }


    private String title;
    private String dealer;
    private String type;
    private String amount;
    private String description;
    private int status;
    private Date startDate;

    public Special(String title, String dealer, String type, String amount, String description, int status, Date startDate, Date endDate) {
        this.title = title;
        this.dealer = dealer;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private Date endDate;

}
