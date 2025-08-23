package de.hybris.platform.solrfacetsearch.daos.impl;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.daos.SolrIndexedPropertyDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultSolrIndexedPropertyDao extends DefaultGenericDao<SolrIndexedPropertyModel> implements SolrIndexedPropertyDao
{
    public DefaultSolrIndexedPropertyDao()
    {
        super("SolrIndexedProperty");
    }


    public List<SolrIndexedPropertyModel> findIndexedPropertiesByIndexedType(SolrIndexedTypeModel indexedType)
    {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("solrIndexedType", indexedType);
        return find(queryParams);
    }


    public SolrIndexedPropertyModel findIndexedPropertyByName(SolrIndexedTypeModel indexedType, String name)
    {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("solrIndexedType", indexedType);
        queryParams.put("name", name);
        List<SolrIndexedPropertyModel> indexedProperties = find(queryParams);
        ServicesUtil.validateIfSingleResult(indexedProperties, "Indexed property not found: " + name, "More than one indexed property found: " + name);
        return indexedProperties.iterator().next();
    }
}
