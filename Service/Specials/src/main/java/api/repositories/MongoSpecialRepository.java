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

        Criteria criteria1 = Criteria.where("dealer").is(special.getDealer());
        Criteria criteria2 = Criteria.where("id").is(special.getId());
        Criteria criteria3 = Criteria.where("type").is(special.getType());
        Criteria criteria4 = Criteria.where("title").is(special.getTitle());
        if(special.getDealer() != null){
            query.addCriteria(criteria1);
        }
        if(special.getId() != null){
            query.addCriteria(criteria2);
        }
        if(special.getType() != null){
            query.addCriteria(criteria3);
        }
        if(special.getTitle() != null){
            query.addCriteria(criteria4);
        }

        return mongoTemplate.find(query, Special.class);
    }
}
