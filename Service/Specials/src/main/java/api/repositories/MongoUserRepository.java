package api.repositories;

import api.models.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by maharb on 6/18/14.
 */
public class MongoUserRepository implements UserRepository {

    private MongoTemplate mongoTemplate;

    public MongoUserRepository(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public User save(User user) {
        mongoTemplate.insert(user, "users");
        return user;
    }

    @Override
    public User getUser(User user) {
        return mongoTemplate.findOne(Query.query(Criteria.where("username").is(user.getUsername())) ,User.class);
    }
}
