package com.internproject.api.controllers;

import com.internproject.api.models.Vehicle;
import com.internproject.api.services.VehicleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for hte vehicles objects
 * <p/>
 * Created by maharb on 6/30/14.
 */
@Controller
@RequestMapping(value = "/v1/specials")
public class VehicleController {

    private Logger log = Logger.getLogger(VehicleController.class.getName());
    @Autowired
    private VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public VehicleController() {
    }

    /**
     * Api endpoint for the creation of vehicles
     *
     * @param vehicle - vehicle to be stored
     * @return - the vehicle that we just stored
     */
    @RequestMapping(value = "/vehicleTest", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Vehicle> storeVehicle(@RequestBody Vehicle vehicle) {
        return new ResponseEntity<Vehicle>(vehicleService.store(vehicle), HttpStatus.OK);
    }

    /**
     * Api endpoint for querying for vehicles
     *
     * @param vehicle - criteria to find vehicle
     * @return - List of matching vehicles
     */
    @RequestMapping(value = "/vehicle")
    @ResponseBody
    public ResponseEntity<List<Vehicle>> getVehicles(@ModelAttribute Vehicle vehicle) {
        List<Vehicle> vehicles = vehicleService.getVehicles(vehicle);
        if (vehicles.size() == 0) {
            log.info("No Vehicles found");
            return new ResponseEntity<List<Vehicle>>(vehicles, HttpStatus.BAD_REQUEST);
        }
        //Successfully found specials
        log.info(vehicles.size() + " Vehicles were found that matched the criteria");
        return new ResponseEntity<List<Vehicle>>(vehicles, HttpStatus.OK);
    }
}
