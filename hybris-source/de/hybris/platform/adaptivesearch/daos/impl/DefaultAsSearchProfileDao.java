package de.hybris.platform.adaptivesearch.daos.impl;

import de.hybris.platform.adaptivesearch.daos.AsSearchProfileDao;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;

public class DefaultAsSearchProfileDao extends AbstractAsGenericDao<AbstractAsSearchProfileModel> implements AsSearchProfileDao
{
    protected static final String BASE_QUERY = "SELECT {pk} FROM {AbstractAsSearchProfile} WHERE";


    public DefaultAsSearchProfileDao()
    {
        super("AbstractAsSearchProfile");
    }


    public <T extends AbstractAsSearchProfileModel> List<T> findAllSearchProfiles()
    {
        return find();
    }


    public <T extends AbstractAsSearchProfileModel> List<T> findSearchProfilesByIndexTypesAndCatalogVersions(List<String> indexTypes, List<CatalogVersionModel> catalogVersions)
    {
        StringBuilder query = new StringBuilder("SELECT {pk} FROM {AbstractAsSearchProfile} WHERE");
        Map<String, Object> parameters = new HashMap<>();
        if(CollectionUtils.isNotEmpty(indexTypes))
        {
            appendClause(query, parameters, "indexType", indexTypes);
        }
        if(CollectionUtils.isNotEmpty(indexTypes) && CollectionUtils.isNotEmpty(catalogVersions))
        {
            appendAndClause(query);
        }
        if(CollectionUtils.isNotEmpty(catalogVersions))
        {
            appendClause(query, parameters, "catalogVersion", catalogVersions);
        }
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), parameters);
        return getFlexibleSearchService().search(searchQuery).getResult();
    }


    public <T extends AbstractAsSearchProfileModel> List<T> findSearchProfilesByCatalogVersion(CatalogVersionModel catalogVersion)
    {
        StringBuilder query = new StringBuilder("SELECT {pk} FROM {AbstractAsSearchProfile} WHERE");
        Map<String, Object> parameters = new HashMap<>();
        appendClause(query, parameters, "catalogVersion", catalogVersion);
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), parameters);
        return getFlexibleSearchService().search(searchQuery).getResult();
    }


    public <T extends AbstractAsSearchProfileModel> Optional<T> findSearchProfileByCode(CatalogVersionModel catalogVersion, String code)
    {
        StringBuilder query = new StringBuilder("SELECT {pk} FROM {AbstractAsSearchProfile} WHERE");
        Map<String, Object> parameters = new HashMap<>();
        appendClause(query, parameters, "catalogVersion", catalogVersion);
        appendAndClause(query);
        appendClause(query, parameters, "code", code);
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), parameters);
        List<T> searchProfiles = getFlexibleSearchService().search(searchQuery).getResult();
        return searchProfiles.isEmpty() ? Optional.<T>empty() : Optional.<T>of(searchProfiles.get(0));
    }


    public <T extends AbstractAsSearchProfileModel> List<T> getSearchProfiles(String query, Map<String, Object> filters)
    {
        StringBuilder fsQuery = new StringBuilder("SELECT {pk} FROM {AbstractAsSearchProfile} WHERE");
        Map<String, Object> parameters = new HashMap<>();
        if(StringUtils.isNotBlank(query))
        {
            appendLikeClause(fsQuery, parameters, "code", "%" + query + "%");
        }
        filters.forEach((key, value) -> {
            if(MapUtils.isNotEmpty(parameters))
            {
                appendAndClause(fsQuery);
            }
            appendClause(fsQuery, parameters, key, value);
        });
        appendOrderByClause(fsQuery, "code", true);
        return queryList(fsQuery.toString(), filters);
    }


    public <T extends AbstractAsSearchProfileModel> SearchPageData<T> getSearchProfiles(String query, Map<String, Object> filters, SearchPageData<?> pagination)
    {
        StringBuilder fsQuery = new StringBuilder("SELECT {pk} FROM {AbstractAsSearchProfile} WHERE");
        Map<String, Object> parameters = new HashMap<>();
        if(StringUtils.isNotBlank(query))
        {
            appendLikeClause(fsQuery, parameters, "code", "%" + query + "%");
        }
        filters.forEach((key, value) -> {
            if(MapUtils.isNotEmpty(parameters))
            {
                appendAndClause(fsQuery);
            }
            appendClause(fsQuery, parameters, key, value);
        });
        appendOrderByClause(fsQuery, "code", true);
        return queryList(fsQuery.toString(), parameters, pagination.getPagination());
    }
}
