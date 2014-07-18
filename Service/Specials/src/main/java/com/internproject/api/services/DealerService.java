package com.internproject.api.services;

import com.internproject.api.concurrency.RunnableQuery;
import com.internproject.api.models.Dealer;
import com.internproject.api.repositories.DealerRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Service layer for dealer objects. This handles the logic for storing or searching for
 * dealers.
 * <p/>
 * Created by maharb on 6/27/14.
 */
public class DealerService {

    @Autowired
    private DealerRepository dealerRepository;
    private Logger log = Logger.getLogger(DealerService.class.getName());
    private static int FIRST_VALUE = 0;
    public DealerService(DealerRepository dealerRepository) {
        this.dealerRepository = dealerRepository;
    }

    public DealerService() {
    }

    /**
     * Find a dealer by a given location
     *
     * @param point - the point we are searching near
     * @return - a list of nearby dealers
     */
    public List getDealerLocation(Point point) {
        return getDealersFromGeoresult(point);
    }

    /**
     * Store a new dealer in the database
     *
     * @param dealer - Dealer object we want to store
     * @return - the dealer we just stored
     */
    public Dealer store(Dealer dealer) {
        return dealerRepository.save(dealer);
    }

    /**
     * Used to get dealers based on passed in dealer parameters
     *
     * @param dealer - dealer criteria we are searching for
     * @return - a list of the dealers that match
     */
    public List<? extends Dealer> getDealers(Point point, Dealer dealer) {
        List<Dealer> dealers;
        List<Dealer> locDealers = getDealersFromGeoresult(point);
        if (dealer.getMake() != null && !dealer.getMake().get(FIRST_VALUE).equals("Any")) {
            dealers = dealerRepository.findAllDealers();
            dealers = dealerCheck(dealers, dealer);
        } else {
            dealers = dealerRepository.findAllDealers();
        }
        dealers = dealerSort(dealers, locDealers);


        return dealers;
    }

    private List<Dealer> getDealersFromGeoresult(Point point) {
        List<GeoResult> locResults = dealerRepository.getDealerByLocation(point);
        List<Dealer> locDealers = new ArrayList<Dealer>();

        for (GeoResult geoResult : locResults) {
            locDealers.add((Dealer) geoResult.getContent());
        }
        return locDealers;
    }

    public Dealer getDealerById(String id) {
        return dealerRepository.getDealerById(id);
    }

    private List<Dealer> dealerSort(List<Dealer> dealers, List<Dealer> locDealers) {
        List<Dealer> temp = new ArrayList<Dealer>();
        for (Dealer dealer : locDealers) {
            for (Dealer tempDealer : dealers) {
                if (dealer.getId().equals(tempDealer.getId())) {
                    temp.add(dealer);
                    break;
                }
            }
        }

        return temp;
    }

    private List<Dealer> dealerCheck(List<Dealer> dealers, Dealer dealer) {
        List<Dealer> tempDealers = new ArrayList<Dealer>();
        for (Dealer temp : dealers) {
            if (dealer.getMake().contains(temp.getMake().get(FIRST_VALUE))) {
                tempDealers.add(temp);
            }
        }
        return tempDealers;
    }

    public void updateSpecials(String id) {
        Dealer dealer = new Dealer();
        dealer.setId(id);
        dealerRepository.updateDealerSpecials(dealer);
    }

}
