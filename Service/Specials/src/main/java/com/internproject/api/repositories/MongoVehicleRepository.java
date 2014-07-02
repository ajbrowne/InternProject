package com.internproject.api.repositories;

import com.internproject.api.models.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by maharb on 6/30/14.
 */
public class MongoVehicleRepository implements VehicleRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public MongoVehicleRepository(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public MongoVehicleRepository(){}


    /**
     * Create new vehicles in the mongodb. For test purposes only
     * @param vehicle - vehicle to be stored
     * @return - the vehicle that was saved
     */
    @Override
    public Vehicle save(Vehicle vehicle) {
        mongoTemplate.insert(vehicle, "vehicles");
        return vehicle;
    }

    /**
     * Search for vehicles based on the given vehicle criteria
     *
     * @param vehicle - vehicle to be searched for
     * @param query - created query object
     * @return - List of matching vehicles
     */
    @Override
    public List<Vehicle> getVehicles(Vehicle vehicle, Query query) {
        return mongoTemplate.find(query, Vehicle.class, "vehicles");
    }
}
