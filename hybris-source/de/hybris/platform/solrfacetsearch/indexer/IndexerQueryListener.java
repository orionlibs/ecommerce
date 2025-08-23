package de.hybris.platform.solrfacetsearch.indexer;

import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;

public interface IndexerQueryListener
{
    void beforeQuery(IndexerQueryContext paramIndexerQueryContext) throws IndexerException;


    void afterQuery(IndexerQueryContext paramIndexerQueryContext) throws IndexerException;


    void afterQueryError(IndexerQueryContext paramIndexerQueryContext) throws IndexerException;
}
