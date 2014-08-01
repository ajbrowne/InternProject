package com.internproject.api.repositories;

import com.internproject.api.models.Dealer;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Repository layer for dealers
 * <p/>
 * Created by maharb on 6/18/14.
 */
public class MongoDealerRepository implements DealerRepository {

    private MongoTemplate mongoTemplate;

    public MongoDealerRepository(MongoTemplate mongoTemplate) {
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
    public List getMatchingDealers(Dealer dealer, Query query) {

        return mongoTemplate.find(query, Dealer.class);
    }

    @Override
    public List<Dealer> findAllDealers() {
        return mongoTemplate.findAll(Dealer.class, "dealers");
    }

    @Override
    public Dealer getDealerById(String id) {
        List<Dealer> allDealers = findAllDealers();
        for(Dealer dealer: allDealers){
            if(dealer.getId().equals(id)){
                return dealer;
            }
        }
        return null;
    }

    @Override
    public synchronized void updateDealerSpecials(Dealer dealer) {
        Dealer temp = mongoTemplate.findOne(Query.query(Criteria.where("id").is(dealer.getId())), Dealer.class);
        temp.setNumSpecials(temp.getNumSpecials() + 1);
        mongoTemplate.insert(temp, "dealers");
    }

}
