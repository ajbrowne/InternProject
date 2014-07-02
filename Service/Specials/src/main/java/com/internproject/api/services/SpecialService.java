package com.internproject.api.services;

import com.internproject.api.models.Special;
import com.internproject.api.repositories.SpecialRepository;
import com.internproject.api.concurrency.RunnableQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by maharb on 6/27/14.
 */
public class SpecialService {

    @Autowired
    private SpecialRepository specialRepository;

    public SpecialService(SpecialRepository specialRepository){
        this.specialRepository = specialRepository;
    }
    public SpecialService(){}

    /**
     * Function used to manage the creation of the appropriate threads used to run the
     * search for specials that match the given criteria
     *
     * @param special - parameters we are searching for
     * @return - List of matching specials objects
     */
    public List<Special> getSpecials(Special special){
        List<Special> specials = new ArrayList<Special>();
        //Create the parent thread taht will manage the special search threads
        RunnableQuery mainThread = new RunnableQuery("special", specialRepository, special, specials);
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(mainThread);
        es.shutdown();
        try {
            es.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Set trending flag on first 4 or all of the specials to true
        //Used to demo trending feature of app
        int check = 4;
        if(specials.size() < 4){
            check = specials.size();
        }
        for(int i =0; i<check;i++){
            specials.get(i).setTrending(true);
        }
        return specials;
    }

    /**
     * Store the given special. Used for tests only
     *
     * @param special - Special being stored
     * @return - the special that was stored
     */
    public Special store(Special special){
        specialRepository.save(special);
        return special;
    }

    /**
     * Retrieves all specials
     *
     * @return - a list of all specials
     */
    public List<Special> getAllSpecials(){
        return specialRepository.findAll();
    }

}
