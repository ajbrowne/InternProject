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

import java.util.*;

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
    public List<? extends MergerObj> getNearestSpecials(Point point) {
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

    private List<Vehicle> vehicleHelper(List<Vehicle> vehicles, Vehicle match) {
        int length = vehicles.size();
        for (int i = 0; i < vehicles.size(); i++) {
            if (!vehicles.get(i).getMake().equals(match.getMake())) {
                vehicles.remove(vehicles.get(i));
                length--;
                i--;
            } else if (!vehicles.get(i).getModel().equals(match.getModel())) {
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

    private boolean listCheck(List<String> ids, String id) {

        for (String temp : ids) {
            if (temp.equals(id)) {
                System.out.println("TEMP: " + temp + " ID: " + id);
                return true;
            }
        }

        return false;
    }

    public List getTopDiscount() {
        List<MergerObj> mergerObjs = new ArrayList<MergerObj>();
        List<Special> specials = specialService.getAllSpecials();

        List<Special> sortedSpecials = new ArrayList<Special>();
        //sortedSpecials = sortSpecialsList(specials, specials, sortedSpecials);
        System.out.println(sortedSpecials);
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        List<String> ids = new ArrayList<String>();
        ids = getTopVehicles(specials, ids);
        for(String id : ids){
            Vehicle vehicle = new Vehicle();
            vehicle.setId(id);
            vehicles.addAll(vehicleService.getVehicles(vehicle));
        }

        mergerObjs = createMerger(mergerObjs, sortedSpecials, vehicles);

        for(MergerObj mergerObj :mergerObjs){
            String dealerName = dealerService.getDealerById(mergerObj.getSpecials().get(0).getDealer()).getName();
            mergerObj.setDealerName(dealerName);
        }


        return mergerObjs;
    }

//    private List<Special> sortSpecialsList(List<Special> specials, List<Special> all, List<Special> sortedSpecials) {
//        List<Special> top = sortByValue(all);
//        List<String> listIds = new ArrayList<String>();
//        for(String id : specialIds){
//            listIds.add(id);
//        }
//        for(Special special : specials){
//            if(listIds.get(0).equals(special.getId())){
//                sortedSpecials.add( special);
//            }else if(listIds.get(1).equals(special.getId())){
//                sortedSpecials.add( special);
//            }else if(listIds.get(2).equals(special.getId())){
//                sortedSpecials.add( special);
//            }
//        }
//        return sortedSpecials;
//    }

    private List<MergerObj> createMerger(List<MergerObj> mergerObjs, List<Special> specials, List<Vehicle> vehicles) {
        for(Special special : specials){
            MergerObj mergerObj = new MergerObj();
            List tempSpecials = new ArrayList();
            for(int i =0;i<mergerObjs.size();i++) {
                if (mergerObjs.get(i).getSpecials().get(0).getDealer().equals(special.getDealer())) {
                    tempSpecials = mergerObjs.get(i).getSpecials();
                    tempSpecials.add(special);
                    mergerObjs.get(i).setSpecials(tempSpecials);
                }else if((i+1)==mergerObjs.size()){
                    tempSpecials.add(special);
                    mergerObj.setSpecials(tempSpecials);
                    mergerObj.setVehicles(vehicles);
                    mergerObjs.add(mergerObj);
                }
            }
        }
        return mergerObjs;
    }

    private List<String> getTopVehicles(List<Special> specials, List<String> ids) {
        for(Special special: specials){
            if(ids.size() >= 3){
                break;
            }
            if(special.getVehicleId().size() > 3){
                ids.addAll(special.getVehicleId().subList(0,2));
                break;
            }else if(special.getVehicleId().size() < 3){
                ids.addAll(special.getVehicleId());
            }else if(special.getVehicleId().size() == 3){
                ids.addAll(special.getVehicleId());
                break;
            }
        }
        ids = ids.subList(0,2);
        return ids;
    }

    private List<Special> sortByValue(List<Special> all) {

        Collections.sort(all, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Special first = (Special)o1;
                Special second = (Special)o2;
                if(Integer.parseInt(first.getAmount())>Integer.parseInt(second.getAmount())){
                    return 1;
                }
                return 0;
            }
        });

        return all;
    }
}
