package com.internproject.api.repositories;

import com.internproject.api.models.Vehicle;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by maharb on 6/30/14.
 */
public interface VehicleRepository {

    public Vehicle save(Vehicle vehicle);

    public List<Vehicle> getVehicles(Vehicle vehicle, Query query);

    List<Vehicle> getAllVehicles();
}
