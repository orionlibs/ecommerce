package de.hybris.platform.solrfacetsearch.indexer;

import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;

public interface ExtendedIndexerBatchListener extends IndexerBatchListener
{
    void afterPrepareContext(IndexerBatchContext paramIndexerBatchContext) throws IndexerException;
}
