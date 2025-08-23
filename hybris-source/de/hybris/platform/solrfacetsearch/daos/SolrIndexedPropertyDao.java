package de.hybris.platform.solrfacetsearch.daos;

import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.List;

public interface SolrIndexedPropertyDao extends GenericDao<SolrIndexedPropertyModel>
{
    List<SolrIndexedPropertyModel> findIndexedPropertiesByIndexedType(SolrIndexedTypeModel paramSolrIndexedTypeModel);


    SolrIndexedPropertyModel findIndexedPropertyByName(SolrIndexedTypeModel paramSolrIndexedTypeModel, String paramString);
}
