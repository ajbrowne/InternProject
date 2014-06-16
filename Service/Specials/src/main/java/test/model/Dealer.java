package test.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by maharb on 6/12/14.
 */
@Document(collection="dealers")
public class Dealer {

    @Id
    private String id;
    private String name;
    private String admin_id;
    private String location;

}
