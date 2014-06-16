package test.config;

import org.springframework.data.repository.CrudRepository;
import test.model.Dealer;

/**
 * Created by maharb on 6/16/14.
 */
public interface DealerRepository extends CrudRepository<Dealer, String> {
}
