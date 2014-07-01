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
        List<GeoResult> newDealer = dealerService.getDealerLocation(point);
        List<MergerObj> specials = new ArrayList<MergerObj>();
        List<Vehicle> tempVehicles = vehicleService.getVehicles(vehicle);
        List<String> ids = new ArrayList<String>();
        for(int j=0;j<tempVehicles.size();j++){
            ids.add(tempVehicles.get(j).getId());
        }
            for(int i =0; i < newDealer.size(); i++){
                Dealer tempDealer = (Dealer)newDealer.get(i).getContent();
                Special tempSpecial = new Special();
                tempSpecial.setDealer(tempDealer.getId());
                tempSpecial.setVehicleId(ids);
                List<Special> temp = specialService.getSpecials(tempSpecial);
                temp = vehicleHelper(temp, ids);
                //store the dealers name and the special in an object to pass to the app
                //dealer name is for the cards in the app.
                if(temp.size() != 0) {
                    specials.add(new MergerObj(tempDealer.getName(), temp, tempVehicles));
                }
            }

        return specials;
    }

    public List<Special> vehicleHelper(List<Special> specials, List<String> vehicleIds){

        for(int i =0; i<specials.size();i++){
            for(int j=0;j<vehicleIds.size();j++){
                if(!specials.get(i).getVehicleId().contains(vehicleIds.get(j))){
                    specials.remove(i);
                }
            }
        }

        return specials;
    }

}
