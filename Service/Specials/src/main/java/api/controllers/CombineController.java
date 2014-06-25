package api.controllers;

import api.models.Dealer;
import api.models.MergerObj;
import api.models.Special;
import api.models.SpecialsDealers;
import api.repositories.DealerRepository;
import api.repositories.SpecialRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
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
    private SpecialRepository specialRepository;
    @Autowired
    private DealerRepository dealerRepository;
    private Logger log = Logger.getLogger(SpecialController.class.getName());

    public CombineController(SpecialRepository specialRepository, DealerRepository dealerRepository){
        this.specialRepository = specialRepository;
        this.dealerRepository = dealerRepository;
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
        List<GeoResult> newDealer = dealerRepository.getDealerByLocation(point);
        List<MergerObj> specials = new ArrayList<MergerObj>();
        //loop over the dealers to find the dealers specials
        for(int i = 0; i < newDealer.size();i++){
            Dealer tempDealer = (Dealer)newDealer.get(i).getContent();
            Special tempSpecial = new Special();
            tempSpecial.setDealer(tempDealer.getId());
            List<Special> temp = specialRepository.findMatching(tempSpecial);
            //store the dealers name and the special in an object to pass to the app
            //dealer name is for the cards in the app.
            if(temp.size() != 0) {
                specials.add(new MergerObj(tempDealer.getName(), temp));
            }
        }
        log.info("Number of specials: " + specials.size());
        return new ResponseEntity<List<MergerObj>>(specials, HttpStatus.OK);
    }

    @RequestMapping(value = "/keyword", produces = "application/json", params = {"keyword"})
    @ResponseBody
    public ResponseEntity<SpecialsDealers> keyword(@RequestParam("keyword")String word){
        SpecialsDealers total = new SpecialsDealers();
        List<Special> theSpecials = specialRepository.findMatching(new Special(word));
        List<Dealer> theDealers = dealerRepository.getMatchingDealers(new Dealer(word));
        total.setDealers(theDealers);
        total.setSpecials(theSpecials);
        return new ResponseEntity<SpecialsDealers>(total, HttpStatus.OK);
    }
}
