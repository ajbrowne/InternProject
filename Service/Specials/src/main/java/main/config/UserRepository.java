package main.config;

import main.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by maharb on 6/11/14.
 */
public interface UserRepository extends MongoRepository<User, String> {

    public User findByUsername(String username);
}