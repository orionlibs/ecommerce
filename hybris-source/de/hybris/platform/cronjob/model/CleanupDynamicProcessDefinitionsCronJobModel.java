package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CleanupDynamicProcessDefinitionsCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "CleanupDynamicProcessDefinitionsCronJob";
    public static final String TIMETHRESHOLD = "timeThreshold";
    public static final String VERSIONTHRESHOLD = "versionThreshold";


    public CleanupDynamicProcessDefinitionsCronJobModel()
    {
    }


    public CleanupDynamicProcessDefinitionsCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CleanupDynamicProcessDefinitionsCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CleanupDynamicProcessDefinitionsCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "timeThreshold", type = Accessor.Type.GETTER)
    public Integer getTimeThreshold()
    {
        return (Integer)getPersistenceContext().getPropertyValue("timeThreshold");
    }


    @Accessor(qualifier = "versionThreshold", type = Accessor.Type.GETTER)
    public Integer getVersionThreshold()
    {
        return (Integer)getPersistenceContext().getPropertyValue("versionThreshold");
    }


    @Accessor(qualifier = "timeThreshold", type = Accessor.Type.SETTER)
    public void setTimeThreshold(Integer value)
    {
        getPersistenceContext().setPropertyValue("timeThreshold", value);
    }


    @Accessor(qualifier = "versionThreshold", type = Accessor.Type.SETTER)
    public void setVersionThreshold(Integer value)
    {
        getPersistenceContext().setPropertyValue("versionThreshold", value);
    }
}
