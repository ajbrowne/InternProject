package api.repositories;

import api.models.Dealer;
import org.springframework.data.geo.Point;

import java.util.List;

/**
 * Created by maharb on 6/18/14.
 */
public interface DealerRepository {
    public List getDealerByLocation(Point point);
    public Dealer save(Dealer dealer);
}
