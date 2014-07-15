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
import java.util.Collections;
import java.util.Comparator;
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
     * This function is modified getNearestVehicles that returns the vehicles of the specials found
     *
     * @param point   - the location we want to search near
     * @param vehicle - the vehicle information we are trying to find
     * @return - a list of mergerobj that contains a list of the specials and vehicles by dealer name
     */
    public List<? extends MergerObj> getNearestVehicles(Point point, Vehicle vehicle, int flag) {
        List<GeoResult> newDealer = dealerService.getDealerLocation(point);
        List<MergerObj> specials = new ArrayList<MergerObj>();
        List<Vehicle> tempVehicles = vehicleService.getAllVehicles();
        List<String> ids = new ArrayList<String>();
        if (flag == 1) {
            tempVehicles = vehicleService.getVehicles(vehicle);
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
        List<Special> matches = new ArrayList<Special>();
        for (Special special : specials) {
            for (String vehicleId : vehicleIds) {
                if (listCheck(special.getVehicleId(), vehicleId)) {
                    matches.add(special);
                    break;
                }
            }
        }

        return matches;
    }

    /**
     * Remove non matching vehicles from the list of initial vehicles
     *
     * @param vehicles - the initial list of vehicles
     * @param match - the the vehicle that will have duplicates in the list
     * @return - a refined list of vehicles with no duplicates
     */
    private List<Vehicle> vehicleHelper(List<Vehicle> vehicles, Vehicle match) {
        int length = vehicles.size();
        //Loop over the vehicles list
        for (int i = 0; i < vehicles.size(); i++) {
            //Check to see if the current location in the list is the matching make
            if (!vehicles.get(i).getMake().equals(match.getMake())) {
                //if it is not then we remove it
                vehicles.remove(vehicles.get(i));
                length--;
                i--;
                //Check to see if the current vehicle has a matching model
            } else if (!vehicles.get(i).getModel().equals(match.getModel())) {
                //if it does not then we remove it
                vehicles.remove(vehicles.get(i));
                length--;
                i--;
                if (i >= length) {
                    break;
                }
            }
        }
        return vehicles;
    }

    /**
     * Check to see if the matching id is in the list
     * This is like the .contains method, we wrote this
     * because the .contains method was not returning the
     * results we were expecting
     *
     * @param ids - a list of ids to be checked
     * @param id - the id we are checking for
     * @return - a boolean based on if the id is in the list
     */
    private boolean listCheck(List<String> ids, String id) {
        for (String temp : ids) {
            if (temp.equals(id)) {
                return true;
            }
        }

        return false;
    }


    /**
     * Service for the api endpoint to return top discounts on vehicles
     *
     * @return - a list of objs containing the vehicles, specials, and dealer name for the top discount
     */
    public List<? extends MergerObj> getTopDiscount() {
        //Create list to return and get all of the specials
        List<MergerObj> mergerObjs = new ArrayList<MergerObj>();
        List specials = specialService.getAllSpecials();

        //Sort the specials from highest amount and take only the first 3 results
        List<Special> sortedSpecials = sortByValue(specials).subList(0,3);
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        List<String> ids = new ArrayList<String>();
        //Get the ids of the vehicles of the top specials
        ids = getTopVehicles(specials, ids);
        //create vehicle objects based on the ids
        for(String id : ids){
            Vehicle vehicle = new Vehicle();
            vehicle.setId(id);
            vehicles.addAll(vehicleService.getVehicles(vehicle));
        }
        //Create the merge objects with out dealer names
        mergerObjs = createMerger(mergerObjs, sortedSpecials, vehicles);
        //Get the dealer names based on the id in the specials and add it to the objects
        for(MergerObj mergerObj :mergerObjs) {
            String dealerName = dealerService.getDealerById(mergerObj.getSpecials().get(0).getDealer()).getName();
            mergerObj.setDealerName(dealerName);
        }
        return mergerObjs;
    }

    /**
     * Create the merger objects that will be returned.
     * This was originally supposed to create the new objs based on the dealers
     * being different but I am not sure it functions completely correctly.
     * (Works for the purpose of demoing)
     *
     * @param mergerObjs - List of the merger objects we are adding too
     * @param specials - List of specials to be added
     * @param vehicles - List of vehicles to be added
     * @return - return the populated list of merger objects
     */
    private List<MergerObj> createMerger(List<MergerObj> mergerObjs, List<Special> specials, List<Vehicle> vehicles) {
        MergerObj mergerObj = new MergerObj();
        //Loop over the specials
        for(Special special : specials){
            //Check to see if there is a current obj or if we need to create the new one
            List<Special> tempSpecials = new ArrayList<Special>();
            if(mergerObjs.size() == 0){
                //Adds first object to the list that will be returned
                tempSpecials.add(special);
                mergerObj.setSpecials(tempSpecials);
                mergerObj.setVehicles(vehicles);
                mergerObjs.add(mergerObj);
            }else{
                //Loops over the current objects in the return list
                for(int i =0;i<mergerObjs.size();i++) {
                    //If the object contains the correct dealer info then the next special is added to this objects
                    //specials list
                    if (mergerObjs.get(i).getSpecials().get(0).getDealer().equals(special.getDealer())) {
                        tempSpecials = mergerObjs.get(i).getSpecials();
                        tempSpecials.add(special);
                        mergerObjs.get(i).setSpecials(tempSpecials);
                        //if the object doesn't contain the same dealer a new merger object is created
                    }else if((i+1)==mergerObjs.size()){
                        tempSpecials.add(special);
                        mergerObj.setSpecials(tempSpecials);
                        mergerObj.setVehicles(vehicles);
                        mergerObjs.add(mergerObj);
                    }
                }
            }
        }
        return mergerObjs;
    }

    /**
     * Method used to get the top 3 vehicles based on the top special discounts
     * The top 3 vehicles could come from one special
     *
     * @param specials - List of top specials
     * @param ids - List of vehicle ids
     * @return - Return a modified list of ids that only contains the top 3
     */
    private List<String> getTopVehicles(List<Special> specials, List<String> ids) {
        //Loop over the specials to compare ids
        for(Special special: specials){
            //if the size of the list of ids found is greater than 3 exit the loop
            if(ids.size() >= 3){
                break;
            }
            //Check the number of vehicles in the first special
            //If it is greater than 3 then we just grab the first 3 and return
            if(special.getVehicleId().size() > 3){
                ids.addAll(special.getVehicleId().subList(0,3));
                break;
                //if it is less than 3 then we add them all and continue in the loop
            }else if(special.getVehicleId().size() < 3){
                ids.addAll(special.getVehicleId());
                //if it is exactly 3 then we get those and return
            }else if(special.getVehicleId().size() == 3){
                ids.addAll(special.getVehicleId());
                break;
            }
        }
        //Catch just in case too many vehicle ids get added to the list
        ids = ids.subList(0,3);
        return ids;
    }

    /**
     * Comparator for sorting a list of specials by the amount that is stored
     *
     * @param all - All of the specials we have
     * @return - return the sorted list of specials
     */
    private List<Special> sortByValue(List<Special> all) {
        Collections.sort(all, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Special first = (Special) o1;
                Special second = (Special) o2;
                if (Integer.parseInt(first.getAmount()) < Integer.parseInt(second.getAmount())) {
                    return 1;
                }
                return 0;
            }
        });
        return all;
    }
}
