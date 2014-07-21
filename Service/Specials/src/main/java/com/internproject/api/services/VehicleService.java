package com.internproject.api.services;

import com.internproject.api.concurrency.RunnableQuery;
import com.internproject.api.models.Vehicle;
import com.internproject.api.repositories.VehicleRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Service to handle the logic of vehicle search and creation
 * <p/>
 * Created by maharb on 6/30/14.
 */
@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;
    private Logger log = Logger.getLogger(VehicleService.class.getName());

    public VehicleService() {
    }

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Logic needed to store the vehicles
     *
     * @param vehicle - vehicle being stored
     * @return - Vehicle that was just stored
     */
    public Vehicle store(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    /**
     * Logic needed to get vehicles based on given criteria.
     * Creation of appropriate thread to find vehicles concurrently
     *
     * @param vehicle - vehicle criteria being searched for
     * @return - List of matching vehicles
     */
    public List<Vehicle> getVehicles(Vehicle vehicle) {
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        RunnableQuery mainThread = new RunnableQuery("vehicle", vehicleRepository, vehicle, vehicles);
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(mainThread);
        es.shutdown();
        try {
            es.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.warn(e);
        }
        return vehicles;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.getAllVehicles();
    }
}
