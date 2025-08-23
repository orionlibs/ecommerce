package de.hybris.platform.solrfacetsearch.indexer.cron;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.indexer.cron.SolrIndexerCronJobModel;
import java.util.Map;
import org.apache.log4j.Logger;

public class SolrIndexerJob extends AbstractIndexerJob
{
    private static final Logger LOG = Logger.getLogger(SolrIndexerJob.class);


    public PerformResult performIndexingJob(CronJobModel cronJob)
    {
        LOG.info("Started indexer cronjob.");
        if(!(cronJob instanceof SolrIndexerCronJobModel))
        {
            LOG.warn("Unexpected cronjob type: " + cronJob);
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
        }
        SolrIndexerCronJobModel solrIndexerCronJob = (SolrIndexerCronJobModel)cronJob;
        SolrFacetSearchConfigModel facetSearchConfigModel = solrIndexerCronJob.getFacetSearchConfig();
        FacetSearchConfig facetSearchConfig = getFacetSearchConfig(facetSearchConfigModel);
        if(facetSearchConfig == null)
        {
            return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
        }
        try
        {
            indexItems(solrIndexerCronJob, facetSearchConfig);
        }
        catch(IndexerException e)
        {
            LOG.warn("Error during indexer call: " + facetSearchConfigModel.getName(), (Throwable)e);
            return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
        }
        LOG.info("Finished indexer cronjob.");
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    protected void indexItems(SolrIndexerCronJobModel solrIndexerCronJob, FacetSearchConfig facetSearchConfig) throws IndexerException
    {
        Map<String, String> indexerHints = solrIndexerCronJob.getIndexerHints();
        IndexerOperationValues indexerOperation = solrIndexerCronJob.getIndexerOperation();
        switch(null.$SwitchMap$de$hybris$platform$solrfacetsearch$enums$IndexerOperationValues[indexerOperation.ordinal()])
        {
            case 1:
                this.indexerService.performFullIndex(facetSearchConfig, indexerHints);
                return;
            case 2:
                this.indexerService.updateIndex(facetSearchConfig, indexerHints);
                return;
            case 3:
                this.indexerService.deleteFromIndex(facetSearchConfig, indexerHints);
                return;
        }
        throw new IndexerException("Unsupported indexer operation: " + indexerOperation);
    }
}
