package de.hybris.platform.solrfacetsearch.indexer.workers;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;

public interface IndexerWorkerFactory
{
    IndexerWorker createIndexerWorker(FacetSearchConfig paramFacetSearchConfig) throws IndexerException;
}
