package com.api.repositories;

import com.api.models.Special;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by maharb on 6/18/14.
 */
public interface SpecialRepository {

    public Special save(Special special);

    public List findAll();

    public List findMatching(Special special, Query query);
}
