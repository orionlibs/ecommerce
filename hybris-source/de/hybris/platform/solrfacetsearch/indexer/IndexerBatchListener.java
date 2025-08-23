package de.hybris.platform.solrfacetsearch.indexer;

import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;

public interface IndexerBatchListener
{
    void beforeBatch(IndexerBatchContext paramIndexerBatchContext) throws IndexerException;


    void afterBatch(IndexerBatchContext paramIndexerBatchContext) throws IndexerException;


    void afterBatchError(IndexerBatchContext paramIndexerBatchContext) throws IndexerException;
}
