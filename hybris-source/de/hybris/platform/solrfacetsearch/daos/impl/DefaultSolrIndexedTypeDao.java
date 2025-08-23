package de.hybris.platform.solrfacetsearch.daos.impl;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.daos.SolrIndexedTypeDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultSolrIndexedTypeDao extends DefaultGenericDao<SolrIndexedTypeModel> implements SolrIndexedTypeDao
{
    public DefaultSolrIndexedTypeDao()
    {
        super("SolrIndexedType");
    }


    public List<SolrIndexedTypeModel> findAllIndexedTypes()
    {
        return find();
    }


    public SolrIndexedTypeModel findIndexedTypeByIdentifier(String identifier)
    {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("identifier", identifier);
        List<SolrIndexedTypeModel> indexedTypes = find(queryParams);
        ServicesUtil.validateIfSingleResult(indexedTypes, "Indexed type not found: " + queryParams, "More than one indexed type found: " + queryParams);
        return indexedTypes.iterator().next();
    }
}
