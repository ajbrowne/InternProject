package com.api.services;

import com.api.models.Dealer;
import com.api.repositories.DealerRepository;
import com.api.concurrency.RunnableQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by maharb on 6/27/14.
 */
public class DealerService {

    @Autowired
    private DealerRepository dealerRepository;

    public DealerService(DealerRepository dealerRepository){
        this.dealerRepository = dealerRepository;
    }

    public DealerService(){}

    public List getDealerLocation(Point point){
        return dealerRepository.getDealerByLocation(point);
    }

    public Dealer store(Dealer dealer){
        return dealerRepository.save(dealer);
    }

    public List getDealers(Dealer dealer){
        List<Dealer> dealers = new ArrayList<Dealer>();

        RunnableQuery mainThread = new RunnableQuery("dealer", dealerRepository, dealer, dealers);
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(mainThread);
        es.shutdown();
        try {
            es.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Query query = new Query();

        if(dealer.getName() != null){
            Criteria criteria = Criteria.where("name").regex(dealer.getName(), "i");
            query.addCriteria(criteria);
        }
        if(dealer.getAdmin() != null){
            Criteria criteria1 = Criteria.where("admin").regex(dealer.getAdmin(), "i");
            query.addCriteria(criteria1);
        }
        if(dealer.getState() != null){
            Criteria criteria2 = Criteria.where("state").regex(dealer.getState(), "i");
            query.addCriteria(criteria2);
        }
        if(dealer.getCity() != null){
            Criteria criteria3 = Criteria.where("city").regex(dealer.getCity(), "i");
            query.addCriteria(criteria3);
        }

        return dealerRepository.getMatchingDealers(dealer, query);
    }

}
