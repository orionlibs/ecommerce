package de.hybris.platform.solrfacetsearch.solr.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.IndexNameResolver;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.util.Collection;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.beans.factory.annotation.Required;

public class XmlExportSearchProvider implements SolrSearchProvider
{
    private IndexNameResolver indexNameResolver;


    public Index resolveIndex(FacetSearchConfig facetSearchConfig, IndexedType indexedType, String qualifier)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("facetSearchConfig", facetSearchConfig);
        ServicesUtil.validateParameterNotNullStandardMessage("indexType", indexedType);
        ServicesUtil.validateParameterNotNullStandardMessage("qualifier", indexedType);
        String indexName = this.indexNameResolver.resolve(facetSearchConfig, indexedType, qualifier);
        DefaultIndex index = new DefaultIndex();
        index.setName(indexName);
        index.setFacetSearchConfig(facetSearchConfig);
        index.setIndexedType(indexedType);
        index.setQualifier(qualifier);
        return (Index)index;
    }


    public SolrClient getClient(Index index) throws SolrServiceException
    {
        throw new UnsupportedOperationException();
    }


    public SolrClient getClientForIndexing(Index index) throws SolrServiceException
    {
        throw new UnsupportedOperationException();
    }


    public void createIndex(Index index) throws SolrServiceException
    {
    }


    public void deleteIndex(Index index) throws SolrServiceException
    {
    }


    public void exportConfig(Index index) throws SolrServiceException
    {
    }


    public void commit(Index index, SolrSearchProvider.CommitType commitType) throws SolrServiceException
    {
    }


    public void optimize(Index index) throws SolrServiceException
    {
    }


    public void deleteAllDocuments(Index index) throws SolrServiceException
    {
    }


    public void deleteOldDocuments(Index index, long indexOperationId) throws SolrServiceException
    {
    }


    public void deleteDocumentsByPk(Index index, Collection<PK> pks)
    {
    }


    public IndexNameResolver getIndexNameResolver()
    {
        return this.indexNameResolver;
    }


    @Required
    public void setIndexNameResolver(IndexNameResolver indexNameResolver)
    {
        this.indexNameResolver = indexNameResolver;
    }
}
