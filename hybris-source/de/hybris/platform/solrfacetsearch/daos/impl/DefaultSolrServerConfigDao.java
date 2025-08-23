package de.hybris.platform.solrfacetsearch.daos.impl;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.daos.SolrServerConfigDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultSolrServerConfigDao extends DefaultGenericDao<SolrServerConfigModel> implements SolrServerConfigDao
{
    public DefaultSolrServerConfigDao()
    {
        super("SolrServerConfig");
    }


    public List<SolrServerConfigModel> findAllSolrServerConfigs()
    {
        return find();
    }


    public SolrServerConfigModel findSolrServerConfigByName(String name)
    {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("name", name);
        Collection<SolrServerConfigModel> solrServerConfigs = find(queryParams);
        ServicesUtil.validateIfSingleResult(solrServerConfigs, "Solr server config not found: " + queryParams.toString(), "More than one Solr server config found: " + queryParams
                        .toString());
        return solrServerConfigs.iterator().next();
    }
}
