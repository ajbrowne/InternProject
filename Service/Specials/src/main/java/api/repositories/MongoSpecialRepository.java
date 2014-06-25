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

        if(special.getDealer() != null){
            Criteria criteria1 = Criteria.where("dealer").regex(special.getDealer(), "i");
            query.addCriteria(criteria1);
        }
        if(special.getId() != null){
            Criteria criteria2 = Criteria.where("id").is(special.getId());
            query.addCriteria(criteria2);
        }
        if(special.getType() != null){
            Criteria criteria3 = Criteria.where("type").regex(special.getType(), "i");
            query.addCriteria(criteria3);
        }
        if(special.getTitle() != null){
            Criteria criteria4 = Criteria.where("title").regex(special.getTitle(), "i");
            query.addCriteria(criteria4);
        }
        if(special.getDescription() != null){
            Criteria criteria6 = Criteria.where("description").regex(special.getDescription(), "i");
            query.addCriteria(criteria6);
        }
        if(special.getAmount() != null){
            Criteria criteria5 = Criteria.where("amount").regex(special.getAmount(), "i");
            query.addCriteria(criteria5);
        }

        return mongoTemplate.find(query, Special.class);
    }
}
