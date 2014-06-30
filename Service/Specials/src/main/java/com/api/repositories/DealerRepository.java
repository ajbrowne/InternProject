package com.api.repositories;

import com.api.models.Dealer;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by maharb on 6/18/14.
 */
public interface DealerRepository {
    public List getDealerByLocation(Point point);
    public Dealer save(Dealer dealer);
    public List getMatchingDealers(Dealer dealer, Query query);
}
