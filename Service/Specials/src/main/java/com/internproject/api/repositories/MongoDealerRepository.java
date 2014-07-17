package com.internproject.api.repositories;

import com.internproject.api.models.Dealer;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
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
        List returnDealers = new ArrayList<Dealer>();
        for(int i = 0; i < dealers.getContent().size();i++){
            returnDealers.add(dealers.getContent().get(i));
        }
        return returnDealers;
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
        return mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), Dealer.class, "dealers");
    }

    @Override
    public synchronized void updateDealerSpecials(Dealer dealer) {
        Dealer temp = mongoTemplate.findOne(Query.query(Criteria.where("id").is(dealer.getId())), Dealer.class);
        temp.setNumSpecials(temp.getNumSpecials() + 1);
        System.out.println("SPECIALS: " + temp);
        mongoTemplate.insert(temp, "dealers");
    }

}
