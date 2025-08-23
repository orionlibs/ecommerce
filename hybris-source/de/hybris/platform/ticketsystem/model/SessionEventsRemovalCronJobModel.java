package de.hybris.platform.ticketsystem.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SessionEventsRemovalCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "SessionEventsRemovalCronJob";


    public SessionEventsRemovalCronJobModel()
    {
    }


    public SessionEventsRemovalCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SessionEventsRemovalCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SessionEventsRemovalCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }
}
