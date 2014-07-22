package com.internproject.api.services;

import com.internproject.api.models.Dealer;
import com.internproject.api.repositories.DealerRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for dealer objects. This handles the logic for storing or searching for
 * dealers.
 * <p/>
 * Created by maharb on 6/27/14.
 */
@Service
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

    /**
     * Converts the georesults objects into a list of dealers.
     *
     * @param point - Used to get the dealers by location
     * @return - Converted List of Dealers
     */
    private List<Dealer> getDealersFromGeoresult(Point point) {
        List<GeoResult> locResults = dealerRepository.getDealerByLocation(point);
        List<Dealer> locDealers = new ArrayList<Dealer>();

        //loop over the georesults to convert them to dealers
        for (GeoResult geoResult : locResults) {
            locDealers.add((Dealer) geoResult.getContent());
        }
        return locDealers;
    }

    /**
     * Get a dealer by a specific ID
     *
     * @param id - the id we are looking for
     * @return - the matching dealer
     */
    public Dealer getDealerById(String id) {
        return dealerRepository.getDealerById(id);
    }

    /**
     * Sorts through the dealers found by make and the ones
     * found by location, and returns the dealers that match
     * both criteria.
     * TODO:Come up with a better method name
     * @param dealers - dealers found by make
     * @param locDealers - dealers found by location
     * @return - merged list of dealers
     */
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

    /**
     * Check the list of dealers and return only a list of dealers
     * that match the make that was passed in from the app.
     *
     * TODO:Better method name
     * @param dealers - list of dealers
     * @param dealer - dealer object with the make attribute set to the one being searched for
     * @return - A list of dealers with only the matching makes
     */
    private List<Dealer> dealerCheck(List<Dealer> dealers, Dealer dealer) {
        List<Dealer> tempDealers = new ArrayList<Dealer>();
        for (Dealer temp : dealers) {
            if (dealer.getMake().contains(temp.getMake().get(FIRST_VALUE))) {
                tempDealers.add(temp);
            }
        }
        return tempDealers;
    }

    /**
     * Kind of a helper method used to store the number of specials that
     * each dealer has running at this time.
     *
     * @param id - the id of the dealer that needs that number updated
     */
    public void updateSpecials(String id) {
        Dealer dealer = new Dealer();
        dealer.setId(id);
        dealerRepository.updateDealerSpecials(dealer);
    }

}
