package de.hybris.platform.solrfacetsearch.daos.impl;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.daos.SolrIndexDao;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultSolrIndexDao extends DefaultGenericDao<SolrIndexModel> implements SolrIndexDao
{
    public DefaultSolrIndexDao()
    {
        super("SolrIndex");
    }


    public List<SolrIndexModel> findAllIndexes()
    {
        return find();
    }


    public SolrIndexModel findIndexByConfigAndTypeAndQualifier(SolrFacetSearchConfigModel facetSearchConfig, SolrIndexedTypeModel indexedType, String qualifier)
    {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("facetSearchConfig", facetSearchConfig);
        queryParams.put("indexedType", indexedType);
        queryParams.put("qualifier", qualifier);
        Collection<SolrIndexModel> indexes = find(queryParams);
        ServicesUtil.validateIfSingleResult(indexes, "Index not found: " + queryParams.toString(), "More than one index was found: " + queryParams
                        .toString());
        return indexes.iterator().next();
    }


    public SolrIndexModel findActiveIndexByConfigAndType(SolrFacetSearchConfigModel facetSearchConfig, SolrIndexedTypeModel indexedType)
    {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("facetSearchConfig", facetSearchConfig);
        queryParams.put("indexedType", indexedType);
        queryParams.put("active", Boolean.valueOf(true));
        Collection<SolrIndexModel> indexes = find(queryParams);
        ServicesUtil.validateIfSingleResult(indexes, "Active index not found: " + queryParams.toString(), "More than one active index found: " + queryParams
                        .toString());
        return indexes.iterator().next();
    }


    public List<SolrIndexModel> findIndexesByConfigAndType(SolrFacetSearchConfigModel facetSearchConfig, SolrIndexedTypeModel indexedType)
    {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("facetSearchConfig", facetSearchConfig);
        queryParams.put("indexedType", indexedType);
        return find(queryParams);
    }
}
