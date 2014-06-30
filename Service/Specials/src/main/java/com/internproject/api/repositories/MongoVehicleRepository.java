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

    @Override
    public Vehicle save(Vehicle vehicle) {
        mongoTemplate.insert(vehicle, "vehicles");
        return vehicle;
    }

    @Override
    public List<Vehicle> getVehicles(Vehicle vehicle, Query query) {
        return mongoTemplate.find(query, Vehicle.class);
    }
}
