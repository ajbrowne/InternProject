package com.internproject.api.services;

import com.internproject.api.concurrency.RunnableQuery;
import com.internproject.api.models.Vehicle;
import com.internproject.api.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by maharb on 6/30/14.
 */
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public VehicleService(){}
    public VehicleService(VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle store(Vehicle vehicle){
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getVehicles(Vehicle vehicle){
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        RunnableQuery mainThread = new RunnableQuery("vehicle", vehicleRepository, vehicle, vehicles);
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(mainThread);
        es.shutdown();
        try {
            es.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return vehicles;
    }
}
