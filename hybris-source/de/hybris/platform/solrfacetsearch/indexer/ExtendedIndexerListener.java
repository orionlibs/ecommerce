package de.hybris.platform.solrfacetsearch.indexer;

import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;

public interface ExtendedIndexerListener extends IndexerListener
{
    void afterPrepareContext(IndexerContext paramIndexerContext) throws IndexerException;
}
