package de.hybris.platform.solrfacetsearch.reporting.cron;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.solrfacetsearch.model.cron.SolrQueryStatisticsCollectorCronJobModel;
import de.hybris.platform.solrfacetsearch.reporting.AggregatedQueryStatsService;
import de.hybris.platform.solrfacetsearch.reporting.SolrQueryStatisticsAggregator;
import de.hybris.platform.solrfacetsearch.reporting.data.AggregatedSearchQueryInfo;
import java.util.List;
import org.apache.log4j.Logger;

public class SolrQueryStatisticsCollectorJob extends AbstractJobPerformable<SolrQueryStatisticsCollectorCronJobModel>
{
    private static final Logger LOG = Logger.getLogger(SolrQueryStatisticsCollectorJob.class);
    private SolrQueryStatisticsAggregator solrQueryStatisticsAggregator;
    private AggregatedQueryStatsService aggregatedQueryStatsService;
    private boolean enableCollectingStatistics;


    public PerformResult perform(SolrQueryStatisticsCollectorCronJobModel cronJob)
    {
        if(this.enableCollectingStatistics)
        {
            try
            {
                List<AggregatedSearchQueryInfo> aggregatedStatistics = this.solrQueryStatisticsAggregator.aggregate();
                this.aggregatedQueryStatsService.save(aggregatedStatistics);
            }
            catch(Exception ex)
            {
                LOG.error("Error executing SolrQueryStatisticsCollectorJob", ex);
                return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
            }
        }
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    public void setSolrQueryStatisticsAggregator(SolrQueryStatisticsAggregator solrQueryStatisticsAggregator)
    {
        this.solrQueryStatisticsAggregator = solrQueryStatisticsAggregator;
    }


    public void setAggregatedQueryStatsService(AggregatedQueryStatsService aggregatedQueryStatsService)
    {
        this.aggregatedQueryStatsService = aggregatedQueryStatsService;
    }


    public void setEnableCollectingStatistics(boolean enableCollectingStatistics)
    {
        this.enableCollectingStatistics = enableCollectingStatistics;
    }
}
