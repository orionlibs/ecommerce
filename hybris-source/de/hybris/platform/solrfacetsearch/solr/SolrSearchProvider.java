package de.hybris.platform.solrfacetsearch.solr;

import de.hybris.platform.core.PK;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.util.Collection;
import org.apache.solr.client.solrj.SolrClient;

public interface SolrSearchProvider
{
    Index resolveIndex(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, String paramString);


    SolrClient getClient(Index paramIndex) throws SolrServiceException;


    SolrClient getClientForIndexing(Index paramIndex) throws SolrServiceException;


    void createIndex(Index paramIndex) throws SolrServiceException;


    void deleteIndex(Index paramIndex) throws SolrServiceException;


    void exportConfig(Index paramIndex) throws SolrServiceException;


    void deleteAllDocuments(Index paramIndex) throws SolrServiceException;


    void deleteOldDocuments(Index paramIndex, long paramLong) throws SolrServiceException;


    void deleteDocumentsByPk(Index paramIndex, Collection<PK> paramCollection) throws SolrServiceException;


    void commit(Index paramIndex, CommitType paramCommitType) throws SolrServiceException;


    void optimize(Index paramIndex) throws SolrServiceException;
}
