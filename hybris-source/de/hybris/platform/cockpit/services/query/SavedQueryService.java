package de.hybris.platform.cockpit.services.query;

import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import de.hybris.platform.core.model.user.UserModel;
import java.util.Collection;

public interface SavedQueryService
{
    Collection<CockpitSavedQueryModel> getSavedQueries(SearchProvider paramSearchProvider, UserModel paramUserModel);


    CockpitSavedQueryModel createSavedQuery(String paramString, Query paramQuery, UserModel paramUserModel);


    Query getQuery(CockpitSavedQueryModel paramCockpitSavedQueryModel);


    @Deprecated
    void deleteQuery(CockpitSavedQueryModel paramCockpitSavedQueryModel);


    void renameQuery(CockpitSavedQueryModel paramCockpitSavedQueryModel, String paramString);


    void storeUpdates(CockpitSavedQueryModel paramCockpitSavedQueryModel);


    void publishSavedQuery(CockpitSavedQueryModel paramCockpitSavedQueryModel);
}
