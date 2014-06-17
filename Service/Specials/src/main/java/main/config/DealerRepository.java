package main.config;

import main.model.Dealer;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by maharb on 6/16/14.
 */
public interface DealerRepository extends MongoRepository<Dealer, String> {

    public List<Dealer> findByLocNear(Point loc);
}
