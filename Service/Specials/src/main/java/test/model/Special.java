package test.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by maharb on 6/12/14.
 */
@Document
public class Special {

    @Id
    private String id;
    private String title;
    private String dealer_id;
    private String type;
    private int amount;
}
