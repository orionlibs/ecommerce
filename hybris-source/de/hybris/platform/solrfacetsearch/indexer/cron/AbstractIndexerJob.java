package de.hybris.platform.solrfacetsearch.indexer.cron;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerService;
import de.hybris.platform.solrfacetsearch.indexer.spi.Indexer;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import org.apache.log4j.Logger;

public abstract class AbstractIndexerJob extends AbstractJobPerformable
{
    private static final Logger LOG = Logger.getLogger(AbstractIndexerJob.class);
    protected IndexerService indexerService;
    protected FacetSearchConfigService facetSearchConfigService;
    protected Indexer indexer;


    public PerformResult perform(CronJobModel cronJob)
    {
        return performIndexingJob(cronJob);
    }


    public abstract PerformResult performIndexingJob(CronJobModel paramCronJobModel);


    public void setIndexerService(IndexerService indexerService)
    {
        this.indexerService = indexerService;
    }


    public void setFacetSearchConfigService(FacetSearchConfigService facetSearchConfigService)
    {
        this.facetSearchConfigService = facetSearchConfigService;
    }


    protected FacetSearchConfig getFacetSearchConfig(SolrFacetSearchConfigModel facetSearchConfigModel)
    {
        try
        {
            return this.facetSearchConfigService.getConfiguration(facetSearchConfigModel.getName());
        }
        catch(FacetConfigServiceException e)
        {
            LOG.warn("Configuration service could not load configuration with name: " + facetSearchConfigModel.getName(), (Throwable)e);
            return null;
        }
    }


    public void setIndexer(Indexer indexer)
    {
        this.indexer = indexer;
    }
}
