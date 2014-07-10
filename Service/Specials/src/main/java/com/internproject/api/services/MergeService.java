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
        Map<String, Integer> all = new HashMap<String, Integer>();

        for(Special special: specials){
            all.put(special.getId(), Integer.parseInt(special.getAmount()));
        }

        List<Special> sortedSpecials = new ArrayList<Special>();
        sortSpecialsList(specials, all, sortedSpecials);
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        List<String> ids = new ArrayList<String>();
        ids = getTopVehicles(specials, ids);
        for(String id : ids){
            Vehicle vehicle = new Vehicle();
            vehicle.setId(id);
            vehicles.addAll(vehicleService.getVehicles(vehicle));
        }

        createMerger(mergerObjs, sortedSpecials, vehicles);

        for(MergerObj mergerObj :mergerObjs){
            String dealerName = dealerService.getDealerById(mergerObj.getSpecials().get(0).getDealer()).getName();
            mergerObj.setDealerName(dealerName);
        }


        return mergerObjs;
    }

    private void sortSpecialsList(List<Special> specials, Map<String, Integer> all, List<Special> sortedSpecials) {
        Map<String, Integer> top = sortByValue(all);
        Set<String> specialIds = top.keySet();
        List<String> listIds = new ArrayList<String>();
        for(String id : specialIds){
            listIds.add(id);
        }
        for(Special special : specials){
            if(listIds.get(0).equals(special.getId())){
                sortedSpecials.add( special);
            }else if(listIds.get(1).equals(special.getId())){
                sortedSpecials.add( special);
            }else if(listIds.get(2).equals(special.getId())){
                sortedSpecials.add( special);
            }
        }
    }

    private void createMerger(List<MergerObj> mergerObjs, List<Special> specials, List<Vehicle> vehicles) {
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

    private Map<String, Integer> sortByValue(Map<String, Integer> all) {
        List list = new LinkedList(all.entrySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        Map sorted = new LinkedHashMap();
        for (Object aList : list) {
            Map.Entry entry = (Map.Entry) aList;
            sorted.put(entry.getKey(), entry.getValue());
        }
        return sorted;
    }
}
