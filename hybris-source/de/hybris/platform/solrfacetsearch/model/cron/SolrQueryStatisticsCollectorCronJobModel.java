package de.hybris.platform.solrfacetsearch.model.cron;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SolrQueryStatisticsCollectorCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "SolrQueryStatisticsCollectorCronJob";


    public SolrQueryStatisticsCollectorCronJobModel()
    {
    }


    public SolrQueryStatisticsCollectorCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrQueryStatisticsCollectorCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrQueryStatisticsCollectorCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }
}
