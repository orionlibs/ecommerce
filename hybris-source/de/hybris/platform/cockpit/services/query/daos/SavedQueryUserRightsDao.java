package de.hybris.platform.cockpit.services.query.daos;

import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.Collection;

public interface SavedQueryUserRightsDao extends SavedQueryDao
{
    Collection<CockpitSavedQueryModel> findReadableSavedQueriesByUser(UserModel paramUserModel);


    Collection<CockpitSavedQueryModel> findSharedQueriesByUser(UserModel paramUserModel);


    Collection<CockpitSavedQueryModel> findNotSharedQueriesByUser(UserModel paramUserModel);
}
