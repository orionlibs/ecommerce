package de.hybris.platform.solrfacetsearch.daos;

import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.model.SolrIndexOperationModel;
import java.util.Optional;

public interface SolrIndexOperationDao extends GenericDao<SolrIndexOperationModel>
{
    SolrIndexOperationModel findIndexOperationById(long paramLong);


    Optional<SolrIndexOperationModel> findLastSuccesfulIndexOperation(SolrIndexModel paramSolrIndexModel);
}
