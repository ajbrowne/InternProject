package com.internproject.api.repositories;

import com.internproject.api.models.User;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository Layer for Users
 * <p/>
 * Created by maharb on 6/18/14.
 */
@Repository
public class MongoUserRepository implements UserRepository {

    private MongoTemplate mongoTemplate;

    public MongoUserRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public User save(User user) {
        try {
            mongoTemplate.insert(user, "users");
        } catch (DuplicateKeyException e) {
            return null;
        }
        return user;
    }

    @Override
    public User getUser(User user) {
        return mongoTemplate.findOne(Query.query(Criteria.where("username").is(user.getUsername())), User.class);
    }
}
