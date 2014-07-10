package com.internproject.api.repositories;

import com.internproject.api.models.Dealer;
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
    public void updateDealerSpecials(Dealer dealer);
    public List<Dealer> findAllDealers();
    public Dealer getDealerById(String id);
}
