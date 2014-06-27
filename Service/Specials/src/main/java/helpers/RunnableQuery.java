package helpers;

import api.models.Special;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maharb on 6/27/14.
 */
public class RunnableQuery implements Runnable {

    private Thread t;
    private String name;
    private MongoTemplate mongoTemplate;
    private Special special;
    private final List<Special> specials;

    public RunnableQuery(String name, MongoTemplate mongoTemplate, Special special, List<Special> specials){
        this.name = name;
        this.mongoTemplate = mongoTemplate;
        this.special = special;
        this.specials = specials;
    }

    @Override
    public void run() {
        Query query = new Query();
        List<Special> temp = new ArrayList<Special>();
        System.out.println(special);
        if(special.getDealer() != null && name == "dealer"){
            temp = doSomeWork(name, special.getDealer(), query);
        }
        if(special.getId() != null && name == "id"){
            temp = doSomeWork(name, special.getId(), query);
        }
        if(special.getType() != null && name == "type"){
            temp = doSomeWork(name, special.getType(), query);
        }
        if(special.getTitle() != null && name == "title"){
            temp = doSomeWork(name, special.getTitle(), query);
        }
        if(special.getDescription() != null && name == "description"){
            temp = doSomeWork(name, special.getDescription(), query);
        }
        if(special.getAmount() != null && name == "amount"){
            temp = doSomeWork(name, special.getAmount(), query);
        }
        synchronized (specials){
            specials.addAll(temp);
        }

    }

    private List<Special> doSomeWork(String queryType, String value, Query query){
        Criteria criteria;
        System.out.println(queryType);
        if(queryType.equals("id")){
            criteria = Criteria.where(queryType).is(value);
            query.addCriteria(criteria);
        }else{
            criteria = Criteria.where(queryType).regex(".*" + value + ".*", "i");
            query.addCriteria(criteria);
        }

        return mongoTemplate.find(query, Special.class);
    }
}
