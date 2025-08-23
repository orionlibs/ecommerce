package de.hybris.platform.solrfacetsearch.daos;

import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;
import java.util.List;

public interface SolrServerConfigDao extends GenericDao<SolrServerConfigModel>
{
    List<SolrServerConfigModel> findAllSolrServerConfigs();


    SolrServerConfigModel findSolrServerConfigByName(String paramString);
}
