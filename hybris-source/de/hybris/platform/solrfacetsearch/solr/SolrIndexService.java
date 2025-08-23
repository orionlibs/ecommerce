package de.hybris.platform.solrfacetsearch.solr;

import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.util.List;

public interface SolrIndexService
{
    SolrIndexModel createIndex(String paramString1, String paramString2, String paramString3) throws SolrServiceException;


    List<SolrIndexModel> getAllIndexes() throws SolrServiceException;


    List<SolrIndexModel> getIndexesForConfigAndType(String paramString1, String paramString2) throws SolrServiceException;


    SolrIndexModel getIndex(String paramString1, String paramString2, String paramString3) throws SolrServiceException;


    SolrIndexModel getOrCreateIndex(String paramString1, String paramString2, String paramString3) throws SolrServiceException;


    void deleteIndex(String paramString1, String paramString2, String paramString3) throws SolrServiceException;


    SolrIndexModel activateIndex(String paramString1, String paramString2, String paramString3) throws SolrServiceException;


    SolrIndexModel getActiveIndex(String paramString1, String paramString2) throws SolrServiceException;
}
