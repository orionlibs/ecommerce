package de.hybris.platform.solrfacetsearch.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.daos.SolrFacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultSolrFacetSearchConfigDao extends DefaultGenericDao<SolrFacetSearchConfigModel> implements SolrFacetSearchConfigDao
{
    protected static final String CATALOG_VERSION_PARAM = "catalogVersion";
    protected static final String FIND_BY_CATALOG_VERSION_QUERY = "SELECT {f.PK} FROM {SolrFacetSearchConfig AS f JOIN SolrFacetSearchConfig2CatalogVersionRelation as rel ON {f.PK} = {rel.source} } WHERE {rel.target}=?catalogVersion ";


    public DefaultSolrFacetSearchConfigDao()
    {
        super("SolrFacetSearchConfig");
    }


    public List<SolrFacetSearchConfigModel> findAllFacetSearchConfigs()
    {
        return find();
    }


    public SolrFacetSearchConfigModel findFacetSearchConfigByName(String name)
    {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("name", name);
        Collection<SolrFacetSearchConfigModel> configurations = find(queryParams);
        ServicesUtil.validateIfSingleResult(configurations, "Solr facet search configuration not found: " + queryParams.toString(), "More than one Solr facet search configuration found: " + queryParams
                        .toString());
        return configurations.iterator().next();
    }


    public List<SolrFacetSearchConfigModel> findFacetSearchConfigsByCatalogVersion(CatalogVersionModel catalogVersion)
    {
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery("SELECT {f.PK} FROM {SolrFacetSearchConfig AS f JOIN SolrFacetSearchConfig2CatalogVersionRelation as rel ON {f.PK} = {rel.source} } WHERE {rel.target}=?catalogVersion ");
        searchQuery.addQueryParameter("catalogVersion", catalogVersion);
        SearchResult<SolrFacetSearchConfigModel> searchResult = getFlexibleSearchService().search(searchQuery);
        return searchResult.getResult();
    }
}
