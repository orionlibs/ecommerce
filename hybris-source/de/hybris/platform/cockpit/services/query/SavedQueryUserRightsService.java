package de.hybris.platform.cockpit.services.query;

import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.Collection;
import java.util.List;

public interface SavedQueryUserRightsService extends SavedQueryService
{
    void addReadUser(PrincipalModel paramPrincipalModel, CockpitSavedQueryModel paramCockpitSavedQueryModel);


    void removeReadUser(PrincipalModel paramPrincipalModel, CockpitSavedQueryModel paramCockpitSavedQueryModel);


    List<PrincipalModel> getReadUsersForSavedQuery(CockpitSavedQueryModel paramCockpitSavedQueryModel);


    Collection<CockpitSavedQueryModel> getSharedQueries(SearchProvider paramSearchProvider, UserModel paramUserModel);


    Collection<CockpitSavedQueryModel> getNotSharedQueries(SearchProvider paramSearchProvider, UserModel paramUserModel);
}
