package de.hybris.platform.solrfacetsearch.indexer.listeners;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.OptimizeMode;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerListener;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProviderFactory;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class OptimizeModeListener implements IndexerListener
{
    private static final Logger LOG = Logger.getLogger(OptimizeModeListener.class);
    protected static final OptimizeMode DEFAULT_OPTIMIZE_MODE = OptimizeMode.NEVER;
    protected static final String OPTIMIZE_MODE_HINT = "optimizeMode";
    private SolrSearchProviderFactory solrSearchProviderFactory;


    public void beforeIndex(IndexerContext context) throws IndexerException
    {
    }


    public void afterIndex(IndexerContext context) throws IndexerException
    {
        if(CollectionUtils.isEmpty(context.getPks()))
        {
            return;
        }
        OptimizeMode optimizeMode = resolveOptimizeMode(context.getFacetSearchConfig(), context.getIndexerHints());
        if(optimizeMode == OptimizeMode.AFTER_INDEX || (optimizeMode == OptimizeMode.AFTER_FULL_INDEX && context.getIndexOperation() == IndexOperation.FULL))
        {
            optimize(context.getFacetSearchConfig(), context.getIndexedType(), context.getIndex());
        }
    }


    public void afterIndexError(IndexerContext context) throws IndexerException
    {
    }


    protected OptimizeMode resolveOptimizeMode(FacetSearchConfig facetSearchConfig, Map<String, String> indexerHints)
    {
        String optimizeModeHint = indexerHints.get("optimizeMode");
        if(StringUtils.isNotBlank(optimizeModeHint))
        {
            try
            {
                return OptimizeMode.valueOf(optimizeModeHint);
            }
            catch(IllegalArgumentException e)
            {
                LOG.error("Invalid optimizeMode indexer hint" + optimizeModeHint, e);
            }
        }
        IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
        OptimizeMode optimizeMode = indexConfig.getOptimizeMode();
        if(optimizeMode != null)
        {
            return optimizeMode;
        }
        return DEFAULT_OPTIMIZE_MODE;
    }


    protected void optimize(FacetSearchConfig facetSearchConfig, IndexedType indexedType, Index index) throws IndexerException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Performing optimize on " + index.getName() + " (" + facetSearchConfig.getName() + "/" + indexedType
                            .getUniqueIndexedTypeCode() + ")");
        }
        try
        {
            SolrSearchProvider solrSearchProvider = this.solrSearchProviderFactory.getSearchProvider(facetSearchConfig, indexedType);
            solrSearchProvider.optimize(index);
        }
        catch(SolrServiceException e)
        {
            throw new IndexerException(e);
        }
    }


    @Required
    public void setSolrSearchProviderFactory(SolrSearchProviderFactory solrSearchProviderFactory)
    {
        this.solrSearchProviderFactory = solrSearchProviderFactory;
    }


    public SolrSearchProviderFactory getSolrSearchProviderFactory()
    {
        return this.solrSearchProviderFactory;
    }
}
