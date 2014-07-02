package com.internproject.api.controllers;

import com.internproject.api.models.Vehicle;
import com.internproject.api.services.MergeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * This controller is used for any api calls that are not specifically accessing
 * the dealer or special collections, but may be accessing both.
 *
 * Created by maharb on 6/24/14.
 */

@Controller
@RequestMapping(value="/v1/specials")
public class CombineController {

    @Autowired
    private MergeService mergeService;
    private Logger log = Logger.getLogger(SpecialController.class.getName());

    public CombineController(MergeService mergeService){
        this.mergeService = mergeService;
    }

    public CombineController(){}

    /**
     * This function is used to get specials by location.
     * Mapped to /v1/specials/special
     *
     * @param lng - longitude of users position
     * @param lat - latitude of users position
     * @return - A list of the dealers near the users position - currently nearest 2
     */
    @RequestMapping(value="/special", produces = "application/json", params = {"lng", "lat"})
    @ResponseBody
    public ResponseEntity<List> specialLoc(@RequestParam("lng") double lng, @RequestParam("lat") double lat){
        //Create point object with the latitude and longitude of the user
        Point point = new Point(lng, lat);
        log.info("Location received from app: " + point);
        //Query and return the nearest dealers
        return new ResponseEntity<List>(mergeService.getNearestSpecials(point), HttpStatus.OK);
    }

    /**
     * The endpoint for doing vehicle search.
     * /v1/specials/vehicle
     *
     * @param lng - longitude of users position
     * @param lat - latitude of users position
     * @param vehicle - the vehicle attributes we are searching by
     * @return - HttpStatus with a list of the specials and vehicles with the dealers name
     */
    @RequestMapping(value = "/vehicle", produces = "application/json", params = {"lng", "lat"})
    @ResponseBody
    public ResponseEntity<List> vehicleLoc(@RequestParam(value = "lng", required = false) double lng, @RequestParam(value = "lat", required = false) double lat, @ModelAttribute Vehicle vehicle){
        Point point = new Point(lng, lat);
        log.info("Vehicle Location received from app: " + point);
        return new ResponseEntity<List>(mergeService.getNearestVehicles(point, vehicle), HttpStatus.OK);
    }


}
