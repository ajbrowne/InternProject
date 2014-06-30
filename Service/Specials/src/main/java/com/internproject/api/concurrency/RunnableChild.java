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

    public RunnableChild(String name, String parentName, SpecialRepository specialRepository, Special special, List<Special> specials){
        this.name = name;
        this.specialRepository = specialRepository;
        this.special = special;
        this.specials = specials;
        this.parentName = parentName;
    }

    public RunnableChild(String name, String parentName, DealerRepository dealerRepository, Dealer dealer, List<Dealer> dealers){
        this.name = name;
        this.dealerRepository = dealerRepository;
        this.dealer = dealer;
        this.dealers = dealers;
        this.parentName = parentName;
    }

    public RunnableChild(String name, String parentName, VehicleRepository vehicleRepository, Vehicle vehicle, List<Vehicle> vehicles){
        this.name = name;
        this.vehicleRepository = vehicleRepository;
        this.vehicle = vehicle;
        this.vehicles = vehicles;
        this.parentName = parentName;
    }



    @Override
    public void run() {
        if(parentName.equals("special")){
            specialSearch();
        }else if(parentName.equals("dealer")){
            dealerSearch();
        }else if(parentName.equals("vehicle")){
            vehicleSearch();
        }


    }

    private void vehicleSearch() {
        Query query = new Query();
        List<Vehicle> temp = new ArrayList<Vehicle>();
        if(vehicle.getId() != null && name.equals("id")){
            temp = runQuery(name, dealer.getId(), query,0);
        }else if(vehicle.getYear() != 0 && name.equals("year")){
            temp = runQuery(name, null, query, vehicle.getYear());
        }else if(vehicle.getMake() != null && name.equals("make")){
            temp = runQuery(name, vehicle.getMake(), query, 0);
        }else if(vehicle.getModel() != null && name.equals("model")){
            temp = runQuery(name, vehicle.getModel(), query,0);
        }else if(vehicle.getTrim() != null && name.equals("trim")){
            temp = runQuery(name, vehicle.getTrim(), query,0);
        }

        synchronized (RunnableChild.class){
            vehicles.addAll(temp);
            duplicateCheckVehicles(vehicles);
        }
    }

    private void dealerSearch() {
        Query query = new Query();
        List<Dealer> temp = new ArrayList<Dealer>();
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
        }

        synchronized (RunnableChild.class){
            dealers.addAll(temp);
            duplicateCheckDealers(dealers);
        }

    }

    private List runQuery(String queryType, String value, Query query, int optional){
        Criteria criteria;
        if(queryType.equals("id") || queryType.equals("year")){
            if(optional != 0){
                criteria = Criteria.where(queryType).is(optional);
                query.addCriteria(criteria);
            }else {
                criteria = Criteria.where(queryType).is(value);
                query.addCriteria(criteria);
            }
        }else{
            criteria = Criteria.where(queryType).regex(".*" + value + ".*", "i");
            query.addCriteria(criteria);
        }

        if(parentName.equals("special")){
            return specialRepository.findMatching(special, query);
        }else if(parentName.equals("dealer")) {
            return dealerRepository.getMatchingDealers(dealer, query);
        }
        return vehicleRepository.getVehicles(vehicle, query);
    }

    private void duplicateCheck(List<Special> specialsCheck){
        for(int i=0;i<specialsCheck.size();i++){
            for(int j=i+1;j<specialsCheck.size();j++){
                if(specialsCheck.get(i).getId().equals(specialsCheck.get(j).getId())){
                    specialsCheck.remove(j);
                }
            }
        }
    }

    private void duplicateCheckDealers(List<Dealer> dealersCheck){
        for(int i=0;i<dealersCheck.size();i++){
            for(int j=i+1;j<dealersCheck.size();j++){
                if(dealersCheck.get(i).getId().equals(dealersCheck.get(j).getId())){
                    dealersCheck.remove(j);
                }
            }
        }
    }

    private void duplicateCheckVehicles(List<Vehicle> vehiclesCheck){
        for(int i=0;i<vehiclesCheck.size();i++){
            for(int j=i+1;j<vehiclesCheck.size();j++){
                if(vehiclesCheck.get(i).getId().equals(vehiclesCheck.get(j).getId())){
                    vehiclesCheck.remove(j);
                }
            }
        }
    }

    private void specialSearch(){
        Query query = new Query();
        List<Special> temp = new ArrayList<Special>();
        if(special.getDealer() != null && name == "dealer"){
            temp = runQuery(name, special.getDealer(), query,0);
        }
        if(special.getId() != null && name == "id"){
            temp = runQuery(name, special.getId(), query,0);
        }
        if(special.getType() != null && name == "type"){
            temp = runQuery(name, special.getType(), query,0);
        }
        if(special.getTitle() != null && name == "title"){
            temp = runQuery(name, special.getTitle(), query,0);
        }
        if(special.getDescription() != null && name == "description"){
            temp = runQuery(name, special.getDescription(), query,0);
        }
        if(special.getAmount() != null && name == "amount"){
            temp = runQuery(name, special.getAmount(), query,0);
        }

        synchronized (RunnableChild.class){
            specials.addAll(temp);
            duplicateCheck(specials);
        }
    }
}
