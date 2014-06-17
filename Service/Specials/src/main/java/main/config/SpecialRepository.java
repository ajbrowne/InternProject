package main.config;

import main.model.Special;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by maharb on 6/16/14.
 */
public interface SpecialRepository extends MongoRepository<Special, String> {
    public Special findByTitle(String title);
    public Special findById(String id);
    public Special findByType(String type);
    public Special findByDealer(String dealer);
}
