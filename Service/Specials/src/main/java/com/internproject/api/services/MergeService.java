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

    public MergeService(SpecialService specialService, DealerService dealerService, VehicleService vehicleService){
        this.specialService = specialService;
        this.dealerService = dealerService;
        this.vehicleService = vehicleService;
    }

    public MergeService(){}

    public List getNearestSpecials(Point point){
        List<GeoResult> newDealer = dealerService.getDealerLocation(point);
        List<MergerObj> specials = new ArrayList<MergerObj>();
        //loop over the dealers to find the dealers specials
        for(int i = 0; i < newDealer.size();i++){
            Dealer tempDealer = (Dealer)newDealer.get(i).getContent();
            Special tempSpecial = new Special();
            tempSpecial.setDealer(tempDealer.getId());
            List<Special> temp = specialService.getSpecials(tempSpecial);
            //store the dealers name and the special in an object to pass to the app
            //dealer name is for the cards in the app.
            if(temp.size() != 0) {
                specials.add(new MergerObj(tempDealer.getName(), temp));
            }
        }
        log.info("Number of specials: " + specials.size());

        return specials;
    }

    public List getNearestVehicles(Point point, Vehicle vehicle){
        List<MergerObj> specials = getNearestSpecials(point);

        for(int i =0; i < specials.size(); i++){
            List<Vehicle> theVehicles = vehicleHelper(specials.get(i).getSpecials(), vehicle);
            specials.get(i).setVehicles(theVehicles);
        }


        return specials;

    }

    public List<Vehicle> vehicleHelper(List<Special> specials, Vehicle vehicle){
        List<Vehicle> temp = new ArrayList<Vehicle>();
        List<String> ids = new ArrayList<String>();
        List<Vehicle> returnVehicle = new ArrayList<Vehicle>();
        for(int i = 0; i < specials.size();i++){
            if(specials.get(i).getVehicleId().size() != 0){
                ids = specials.get(i).getVehicleId();
                for(int j = 0; j < ids.size(); j++){
                    vehicle.setId(ids.get(j));
                    temp = vehicleService.getVehicles(vehicle);
                    if(temp.size() != 0){
                        returnVehicle.addAll(temp);
                    }
                }
            }

        }
        duplicateCheckVehicles(returnVehicle);
        return returnVehicle;
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

}
