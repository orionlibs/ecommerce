package de.hybris.platform.commercewebservicescommons.model.expressupdate.cron;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OrderStatusUpdateCleanerCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "OrderStatusUpdateCleanerCronJob";
    public static final String QUEUETIMELIMIT = "queueTimeLimit";


    public OrderStatusUpdateCleanerCronJobModel()
    {
    }


    public OrderStatusUpdateCleanerCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderStatusUpdateCleanerCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderStatusUpdateCleanerCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "queueTimeLimit", type = Accessor.Type.GETTER)
    public Integer getQueueTimeLimit()
    {
        return (Integer)getPersistenceContext().getPropertyValue("queueTimeLimit");
    }


    @Accessor(qualifier = "queueTimeLimit", type = Accessor.Type.SETTER)
    public void setQueueTimeLimit(Integer value)
    {
        getPersistenceContext().setPropertyValue("queueTimeLimit", value);
    }
}
