package api.repositories;

import api.models.Special;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by maharb on 6/18/14.
 */
public class MongoSpecialRepository implements SpecialRepository {

    private MongoTemplate mongoTemplate;

    public MongoSpecialRepository(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Special save(Special special) {

        mongoTemplate.insert(special, "special");
        return special;
    }

    @Override
    public List findAll() {
        return mongoTemplate.findAll(Special.class);
    }

    @Override
    public List<Special> findMatching(Special special) {
        Query query = new Query();
        Criteria criteria = new Criteria().orOperator(Criteria.where("title").is(special.getTitle()),
                Criteria.where("type").is(special.getType()),
                Criteria.where("id").is(special.getId()),
                Criteria.where("dealer").is(special.getDealer()));
        query.addCriteria(criteria);
        return mongoTemplate.find(query, Special.class);
    }
}
