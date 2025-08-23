package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;

public class CleanUpCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "CleanUpCronJob";
    public static final String XDAYSOLD = "xDaysOld";
    public static final String EXCLUDECRONJOBS = "excludeCronJobs";
    public static final String RESULTCOLL = "resultcoll";
    public static final String STATUSCOLL = "statuscoll";


    public CleanUpCronJobModel()
    {
    }


    public CleanUpCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CleanUpCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CleanUpCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "excludeCronJobs", type = Accessor.Type.GETTER)
    public List<CronJobModel> getExcludeCronJobs()
    {
        return (List<CronJobModel>)getPersistenceContext().getPropertyValue("excludeCronJobs");
    }


    @Accessor(qualifier = "resultcoll", type = Accessor.Type.GETTER)
    public Collection<CronJobResult> getResultcoll()
    {
        return (Collection<CronJobResult>)getPersistenceContext().getPropertyValue("resultcoll");
    }


    @Accessor(qualifier = "statuscoll", type = Accessor.Type.GETTER)
    public Collection<CronJobStatus> getStatuscoll()
    {
        return (Collection<CronJobStatus>)getPersistenceContext().getPropertyValue("statuscoll");
    }


    @Accessor(qualifier = "xDaysOld", type = Accessor.Type.GETTER)
    public int getXDaysOld()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("xDaysOld"));
    }


    @Accessor(qualifier = "excludeCronJobs", type = Accessor.Type.SETTER)
    public void setExcludeCronJobs(List<CronJobModel> value)
    {
        getPersistenceContext().setPropertyValue("excludeCronJobs", value);
    }


    @Accessor(qualifier = "resultcoll", type = Accessor.Type.SETTER)
    public void setResultcoll(Collection<CronJobResult> value)
    {
        getPersistenceContext().setPropertyValue("resultcoll", value);
    }


    @Accessor(qualifier = "statuscoll", type = Accessor.Type.SETTER)
    public void setStatuscoll(Collection<CronJobStatus> value)
    {
        getPersistenceContext().setPropertyValue("statuscoll", value);
    }


    @Accessor(qualifier = "xDaysOld", type = Accessor.Type.SETTER)
    public void setXDaysOld(int value)
    {
        getPersistenceContext().setPropertyValue("xDaysOld", toObject(value));
    }
}
