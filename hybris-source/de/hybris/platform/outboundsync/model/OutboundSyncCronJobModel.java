package de.hybris.platform.outboundsync.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OutboundSyncCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "OutboundSyncCronJob";
    public static final String _JOBCRONJOBRELATION = "JobCronJobRelation";


    public OutboundSyncCronJobModel()
    {
    }


    public OutboundSyncCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OutboundSyncCronJobModel(OutboundSyncJobModel _job)
    {
        setJob((JobModel)_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OutboundSyncCronJobModel(OutboundSyncJobModel _job, ItemModel _owner)
    {
        setJob((JobModel)_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "job", type = Accessor.Type.GETTER)
    public OutboundSyncJobModel getJob()
    {
        return (OutboundSyncJobModel)super.getJob();
    }


    @Accessor(qualifier = "job", type = Accessor.Type.SETTER)
    public void setJob(JobModel value)
    {
        if(value == null || value instanceof OutboundSyncJobModel)
        {
            super.setJob(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.outboundsync.model.OutboundSyncJobModel");
        }
    }
}
