package com.internproject.api.concurrency;

import com.internproject.api.models.Dealer;
import com.internproject.api.models.Special;
import com.internproject.api.models.Vehicle;
import com.internproject.api.repositories.DealerRepository;
import com.internproject.api.repositories.SpecialRepository;
import com.internproject.api.repositories.VehicleRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by maharb on 6/27/14.
 */
public class RunnableQuery implements Runnable {


    private VehicleRepository vehicleRepository;
    private Vehicle vehicle;
    private List<Vehicle> vehicles;
    private SpecialRepository specialRepository;
    private String name;
    private Special special;
    private List<Special> specials;

    private DealerRepository dealerRepository;
    private Dealer dealer;
    private List<Dealer> dealers;

    /*
     * Constructor for specials
     */
    public RunnableQuery(String name, SpecialRepository specialRepository,Special special, List<Special> specials){
        this.name = name;
        this.specialRepository = specialRepository;
        this.special = special;
        this.specials = specials;
    }

    /*
     * Constructor for dealers
     */
    public RunnableQuery(String name, DealerRepository dealerRepository,Dealer dealer, List<Dealer> dealers){
        this.name = name;
        this.dealerRepository = dealerRepository;
        this.dealer = dealer;
        this.dealers = dealers;
    }

    /*
     * Constructor for vehicles
     */
    public RunnableQuery(String name, VehicleRepository vehicleRepository, Vehicle vehicle, List<Vehicle> vehicles){
        this.name = name;
        this.vehicleRepository = vehicleRepository;
        this.vehicle = vehicle;
        this.vehicles = vehicles;
    }

    /**
     * Run the correct methods to create the child threads based on what search is happening
     */
    @Override
    public void run() {
        if(name.equals("special")){
            specialChildren();
        }else if(name.equals("dealer")){
            dealerChildren();
        }else if(name.equals("vehicle")){
            vehicleChildren();
        }

    }

    /**
     * Helper method to create the special children threads which create the appropriate search queries
     * and then run the query.
     */
    public void specialChildren(){
        //Children created with their name being the field that thread will search in
        RunnableChild t1 = new RunnableChild("id", name,specialRepository, special, specials);
        RunnableChild t2 = new RunnableChild("title", name,specialRepository, special, specials);
        RunnableChild t3 = new RunnableChild("type", name,specialRepository, special, specials);
        RunnableChild t4 = new RunnableChild("description", name,specialRepository, special, specials);
        RunnableChild t5 = new RunnableChild("amount", name,specialRepository, special, specials);
        RunnableChild t6 = new RunnableChild("dealer", name,specialRepository, special, specials);
        RunnableChild t7 = new RunnableChild("vehicleId", name, specialRepository, special, specials);

        //Executor service manages the running of all of the threads and then waiting for all of them
        //to finish before moving on.
        RunnableChild[] list = new RunnableChild[]{t1,t2,t3,t4,t5,t6, t7};
        ExecutorService es = Executors.newCachedThreadPool();
        for(int i= 0; i < 7; i++){
            es.execute(list[i]);
        }
        es.shutdown();
        try {
            es.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to create the dealer children threads which create the appropriate search queries
     * and then run the query.
     */
    public void dealerChildren(){
        //Children created with their name being the field that thread will search in
        RunnableChild t1 = new RunnableChild("id", name,dealerRepository, dealer, dealers);
        RunnableChild t5 = new RunnableChild("name", name,dealerRepository, dealer, dealers);
        RunnableChild t2 = new RunnableChild("admin", name,dealerRepository, dealer, dealers);
        RunnableChild t3 = new RunnableChild("city", name,dealerRepository, dealer, dealers);
        RunnableChild t4 = new RunnableChild("state", name,dealerRepository, dealer, dealers);

        //Executor service manages the running of all of the threads and then waiting for all of them
        //to finish before moving on.
        RunnableChild[] list = new RunnableChild[]{t1,t2,t3,t4,t5};
        ExecutorService es = Executors.newCachedThreadPool();
        for(int i= 0; i < 5; i++){
            es.execute(list[i]);
        }
        es.shutdown();
        try {
            es.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to create the vehicle children threads which create the appropriate search queries
     * and then run the query.
     */
    public void vehicleChildren(){
        //Children created with their name being the field that thread will search in
        RunnableChild t1 = new RunnableChild("id", name, vehicleRepository, vehicle, vehicles);
        RunnableChild t5 = new RunnableChild("year", name,vehicleRepository, vehicle, vehicles);
        RunnableChild t2 = new RunnableChild("make", name,vehicleRepository, vehicle, vehicles);
        RunnableChild t3 = new RunnableChild("model", name,vehicleRepository, vehicle, vehicles);
        RunnableChild t4 = new RunnableChild("trim", name,vehicleRepository, vehicle, vehicles);

        //Executor service manages the running of all of the threads and then waiting for all of them
        //to finish before moving on.
        RunnableChild[] list = new RunnableChild[]{t1,t2,t3,t4,t5};
        ExecutorService es = Executors.newCachedThreadPool();
        for(int i= 0; i < 5; i++){
            es.execute(list[i]);
        }
        es.shutdown();
        try {
            es.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


