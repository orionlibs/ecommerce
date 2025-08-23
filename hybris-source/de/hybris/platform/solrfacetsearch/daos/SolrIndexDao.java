package de.hybris.platform.solrfacetsearch.daos;

import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.List;

public interface SolrIndexDao extends GenericDao<SolrIndexModel>
{
    List<SolrIndexModel> findAllIndexes();


    List<SolrIndexModel> findIndexesByConfigAndType(SolrFacetSearchConfigModel paramSolrFacetSearchConfigModel, SolrIndexedTypeModel paramSolrIndexedTypeModel);


    SolrIndexModel findIndexByConfigAndTypeAndQualifier(SolrFacetSearchConfigModel paramSolrFacetSearchConfigModel, SolrIndexedTypeModel paramSolrIndexedTypeModel, String paramString);


    SolrIndexModel findActiveIndexByConfigAndType(SolrFacetSearchConfigModel paramSolrFacetSearchConfigModel, SolrIndexedTypeModel paramSolrIndexedTypeModel);
}
