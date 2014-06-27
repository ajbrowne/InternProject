package api.repositories;

import api.models.Special;
import helpers.RunnableQuery;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by maharb on 6/18/14.
 */
public class MongoSpecialRepository implements SpecialRepository {

    private MongoTemplate mongoTemplate;

    public MongoSpecialRepository(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Special save(Special special) {

        mongoTemplate.insert(special, "special");
        return special;
    }

    @Override
    public List findAll() {
        return mongoTemplate.findAll(Special.class);
    }

    @Override
    public List<Special> findMatching(Special special) {
        final List<Special> specials = new ArrayList<Special>();
        RunnableQuery t1 = new RunnableQuery("id", mongoTemplate, special, specials);
        RunnableQuery t2 = new RunnableQuery("title", mongoTemplate, special, specials);
        RunnableQuery t3 = new RunnableQuery("type", mongoTemplate, special, specials);
        RunnableQuery t4 = new RunnableQuery("description", mongoTemplate, special, specials);
        RunnableQuery t5 = new RunnableQuery("amount", mongoTemplate, special, specials);
        RunnableQuery t6 = new RunnableQuery("dealer", mongoTemplate, special, specials);
        RunnableQuery[] list = new RunnableQuery[]{t1,t2,t3,t4,t5,t6};
        ExecutorService es = Executors.newCachedThreadPool();
        for(int i= 0; i < 6; i++){
            es.execute(list[i]);
        }
        es.shutdown();
        try {
            es.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return specials;
    }
}
