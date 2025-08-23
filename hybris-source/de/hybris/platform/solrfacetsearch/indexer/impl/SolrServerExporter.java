package de.hybris.platform.solrfacetsearch.indexer.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContextFactory;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.ExporterException;
import de.hybris.platform.solrfacetsearch.indexer.spi.Exporter;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProviderFactory;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class SolrServerExporter implements Exporter
{
    private static final Logger LOG = LoggerFactory.getLogger(SolrServerExporter.class);
    private IndexerBatchContextFactory<IndexerBatchContext> indexerBatchContextFactory;
    private SolrSearchProviderFactory solrSearchProviderFactory;


    public IndexerBatchContextFactory<IndexerBatchContext> getIndexerBatchContextFactory()
    {
        return this.indexerBatchContextFactory;
    }


    @Required
    public void setIndexerBatchContextFactory(IndexerBatchContextFactory<IndexerBatchContext> indexerBatchContextFactory)
    {
        this.indexerBatchContextFactory = indexerBatchContextFactory;
    }


    public SolrSearchProviderFactory getSolrSearchProviderFactory()
    {
        return this.solrSearchProviderFactory;
    }


    @Required
    public void setSolrSearchProviderFactory(SolrSearchProviderFactory solrSearchProviderFactory)
    {
        this.solrSearchProviderFactory = solrSearchProviderFactory;
    }


    public void exportToUpdateIndex(Collection<SolrInputDocument> solrDocuments, FacetSearchConfig facetSearchConfig, IndexedType indexedType) throws ExporterException
    {
        if(CollectionUtils.isEmpty(solrDocuments))
        {
            LOG.warn("solrDocuments should not be empty");
            return;
        }
        SolrClient solrClient = null;
        try
        {
            IndexerBatchContext batchContext = this.indexerBatchContextFactory.getContext();
            Index index = batchContext.getIndex();
            SolrSearchProvider solrSearchProvider = this.solrSearchProviderFactory.getSearchProvider(facetSearchConfig, indexedType);
            solrClient = solrSearchProvider.getClientForIndexing(index);
            solrClient.add(index.getName(), solrDocuments);
        }
        catch(SolrServiceException | org.apache.solr.client.solrj.SolrServerException | java.io.IOException exception)
        {
            throw new ExporterException(exception.getMessage(), exception);
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)solrClient);
        }
    }


    public void exportToDeleteFromIndex(Collection<String> idsToDelete, FacetSearchConfig facetSearchConfig, IndexedType indexedType) throws ExporterException
    {
        if(CollectionUtils.isEmpty(idsToDelete))
        {
            LOG.warn("idsToDelete should not be empty");
            return;
        }
        SolrClient solrClient = null;
        try
        {
            IndexerBatchContext batchContext = this.indexerBatchContextFactory.getContext();
            Index index = batchContext.getIndex();
            SolrSearchProvider solrSearchProvider = this.solrSearchProviderFactory.getSearchProvider(facetSearchConfig, indexedType);
            solrClient = solrSearchProvider.getClientForIndexing(index);
            solrClient.deleteById(index.getName(), new ArrayList<>(idsToDelete));
        }
        catch(SolrServiceException | org.apache.solr.client.solrj.SolrServerException | java.io.IOException exception)
        {
            throw new ExporterException(exception.getMessage(), exception);
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)solrClient);
        }
    }
}
