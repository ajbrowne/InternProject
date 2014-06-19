package api.repositories;

import api.models.Dealer;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.NearQuery;

import java.util.List;

/**
 * Created by maharb on 6/18/14.
 */
public class MongoDealerRepository implements DealerRepository {

    private MongoTemplate mongoTemplate;

    public MongoDealerRepository(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List getDealerByLocation(Point point) {
        GeoResults dealers = mongoTemplate.geoNear(NearQuery.near(point).num(2), Dealer.class, "dealers");
        return dealers.getContent();
    }

    @Override
    public Dealer save(Dealer dealer) {
        mongoTemplate.insert(dealer, "dealers");
        return dealer;
    }
}
