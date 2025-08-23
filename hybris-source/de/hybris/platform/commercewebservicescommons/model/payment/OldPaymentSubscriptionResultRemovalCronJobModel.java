package de.hybris.platform.commercewebservicescommons.model.payment;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OldPaymentSubscriptionResultRemovalCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "OldPaymentSubscriptionResultRemovalCronJob";
    public static final String AGE = "age";


    public OldPaymentSubscriptionResultRemovalCronJobModel()
    {
    }


    public OldPaymentSubscriptionResultRemovalCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OldPaymentSubscriptionResultRemovalCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OldPaymentSubscriptionResultRemovalCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "age", type = Accessor.Type.GETTER)
    public Integer getAge()
    {
        return (Integer)getPersistenceContext().getPropertyValue("age");
    }


    @Accessor(qualifier = "age", type = Accessor.Type.SETTER)
    public void setAge(Integer value)
    {
        getPersistenceContext().setPropertyValue("age", value);
    }
}
