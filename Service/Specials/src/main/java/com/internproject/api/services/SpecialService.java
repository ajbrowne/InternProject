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

    public List<Special> getSpecials(Special special){
        List<Special> specials = new ArrayList<Special>();
        RunnableQuery mainThread = new RunnableQuery("special", specialRepository, special, specials);
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(mainThread);
        es.shutdown();
        try {
            es.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return specials;
    }

    public Special store(Special special){
        specialRepository.save(special);
        return special;
    }

    public List<Special> getAllSpecials(){
        return specialRepository.findAll();
    }

}
