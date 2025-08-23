package de.hybris.platform.solrfacetsearch.indexer.cron;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.indexer.cron.SolrIndexerHotUpdateCronJobModel;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

public class SolrIndexerHotUpdateJob extends AbstractIndexerJob
{
    private static final Logger LOG = Logger.getLogger(SolrIndexerHotUpdateJob.class);


    public PerformResult performIndexingJob(CronJobModel cronJob)
    {
        LOG.info("Started indexer hot-update cronjob.");
        if(!(cronJob instanceof SolrIndexerHotUpdateCronJobModel))
        {
            LOG.warn("Unexpected cronjob type: " + cronJob);
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
        }
        SolrIndexerHotUpdateCronJobModel indexerCronJob = (SolrIndexerHotUpdateCronJobModel)cronJob;
        Collection<ItemModel> items = indexerCronJob.getItems();
        if(CollectionUtils.isEmpty(items))
        {
            LOG.info("Nothing to index");
            return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
        }
        SolrFacetSearchConfigModel facetSearchConfigModel = indexerCronJob.getFacetSearchConfig();
        FacetSearchConfig facetSearchConfig = getFacetSearchConfig(facetSearchConfigModel);
        if(facetSearchConfig == null)
        {
            return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
        }
        try
        {
            indexItems(indexerCronJob, facetSearchConfig, items);
        }
        catch(IndexerException e)
        {
            LOG.warn("Error during indexer call: " + facetSearchConfigModel.getName() + " \n" + e);
            return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
        }
        LOG.info("Finished indexer hot-update cronjob.");
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    protected void indexItems(SolrIndexerHotUpdateCronJobModel indexerCronJob, FacetSearchConfig facetSearchConfig, Collection<ItemModel> items) throws IndexerException
    {
        IndexerOperationValues indexerOperation = indexerCronJob.getIndexerOperation();
        IndexedType indexedType = (IndexedType)facetSearchConfig.getIndexConfig().getIndexedTypes().get(indexerCronJob.getIndexTypeName());
        List<PK> pks = (List<PK>)items.stream().map(item -> item.getPk()).collect(Collectors.toList());
        switch(null.$SwitchMap$de$hybris$platform$solrfacetsearch$enums$IndexerOperationValues[indexerOperation.ordinal()])
        {
            case 1:
                this.indexerService.updateTypeIndex(facetSearchConfig, indexedType, pks);
                return;
            case 2:
                this.indexerService.deleteTypeIndex(facetSearchConfig, indexedType, pks);
                return;
        }
        throw new IndexerException("Unsupported indexer operation: " + indexerOperation);
    }
}
