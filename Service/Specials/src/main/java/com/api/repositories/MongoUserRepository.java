package com.api.repositories;

import com.api.models.User;
import org.springframework.dao.DuplicateKeyException;
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
        try{
            mongoTemplate.insert(user, "users");
        }catch(DuplicateKeyException e){
            return null;
        }
        return user;
    }

    @Override
    public User getUser(User user) {
        User temp = mongoTemplate.findOne(Query.query(Criteria.where("username").is(user.getUsername())) ,User.class);
        return temp;
    }
}
