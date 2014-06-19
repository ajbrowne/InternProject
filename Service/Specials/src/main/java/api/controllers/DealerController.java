package api.controllers;

import api.models.Dealer;
import api.repositories.DealerRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by maharb on 6/18/14.
 */
@Controller
@RequestMapping(value="/v1/specials")
public class DealerController {

    @Autowired
    private DealerRepository dealerRepository;
    private Logger log = Logger.getLogger(DealerController.class.getName());

    public DealerController(){
    }

    public DealerController(DealerRepository dealerRepository){
        this.dealerRepository = dealerRepository;
    }

    /**
     * A test function that allows us to create test dealers for testing the app.
     * Mapped to /v1/specials/dealerTest.
     *
     * @param dealer - A dealer object that spring will populate with the data passed in
     * @return - return the dealer we created if successful
     */
    @RequestMapping(value="/dealerTest", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Dealer> createDealer(@RequestBody Dealer dealer){
        double[] first = dealer.getLoc().getCoordinates();
        Point point = new Point(first[0], first[1]);
        dealer.setLocation(point);
        dealerRepository.save(dealer);
        return new ResponseEntity<Dealer>(dealer, HttpStatus.OK);
    }


    /**
     * This function is used to get dealers by location.
     * This is a post version of the function and may be removed if it is
     * determined that the get version is better.
     * Mapped to /v1/specials/dealers
     *
     * @param dealer - A dealer object spring populates with the passed in data
     * @return - Return a list of the nearest dealers to the passed in location
     */
    @RequestMapping(value="/dealers", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<Dealer>> getDealerLoc(@RequestBody Dealer dealer){
        //create an array of doubles using the passed in coordinates
        double[] first = dealer.getLoc().getCoordinates();
        //create a Spring point object using the doubles passed in
        //longitude first and Latitude second.
        Point point = new Point(first[0], first[1]);
        log.info("Location received from app: " + point);
        //Query the Database and return the results to the user
        List<Dealer> newDealer = dealerRepository.getDealerByLocation(point);
        log.info("Number of dealers returned: " + newDealer.size());
        return new ResponseEntity<List<Dealer>>(newDealer, HttpStatus.OK);
    }

    /**
     * This function is used to get dealers by location.
     * Mapped to /v1/specials/dealers
     *
     * @param lng - longitude of users position
     * @param lat - latitude of users position
     * @return - A list of the dealers near the users position - currently nearest 2
     */
    @RequestMapping(value="/dealers", produces = "application/json", params = {"lng", "lat"})
    @ResponseBody
    public ResponseEntity<List<Dealer>> dealerLoc(@RequestParam("lng") double lng, @RequestParam("lat") double lat){
        //Create point object with the latitude and longitude of the user
        Point point = new Point(lng, lat);
        log.info("Location received from app: " + point);
        //Query and return the nearest dealers
        List<Dealer> newDealer = dealerRepository.getDealerByLocation(point);
        log.info("Number of dealers returned: " + newDealer.size());
        return new ResponseEntity<List<Dealer>>(newDealer, HttpStatus.OK);
    }
}
