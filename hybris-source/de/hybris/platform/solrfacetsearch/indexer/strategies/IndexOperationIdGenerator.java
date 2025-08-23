package de.hybris.platform.solrfacetsearch.indexer.strategies;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.solr.Index;

public interface IndexOperationIdGenerator
{
    long generate(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, Index paramIndex);
}
