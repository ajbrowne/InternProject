package com.api.controllers;

import com.api.models.MergerObj;
import com.api.services.MergeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    public ResponseEntity<List<MergerObj>> specialLoc(@RequestParam("lng") double lng, @RequestParam("lat") double lat){
        //Create point object with the latitude and longitude of the user
        Point point = new Point(lng, lat);
        log.info("Location received from app: " + point);
        //Query and return the nearest dealers
        return new ResponseEntity<List<MergerObj>>(mergeService.getNearestSpecials(point), HttpStatus.OK);
    }


}
