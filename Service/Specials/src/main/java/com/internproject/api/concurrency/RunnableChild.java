package com.internproject.api.concurrency;

import com.internproject.api.models.Dealer;
import com.internproject.api.models.Special;
import com.internproject.api.models.Vehicle;
import com.internproject.api.repositories.DealerRepository;
import com.internproject.api.repositories.SpecialRepository;
import com.internproject.api.repositories.VehicleRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Child Thread that manages the creation of queries to query the mongo db.
 * Uses thread names as a means of determining what to put in the query.
 *
 * Created by maharb on 6/27/14.
 */
public class RunnableChild implements Runnable {

    private List<Dealer> dealers;
    private Dealer dealer;
    private DealerRepository dealerRepository;
    private SpecialRepository specialRepository;
    private String name;
    private String parentName;
    private Special special;
    private List<Special> specials;
    private Vehicle vehicle;
    private List<Vehicle> vehicles;
    private VehicleRepository vehicleRepository;

    /*
     * Constructor for specials threads
     */
    public RunnableChild(String name, String parentName, SpecialRepository specialRepository, Special special, List<Special> specials){
        this.name = name;
        this.specialRepository = specialRepository;
        this.special = special;
        this.specials = specials;
        this.parentName = parentName;
    }

    /*
     * Constructor for dealer threads
     */
    public RunnableChild(String name, String parentName, DealerRepository dealerRepository, Dealer dealer, List<Dealer> dealers){
        this.name = name;
        this.dealerRepository = dealerRepository;
        this.dealer = dealer;
        this.dealers = dealers;
        this.parentName = parentName;
    }

    /*
     * Constructor for vehicle threads
     */
    public RunnableChild(String name, String parentName, VehicleRepository vehicleRepository, Vehicle vehicle, List<Vehicle> vehicles){
        this.name = name;
        this.vehicleRepository = vehicleRepository;
        this.vehicle = vehicle;
        this.vehicles = vehicles;
        this.parentName = parentName;
    }


    /**
     * run the threads based on the name of the parent thread.
     */
    @Override
    public void run() {
        if(parentName.equals("special")){
            //Run if this is for specials search
            specialSearch();
        }else if(parentName.equals("dealer")){
            //Run if this is for dealers search
            dealerSearch();
        }else if(parentName.equals("vehicle")){
            //Run if this is for vehicles search
            vehicleSearch();
        }


    }

    /**
     * Determine which query should be run for vehicles
     */
    private void vehicleSearch() {
        Query query = new Query();
        List temp = new ArrayList<Vehicle>();
        //Queries are created differently for the different kind of
        //search
        if(name.equals("id") && vehicle.getId() != null){
            temp = runQuery(name, vehicle.getId(), query,0);
        }else if(name.equals("year") && vehicle.getYear() != 0){
            temp = runQuery(name, null, query, vehicle.getYear());
        }else if(name.equals("make") && vehicle.getMake() != null){
            temp = runQuery(name, vehicle.getMake(), query, 0);
        }else if(name.equals("model") && vehicle.getModel() != null){
            temp = runQuery(name, vehicle.getModel(), query,0);
        }else if(name.equals("trim") && vehicle.getTrim() != null){
            temp = runQuery(name, vehicle.getTrim(), query,0);
        }else if(name.equals("specs") && vehicle.getSpecs() != null){
            for(int i = 0; i < vehicle.getSpecs().size();i++){
                temp.addAll(runQuery(name, vehicle.getSpecs().get(i), query, 0));
            }
        }else if(name.equals("type") && vehicle.getType() != null){
            temp = runQuery(name, vehicle.getType(), query,0);
        }


        //Store the results in a global arraylist
        //synchronize to prevent race conditions
        synchronized (RunnableChild.class){
            vehicles.addAll(temp);
            //remove duplicates
            duplicateCheckVehicles(vehicles);
        }
    }

    /**
     * Determine which query should be run for dealers
     */
    private void dealerSearch() {
        Query query = new Query();
        List temp = new ArrayList<Dealer>();
        //run query based on kind of query needed for each field
        if(dealer.getId() != null && name.equals("id")){
            temp = runQuery(name, dealer.getId(), query, 0);
        }else if(dealer.getName() != null && name.equals("name")){
            temp = runQuery(name, dealer.getName(), query, 0);
        }else if(dealer.getState() != null && name.equals("state")){
            temp = runQuery(name, dealer.getState(), query,0);
        }else if(dealer.getCity() != null && name.equals("city")){
            temp = runQuery(name, dealer.getCity(), query,0);
        }else if(dealer.getAdmin() != null && name.equals("admin")){
            temp = runQuery(name, dealer.getAdmin(), query,0);
        }else if(dealer.getMake() != null && name.equals("make")){
            for(int i = 0; i < dealer.getMake().size();i++){
                temp.addAll(runQuery(name, dealer.getMake().get(i), query, 0));
            }
        }

        //Store the results in a global arraylist
        //synchronize to prevent race conditions
        synchronized (RunnableChild.class){
            dealers.addAll(temp);
            //remove duplicates
            duplicateCheckDealers(dealers);
        }

    }

    /**
     * Create the query based on the different kinds of criteria
     * Criteria changes based on what we are searching for
     *
     * @param queryType - the name of the thread which determines the kind of query needed
     * @param value - the value that we are going to be querying for
     * @param query - the query object
     * @param optional - an optional value if an int is being queried
     * @return - A list of the resulting query
     */
    private List runQuery(String queryType, String value, Query query, int optional) {
        Criteria criteria;
        //create query for values that we need an exact result
        if (queryType.equals("id") || queryType.equals("year")) {
            if (optional != 0) {
                criteria = Criteria.where(queryType).is(optional);
                query.addCriteria(criteria);
            } else {
                criteria = Criteria.where(queryType).is(value);
                query.addCriteria(criteria);
            }
        //create query for searching in an array in the document
        }else if(queryType.equals("specs")){
            criteria = Criteria.where(value).in("specs");
            query.addCriteria(criteria);
        //create query for all other cases that looks for a keyword
        }else if(queryType.equals("vehicleId")){
            criteria = Criteria.where(value).in("vehicleId");
            query.addCriteria(criteria);
            //create query for all other cases that looks for a keyword
        }else if(queryType.equals("make")){
            criteria = Criteria.where("make").all(value);
            query.addCriteria(criteria);
            //create query for all other cases that looks for a keyword
        }else{
            criteria = Criteria.where(queryType).regex(".*" + value + ".*", "i");
            query.addCriteria(criteria);
        }

        //run the query in the correct repository based on parent thread name
        if(parentName.equals("special")){
            return specialRepository.findMatching(special, query);
        }else if(parentName.equals("dealer")) {
            return dealerRepository.getMatchingDealers(dealer, query);
        }
        return vehicleRepository.getVehicles(vehicle, query);
    }

    /**
     * Overly duplicated duplicate checks because I couldn't get the generic version to work
     * @param specialsCheck - list of specials to check for duplicates
     */
    private void duplicateCheck(List<Special> specialsCheck){
        for(int i=0;i<specialsCheck.size();i++){
            for(int j=i+1;j<specialsCheck.size();j++){
                if(specialsCheck.get(i).getId().equals(specialsCheck.get(j).getId())){
                    specialsCheck.remove(j);
                }
            }
        }
    }

    /**
     * Overly duplicated duplicate checks because I couldn't get the generic version to work
     * @param dealersCheck - list of dealers to check for duplicates
     */
    private void duplicateCheckDealers(List<Dealer> dealersCheck){
        for(int i=0;i<dealersCheck.size();i++){
            for(int j=i+1;j<dealersCheck.size();j++){
                if(dealersCheck.get(i).getId().equals(dealersCheck.get(j).getId())){
                    dealersCheck.remove(j);
                }
            }
        }
    }

    /**
     * Overly duplicated duplicate checks because I couldn't get the generic version to work
     * @param vehiclesCheck - list of vehicles to check for duplicates
     */
    private void duplicateCheckVehicles(List<Vehicle> vehiclesCheck){
        for(int i=0;i<vehiclesCheck.size();i++){
            for(int j=i+1;j<vehiclesCheck.size();j++){
                if(vehiclesCheck.get(i).getId().equals(vehiclesCheck.get(j).getId())){
                    vehiclesCheck.remove(j);
                }
            }
        }
    }

    /**
     * Determine which query should be run for specials
     */
    private void specialSearch(){
        Query query = new Query();
        List temp = new ArrayList<Special>();
        //create each query based on kind of query needed for each field
        if(special.getDealer() != null && name.equals("dealer")){
            temp = runQuery(name, special.getDealer(), query,0);
        }
        if(special.getId() != null && name.equals("id")){
            temp = runQuery(name, special.getId(), query,0);
        }
        if(special.getTitle() != null && name.equals("title")){
            temp = runQuery(name, special.getTitle(), query,0);
        }
        if(special.getAmount() != null && name.equals("amount")){
            temp = runQuery(name, special.getAmount(), query,0);
        }
        if(special.getVehicleId() != null && name.equals("vehicleId")){
            for(int i = 0; i < special.getVehicleId().size();i++){
                temp.addAll(runQuery(name, special.getVehicleId().get(i), query, 0));
            }
        }

        //Store the results in a global arraylist
        //synchronize to prevent race conditions
        synchronized (RunnableChild.class){
            specials.addAll(temp);
            //remove duplicates
            duplicateCheck(specials);
        }
    }
}
