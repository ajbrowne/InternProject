package api.repositories;

import api.models.Special;

import java.util.List;

/**
 * Created by maharb on 6/18/14.
 */
public interface SpecialRepository {

    public Special save(Special special);

    public List findAll();

    public List findMatching(Special special);
}
