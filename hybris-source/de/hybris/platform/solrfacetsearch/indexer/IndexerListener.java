package de.hybris.platform.solrfacetsearch.indexer;

import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;

public interface IndexerListener
{
    void beforeIndex(IndexerContext paramIndexerContext) throws IndexerException;


    void afterIndex(IndexerContext paramIndexerContext) throws IndexerException;


    void afterIndexError(IndexerContext paramIndexerContext) throws IndexerException;
}
