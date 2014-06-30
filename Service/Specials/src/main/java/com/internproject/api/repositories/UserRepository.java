package com.internproject.api.repositories;

import com.internproject.api.models.User;

/**
 * Created by maharb on 6/18/14.
 */
public interface UserRepository {
    public User save(User user);
    public User getUser(User user);
}
