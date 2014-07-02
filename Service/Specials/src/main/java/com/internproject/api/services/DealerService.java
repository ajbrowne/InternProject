package com.internproject.api.services;

import com.internproject.api.concurrency.RunnableQuery;
import com.internproject.api.models.Dealer;
import com.internproject.api.repositories.DealerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;

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

    /**
     * Find a dealer by a given location
     *
     * @param point - the point we are searching near
     * @return - a list of nearby dealers
     */
    public List getDealerLocation(Point point){
        return dealerRepository.getDealerByLocation(point);
    }

    /**
     * Store a new dealer in the database
     *
     * @param dealer - Dealer object we want to store
     * @return - the dealer we just stored
     */
    public Dealer store(Dealer dealer){
        return dealerRepository.save(dealer);
    }

    /**
     * Used to get dealers based on passed in dealer parameters
     *
     * @param dealer - dealer criteria we are searching for
     * @return - a list of the dealers that match
     */
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

        return dealers;
    }

}
