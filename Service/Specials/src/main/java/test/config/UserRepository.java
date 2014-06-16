package test.config;

import org.springframework.data.repository.CrudRepository;
import test.model.User;

/**
 * Created by maharb on 6/11/14.
 */
public interface UserRepository extends CrudRepository<User, String> {

    public User findByUsername(String username);
}