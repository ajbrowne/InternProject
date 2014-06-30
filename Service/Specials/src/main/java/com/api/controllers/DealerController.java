package com.api.controllers;

import com.api.models.Dealer;
import com.api.services.DealerService;
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
    private DealerService dealerService;
    private Logger log = Logger.getLogger(DealerController.class.getName());

    public DealerController(){
    }

    public DealerController(DealerService dealerService){
        this.dealerService = dealerService;
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
        dealerService.store(dealer);
        return new ResponseEntity<Dealer>(dealer, HttpStatus.OK);
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
        List<Dealer> newDealer = dealerService.getDealerLocation(point);
        log.info("Number of dealers returned: " + newDealer.size());
        return new ResponseEntity<List<Dealer>>(newDealer, HttpStatus.OK);
    }
}
