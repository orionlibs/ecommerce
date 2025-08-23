package de.hybris.platform.cockpit.services.query.daos.impl;

import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.services.query.daos.SavedQueryDao;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultSavedQueryDao extends DefaultGenericDao<CockpitSavedQueryModel> implements SavedQueryDao
{
    public DefaultSavedQueryDao()
    {
        super("CockpitSavedQuery");
    }


    public Collection<CockpitSavedQueryModel> findSavedQueriesByUser(UserModel userModel)
    {
        return userModel.getCockpitSavedQueries();
    }


    public Collection<CockpitSavedQueryModel> findGlobalSavedQueries()
    {
        Map<String, Object> params = new HashMap<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT {").append("pk").append("} ");
        queryBuilder.append("FROM {").append("CockpitSavedQuery").append("} ");
        queryBuilder.append("WHERE {").append("user").append("} IS NULL");
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString(), params);
        query.setNeedTotal(false);
        SearchResult<CockpitSavedQueryModel> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }
}
