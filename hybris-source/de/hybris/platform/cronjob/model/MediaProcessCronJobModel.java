package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class MediaProcessCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "MediaProcessCronJob";
    public static final String JOBMEDIA = "jobMedia";
    public static final String CURRENTLINE = "currentLine";
    public static final String LASTSUCCESSFULLINE = "lastSuccessfulLine";


    public MediaProcessCronJobModel()
    {
    }


    public MediaProcessCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaProcessCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaProcessCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "currentLine", type = Accessor.Type.GETTER)
    public Integer getCurrentLine()
    {
        return (Integer)getPersistenceContext().getPropertyValue("currentLine");
    }


    @Accessor(qualifier = "jobMedia", type = Accessor.Type.GETTER)
    public JobMediaModel getJobMedia()
    {
        return (JobMediaModel)getPersistenceContext().getPropertyValue("jobMedia");
    }


    @Accessor(qualifier = "lastSuccessfulLine", type = Accessor.Type.GETTER)
    public Integer getLastSuccessfulLine()
    {
        return (Integer)getPersistenceContext().getPropertyValue("lastSuccessfulLine");
    }


    @Accessor(qualifier = "currentLine", type = Accessor.Type.SETTER)
    public void setCurrentLine(Integer value)
    {
        getPersistenceContext().setPropertyValue("currentLine", value);
    }


    @Accessor(qualifier = "jobMedia", type = Accessor.Type.SETTER)
    public void setJobMedia(JobMediaModel value)
    {
        getPersistenceContext().setPropertyValue("jobMedia", value);
    }


    @Accessor(qualifier = "lastSuccessfulLine", type = Accessor.Type.SETTER)
    public void setLastSuccessfulLine(Integer value)
    {
        getPersistenceContext().setPropertyValue("lastSuccessfulLine", value);
    }
}
