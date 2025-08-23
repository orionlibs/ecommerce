package de.hybris.platform.solrfacetsearch.provider;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;

public interface ValueFilter
{
    Object doFilter(IndexerBatchContext paramIndexerBatchContext, IndexedProperty paramIndexedProperty, Object paramObject);
}
