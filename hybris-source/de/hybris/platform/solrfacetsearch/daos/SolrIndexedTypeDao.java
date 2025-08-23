package de.hybris.platform.solrfacetsearch.daos;

import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.List;

public interface SolrIndexedTypeDao extends GenericDao<SolrIndexedTypeModel>
{
    List<SolrIndexedTypeModel> findAllIndexedTypes();


    SolrIndexedTypeModel findIndexedTypeByIdentifier(String paramString);
}
