package de.hybris.platform.solrfacetsearch.indexer.listeners;

import de.hybris.platform.solrfacetsearch.config.CommitMode;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchListener;
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

public class CommitModeListener implements IndexerListener, IndexerBatchListener
{
    private static final Logger LOG = Logger.getLogger(CommitModeListener.class);
    protected static final CommitMode DEFAULT_COMMIT_MODE = CommitMode.AFTER_INDEX;
    protected static final String COMMIT_MODE_HINT = "commitMode";
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
        CommitMode commitMode = resolveCommitMode(context.getFacetSearchConfig(), context.getIndexerHints());
        switch(null.$SwitchMap$de$hybris$platform$solrfacetsearch$config$CommitMode[commitMode.ordinal()])
        {
            case 1:
            case 2:
                commit(context.getFacetSearchConfig(), context.getIndexedType(), context.getIndex(), SolrSearchProvider.CommitType.HARD);
                break;
        }
    }


    public void afterIndexError(IndexerContext context) throws IndexerException
    {
    }


    public void beforeBatch(IndexerBatchContext batchContext) throws IndexerException
    {
    }


    public void afterBatch(IndexerBatchContext batchContext) throws IndexerException
    {
        if(CollectionUtils.isEmpty(batchContext.getPks()))
        {
            return;
        }
        CommitMode commitMode = resolveCommitMode(batchContext.getFacetSearchConfig(), batchContext.getIndexerHints());
        switch(null.$SwitchMap$de$hybris$platform$solrfacetsearch$config$CommitMode[commitMode.ordinal()])
        {
            case 3:
                commit(batchContext.getFacetSearchConfig(), batchContext.getIndexedType(), batchContext.getIndex(), SolrSearchProvider.CommitType.HARD);
                break;
            case 2:
                commit(batchContext.getFacetSearchConfig(), batchContext.getIndexedType(), batchContext.getIndex(), SolrSearchProvider.CommitType.SOFT);
                break;
        }
    }


    public void afterBatchError(IndexerBatchContext batchContext) throws IndexerException
    {
    }


    protected CommitMode resolveCommitMode(FacetSearchConfig facetSearchConfig, Map<String, String> indexerHints)
    {
        String commitModeHint = indexerHints.get("commitMode");
        if(StringUtils.isNotBlank(commitModeHint))
        {
            try
            {
                return CommitMode.valueOf(commitModeHint);
            }
            catch(IllegalArgumentException e)
            {
                LOG.error("Invalid commitMode indexer hint " + commitModeHint, e);
            }
        }
        IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
        CommitMode commitMode = indexConfig.getCommitMode();
        if(commitMode != null)
        {
            return commitMode;
        }
        return DEFAULT_COMMIT_MODE;
    }


    protected void commit(FacetSearchConfig facetSearchConfig, IndexedType indexedType, Index index, SolrSearchProvider.CommitType commitType) throws IndexerException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Performing " + commitType + " commit on " + index.getName() + " (" + facetSearchConfig.getName() + "/" + indexedType
                            .getUniqueIndexedTypeCode() + ")");
        }
        try
        {
            SolrSearchProvider solrSearchProvider = this.solrSearchProviderFactory.getSearchProvider(facetSearchConfig, indexedType);
            solrSearchProvider.commit(index, commitType);
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
