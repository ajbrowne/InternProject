package com.api.repositories;

import com.api.models.User;

/**
 * Created by maharb on 6/18/14.
 */
public interface UserRepository {
    public User save(User user);
    public User getUser(User user);
}
