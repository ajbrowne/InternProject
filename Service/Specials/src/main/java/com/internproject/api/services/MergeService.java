package com.internproject.api.services;

import com.internproject.api.controllers.SpecialController;
import com.internproject.api.models.Dealer;
import com.internproject.api.models.MergerObj;
import com.internproject.api.models.Special;
import com.internproject.api.models.Vehicle;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maharb on 6/30/14.
 */
public class MergeService {

    @Autowired
    private SpecialService specialService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private VehicleService vehicleService;
    private Logger log = Logger.getLogger(SpecialController.class.getName());

    public MergeService(SpecialService specialService, DealerService dealerService, VehicleService vehicleService) {
        this.specialService = specialService;
        this.dealerService = dealerService;
        this.vehicleService = vehicleService;
    }

    public MergeService() {
    }

    /**
     * This function finds specials by nearest to the given location
     *
     * @param point - the location we want to search near
     * @return - a list of mergerobj that contains a list of the specials based on dealer name
     */
    public List getNearestSpecials(Point point) {
        List<GeoResult> newDealer = dealerService.getDealerLocation(point);
        List<MergerObj> specials = new ArrayList<MergerObj>();
        //loop over the dealers to find the dealers specials
        for (GeoResult aNewDealer : newDealer) {
            Dealer tempDealer = (Dealer) aNewDealer.getContent();
            Special tempSpecial = new Special();
            tempSpecial.setDealer(tempDealer.getId());
            List<Special> temp = specialService.getSpecials(tempSpecial);
            //store the dealers name and the special in an object to pass to the app
            //dealer name is for the cards in the app.
            if (temp.size() != 0) {
                specials.add(new MergerObj(tempDealer.getName(), temp));
            }
        }
        log.info("Number of specials: " + specials.size());

        return specials;
    }

    /**
     * This function is modified getNearestSpecials that returns the vehicles of the specials found
     *
     * @param point   - the location we want to search near
     * @param vehicle - the vehicle information we are trying to find
     * @return - a list of mergerobj that contains a list of the specials and vehicles by dealer name
     */
    public List getNearestVehicles(Point point, Vehicle vehicle, int flag) {
        List<GeoResult> newDealer = dealerService.getDealerLocation(point);
        List<MergerObj> specials = new ArrayList<MergerObj>();
        List<Vehicle> tempVehicles = vehicleService.getVehicles(vehicle);
        List<String> ids = new ArrayList<String>();
        if (flag == 1) {
            tempVehicles = vehicleHelper(tempVehicles, vehicle);
        }
        //Loop over the vehicles that match the given description and store their ids
        for (Vehicle tempVehicle : tempVehicles) {
            ids.add(tempVehicle.getId());
        }
        //loop over the dealers that are closest to the point to find
        //the specials for that dealer
        for (GeoResult aNewDealer : newDealer) {
            Dealer tempDealer = (Dealer) aNewDealer.getContent();
            Special tempSpecial = new Special();
            tempSpecial.setDealer(tempDealer.getId());
            tempSpecial.setVehicleId(ids);
            List<Special> temp = specialService.getSpecials(tempSpecial);
            //return only specials with the matching vehicles
            if (flag == 1) {
                temp = specialHelper(temp, ids);
            }
            //store the dealers name and the special in an object to pass to the app
            //dealer name is for the cards in the app.
            if (temp.size() != 0) {
                specials.add(new MergerObj(tempDealer.getName(), temp, tempVehicles));
            }
        }

        return specials;
    }

    /**
     * Used to eliminate specials that don't have the matching vehicles
     *
     * @param specials   - the list of specials we are returning to the app
     * @param vehicleIds - a list of the vehicles we are looking through
     * @return a modified list of specials
     */
    private List<Special> specialHelper(List<Special> specials, List<String> vehicleIds) {

        for (int i = 0; i < specials.size(); i++) {
            for (String vehicleId : vehicleIds) {
                if (!listCheck(specials.get(i).getVehicleId(), vehicleId)) {
                    specials.remove(i);
                }
            }
        }

        return specials;
    }

    public List<Vehicle> vehicleHelper(List<Vehicle> vehicles, Vehicle match) {
        int length = vehicles.size();
        for (int i = 0; i < vehicles.size(); i++) {
            if (!vehicles.get(i).getMake().equals(match.getMake())) {
                vehicles.remove(vehicles.get(i));
                length--;
            } else if (!vehicles.get(i).getModel().equals(match.getModel())) {
                vehicles.remove(vehicles.get(i));
                length--;
                if (i >= length) {
                    break;
                }
            }
        }
        return vehicles;
    }

    private boolean listCheck(List<String> ids, String id) {

        for (String temp : ids) {
            if (temp.equals(id)) {
                return true;
            }
        }

        return false;
    }

}
