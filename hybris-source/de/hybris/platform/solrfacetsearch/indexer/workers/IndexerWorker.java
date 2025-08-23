package de.hybris.platform.solrfacetsearch.indexer.workers;

public interface IndexerWorker extends Runnable
{
    void initialize(IndexerWorkerParameters paramIndexerWorkerParameters);


    boolean isInitialized();
}
