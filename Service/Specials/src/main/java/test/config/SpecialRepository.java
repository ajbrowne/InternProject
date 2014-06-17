package test.config;

import org.springframework.data.repository.CrudRepository;
import test.model.Special;

/**
 * Created by maharb on 6/16/14.
 */
public interface SpecialRepository extends CrudRepository<Special, String> {
    public Special findByTitle(String title);
    public Special findById(String id);
    public Special findByType(String type);
    public Special findByDealer(String dealer);
}
