package com.api.concurrency;

import com.api.models.Dealer;
import com.api.models.Special;
import com.api.repositories.DealerRepository;
import com.api.repositories.SpecialRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maharb on 6/27/14.
 */
public class RunnableChild implements Runnable {

    private List<Dealer> dealers;
    private Dealer dealer;
    private DealerRepository dealerRepository;
    private SpecialRepository specialRepository;
    private String name;
    private String parentName;
    private Special special;
    private List<Special> specials;

    public RunnableChild(String name, String parentName, SpecialRepository specialRepository, Special special, List<Special> specials){
        this.name = name;
        this.specialRepository = specialRepository;
        this.special = special;
        this.specials = specials;
        this.parentName = parentName;
    }

    public RunnableChild(String name, String parentName, DealerRepository dealerRepository, Dealer dealer, List<Dealer> dealers){
        this.name = name;
        this.dealerRepository = dealerRepository;
        this.dealer = dealer;
        this.dealers = dealers;
        this.parentName = parentName;
    }

    @Override
    public void run() {
        if(parentName.equals("special")){
            specialSearch();
        }else if(parentName.equals("dealer")){
            dealerSearch();
        }


    }

    private void dealerSearch() {
        Query query = new Query();
        List<Dealer> temp = new ArrayList<Dealer>();
        if(dealer.getId() != null && name.equals("id")){
            temp = runQuery(name, dealer.getId(), query);
        }else if(dealer.getName() != null && name.equals("name")){
            temp = runQuery(name, dealer.getName(), query);
        }else if(dealer.getState() != null && name.equals("state")){
            temp = runQuery(name, dealer.getState(), query);
        }else if(dealer.getCity() != null && name.equals("city")){
            temp = runQuery(name, dealer.getCity(), query);
        }else if(dealer.getAdmin() != null && name.equals("admin")){
            temp = runQuery(name, dealer.getAdmin(), query);
        }

    }

    private List runQuery(String queryType, String value, Query query){
        Criteria criteria;
        if(queryType.equals("id")){
            criteria = Criteria.where(queryType).is(value);
            query.addCriteria(criteria);
        }else{
            criteria = Criteria.where(queryType).regex(".*" + value + ".*", "i");
            query.addCriteria(criteria);
        }

        if(parentName.equals("special")){
            return specialRepository.findMatching(special, query);
        }
        return dealerRepository.getMatchingDealers(dealer,query);
    }

    private void duplicateCheck(List<Special> specialsCheck){
        for(int i=0;i<specialsCheck.size();i++){
            for(int j=i+1;j<specialsCheck.size();j++){
                if(specialsCheck.get(i).getId().equals(specialsCheck.get(j).getId())){
                    specialsCheck.remove(j);
                }
            }
        }
    }

    private void specialSearch(){
        Query query = new Query();
        List<Special> temp = new ArrayList<Special>();
        if(special.getDealer() != null && name == "dealer"){
            temp = runQuery(name, special.getDealer(), query);
        }
        if(special.getId() != null && name == "id"){
            temp = runQuery(name, special.getId(), query);
        }
        if(special.getType() != null && name == "type"){
            temp = runQuery(name, special.getType(), query);
        }
        if(special.getTitle() != null && name == "title"){
            temp = runQuery(name, special.getTitle(), query);
        }
        if(special.getDescription() != null && name == "description"){
            temp = runQuery(name, special.getDescription(), query);
        }
        if(special.getAmount() != null && name == "amount"){
            temp = runQuery(name, special.getAmount(), query);
        }

        synchronized (RunnableChild.class){
            specials.addAll(temp);
            duplicateCheck(specials);
        }
    }
}
