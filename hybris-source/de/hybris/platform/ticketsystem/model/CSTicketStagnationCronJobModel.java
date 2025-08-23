package de.hybris.platform.ticketsystem.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

@Deprecated(since = "6.6", forRemoval = true)
public class CSTicketStagnationCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "CSTicketStagnationCronJob";
    public static final String STAGNATIONPERIOD = "stagnationPeriod";
    public static final String ELIGIBLESTATES = "eligibleStates";


    public CSTicketStagnationCronJobModel()
    {
    }


    public CSTicketStagnationCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CSTicketStagnationCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CSTicketStagnationCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "eligibleStates", type = Accessor.Type.GETTER)
    public String getEligibleStates()
    {
        return (String)getPersistenceContext().getPropertyValue("eligibleStates");
    }


    @Accessor(qualifier = "stagnationPeriod", type = Accessor.Type.GETTER)
    public int getStagnationPeriod()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("stagnationPeriod"));
    }


    @Accessor(qualifier = "eligibleStates", type = Accessor.Type.SETTER)
    public void setEligibleStates(String value)
    {
        getPersistenceContext().setPropertyValue("eligibleStates", value);
    }


    @Accessor(qualifier = "stagnationPeriod", type = Accessor.Type.SETTER)
    public void setStagnationPeriod(int value)
    {
        getPersistenceContext().setPropertyValue("stagnationPeriod", toObject(value));
    }
}
