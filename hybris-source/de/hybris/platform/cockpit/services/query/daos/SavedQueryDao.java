package de.hybris.platform.cockpit.services.query.daos;

import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.Collection;

public interface SavedQueryDao
{
    Collection<CockpitSavedQueryModel> findSavedQueriesByUser(UserModel paramUserModel);


    Collection<CockpitSavedQueryModel> findGlobalSavedQueries();
}
