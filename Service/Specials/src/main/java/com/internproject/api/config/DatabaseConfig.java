package com.internproject.api.config;

import com.internproject.api.repositories.*;
import com.internproject.api.services.DealerService;
import com.internproject.api.services.MergeService;
import com.internproject.api.services.SpecialService;
import com.internproject.api.services.VehicleService;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.net.UnknownHostException;

/**
 * Configuration file that sets up our mongodb connection
 * and the benas that this service uses
 * Created by maharb on 6/18/14.
 */
@Configuration
public class DatabaseConfig {


    @Bean
    public SpecialRepository specialRepository() throws UnknownHostException {
        return new MongoSpecialRepository(mongoTemplate());
    }

    @Bean
    public UserRepository userRepository() throws UnknownHostException {
        return new MongoUserRepository(mongoTemplate());
    }


    @Bean
    public DealerRepository dealerRepository() throws UnknownHostException {
        return new MongoDealerRepository(mongoTemplate());
    }

    @Bean
    public SpecialService specialService() throws UnknownHostException {
        return new SpecialService(specialRepository());
    }

    @Bean
    public DealerService dealerService() throws UnknownHostException {
        return new DealerService(dealerRepository());
    }

    @Bean
    public MergeService mergeService() throws UnknownHostException {
        return new MergeService(specialService(), dealerService(), vehicleService());
    }

    @Bean
    public VehicleRepository vehicleRepository() throws UnknownHostException {
        return new MongoVehicleRepository(mongoTemplate());
    }

    @Bean
    public VehicleService vehicleService() throws UnknownHostException {
        return new VehicleService(vehicleRepository());
    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClient("localhost"), "Specials");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws UnknownHostException {
        return new MongoTemplate(mongoDbFactory());
    }


}
