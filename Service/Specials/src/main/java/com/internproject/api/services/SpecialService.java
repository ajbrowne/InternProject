package com.internproject.api.services;

import com.internproject.api.models.Special;
import com.internproject.api.repositories.SpecialRepository;
import com.internproject.api.concurrency.RunnableQuery;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Special Service layer that is used to manage the logic for creating and
 * searching for specials
 *
 * Created by maharb on 6/27/14.
 */
public class SpecialService {

    @Autowired
    private SpecialRepository specialRepository;
    private Logger log = Logger.getLogger(SpecialService.class.getName());

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
            log.warn(e);
        }
        //Set trending flag on first 4 or all of the specials to true
        //Used to demo trending feature of app
        int check = 3;
        if(specials.size() < check){
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
        DealerService dealerService = new DealerService();
        dealerService.updateSpecials(special.getDealer());
        return special;
    }

    /**
     * Retrieves all specials
     *
     * @return - a list of all specials
     */
    public List getAllSpecials(){
        return specialRepository.findAll();
    }

}
