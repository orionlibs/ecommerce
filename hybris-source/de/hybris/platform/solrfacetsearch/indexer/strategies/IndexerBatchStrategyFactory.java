package de.hybris.platform.solrfacetsearch.indexer.strategies;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;

public interface IndexerBatchStrategyFactory
{
    IndexerBatchStrategy createIndexerBatchStrategy(FacetSearchConfig paramFacetSearchConfig) throws IndexerException;
}
