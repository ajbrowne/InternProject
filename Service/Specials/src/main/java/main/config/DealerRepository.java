package main.config;

import main.model.Dealer;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by maharb on 6/16/14.
 */
public interface DealerRepository extends MongoRepository<Dealer, String> {

    /**
     * Makes use of the Mongodb $near search. This allows us to put
     * a point object into our db and index it and then find the closest
     * results.
     *
     * @param loc
     * @return
     */
    public List<Dealer> findByLocationNear(Point loc);
    public Dealer findByName(String name);
}
