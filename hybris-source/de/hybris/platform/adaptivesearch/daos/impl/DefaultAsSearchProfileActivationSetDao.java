package de.hybris.platform.adaptivesearch.daos.impl;

import de.hybris.platform.adaptivesearch.daos.AsSearchProfileActivationSetDao;
import de.hybris.platform.adaptivesearch.model.AsSearchProfileActivationSetModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DefaultAsSearchProfileActivationSetDao extends AbstractAsGenericDao<AsSearchProfileActivationSetModel> implements AsSearchProfileActivationSetDao
{
    protected static final String BASE_QUERY = "SELECT {pk} FROM {AsSearchProfileActivationSet} WHERE ";


    public DefaultAsSearchProfileActivationSetDao()
    {
        super("AsSearchProfileActivationSet");
    }


    public List<AsSearchProfileActivationSetModel> findAllSearchProfileActivationSets()
    {
        return find();
    }


    public Optional<AsSearchProfileActivationSetModel> findSearchProfileActivationSetByIndexType(CatalogVersionModel catalogVersion, String indexType)
    {
        StringBuilder query = new StringBuilder("SELECT {pk} FROM {AsSearchProfileActivationSet} WHERE ");
        Map<Object, Object> parameters = new HashMap<>();
        appendClause(query, parameters, "catalogVersion", catalogVersion);
        appendAndClause(query);
        appendClause(query, parameters, "indexType", indexType);
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), parameters);
        List<AsSearchProfileActivationSetModel> searchProfileActivationsSets = getFlexibleSearchService().search(searchQuery).getResult();
        return searchProfileActivationsSets.isEmpty() ? Optional.<AsSearchProfileActivationSetModel>empty() : Optional.<AsSearchProfileActivationSetModel>of(searchProfileActivationsSets.get(0));
    }


    public List<AsSearchProfileActivationSetModel> findSearchProfileActivationSetsByCatalogVersionsAndIndexType(List<CatalogVersionModel> catalogVersions, String indexType)
    {
        StringBuilder query = new StringBuilder("SELECT {pk} FROM {AsSearchProfileActivationSet} WHERE ");
        Map<Object, Object> parameters = new HashMap<>();
        appendClause(query, parameters, "catalogVersion", catalogVersions);
        appendAndClause(query);
        appendClause(query, parameters, "indexType", indexType);
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), parameters);
        return getFlexibleSearchService().search(searchQuery).getResult();
    }
}
