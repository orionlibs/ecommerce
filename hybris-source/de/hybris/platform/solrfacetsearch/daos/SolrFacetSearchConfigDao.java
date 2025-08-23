package de.hybris.platform.solrfacetsearch.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import java.util.List;

public interface SolrFacetSearchConfigDao extends GenericDao<SolrFacetSearchConfigModel>
{
    List<SolrFacetSearchConfigModel> findAllFacetSearchConfigs();


    SolrFacetSearchConfigModel findFacetSearchConfigByName(String paramString);


    List<SolrFacetSearchConfigModel> findFacetSearchConfigsByCatalogVersion(CatalogVersionModel paramCatalogVersionModel);
}
