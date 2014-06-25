package api.repositories;

import api.models.Dealer;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;

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
        GeoResults dealers = mongoTemplate.geoNear(NearQuery.near(point), Dealer.class, "dealers");
        return dealers.getContent();
    }

    @Override
    public Dealer save(Dealer dealer) {
        mongoTemplate.insert(dealer, "dealers");
        return dealer;
    }

    @Override
    public List getMatchingDealers(Dealer dealer) {
        Query query = new Query();
        System.out.println(dealer);

        if(dealer.getName() != null){
            Criteria criteria = Criteria.where("name").regex(dealer.getName(), "i");
            query.addCriteria(criteria);
        }
        if(dealer.getAdmin() != null){
            Criteria criteria1 = Criteria.where("admin").regex(dealer.getAdmin(), "i");
            query.addCriteria(criteria1);
        }
        if(dealer.getState() != null){
            Criteria criteria2 = Criteria.where("state").regex(dealer.getState(), "i");
            query.addCriteria(criteria2);
        }
        if(dealer.getCity() != null){
            Criteria criteria3 = Criteria.where("city").regex(dealer.getCity(), "i");
            query.addCriteria(criteria3);
        }

        return mongoTemplate.find(query, Dealer.class);
    }
}
