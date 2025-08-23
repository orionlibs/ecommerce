package de.hybris.platform.solrfacetsearch.solr;

import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.model.SolrIndexOperationModel;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.util.Date;

public interface SolrIndexOperationService
{
    SolrIndexOperationModel getOperationForId(long paramLong) throws SolrServiceException;


    SolrIndexOperationModel startOperation(SolrIndexModel paramSolrIndexModel, long paramLong, IndexOperation paramIndexOperation, boolean paramBoolean) throws SolrServiceException;


    SolrIndexOperationModel endOperation(long paramLong, boolean paramBoolean) throws SolrServiceException;


    SolrIndexOperationModel cancelOperation(long paramLong) throws SolrServiceException;


    Date getLastIndexOperationTime(SolrIndexModel paramSolrIndexModel) throws SolrServiceException;
}
