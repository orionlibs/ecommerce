package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class JobLogModel extends ItemModel
{
    public static final String _TYPECODE = "JobLog";
    public static final String _CRONJOBJOBLOGSRELATION = "CronJobJobLogsRelation";
    public static final String STEP = "step";
    public static final String MESSAGE = "message";
    public static final String SHORTMESSAGE = "shortMessage";
    public static final String LEVEL = "level";
    public static final String CRONJOB = "cronJob";


    public JobLogModel()
    {
    }


    public JobLogModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public JobLogModel(CronJobModel _cronJob, JobLogLevel _level)
    {
        setCronJob(_cronJob);
        setLevel(_level);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public JobLogModel(CronJobModel _cronJob, JobLogLevel _level, String _message, ItemModel _owner, StepModel _step)
    {
        setCronJob(_cronJob);
        setLevel(_level);
        setMessage(_message);
        setOwner(_owner);
        setStep(_step);
    }


    @Accessor(qualifier = "cronJob", type = Accessor.Type.GETTER)
    public CronJobModel getCronJob()
    {
        return (CronJobModel)getPersistenceContext().getPropertyValue("cronJob");
    }


    @Accessor(qualifier = "level", type = Accessor.Type.GETTER)
    public JobLogLevel getLevel()
    {
        return (JobLogLevel)getPersistenceContext().getPropertyValue("level");
    }


    @Accessor(qualifier = "message", type = Accessor.Type.GETTER)
    public String getMessage()
    {
        return (String)getPersistenceContext().getPropertyValue("message");
    }


    @Accessor(qualifier = "shortMessage", type = Accessor.Type.GETTER)
    public String getShortMessage()
    {
        return (String)getPersistenceContext().getPropertyValue("shortMessage");
    }


    @Accessor(qualifier = "step", type = Accessor.Type.GETTER)
    public StepModel getStep()
    {
        return (StepModel)getPersistenceContext().getPropertyValue("step");
    }


    @Accessor(qualifier = "cronJob", type = Accessor.Type.SETTER)
    public void setCronJob(CronJobModel value)
    {
        getPersistenceContext().setPropertyValue("cronJob", value);
    }


    @Accessor(qualifier = "level", type = Accessor.Type.SETTER)
    public void setLevel(JobLogLevel value)
    {
        getPersistenceContext().setPropertyValue("level", value);
    }


    @Accessor(qualifier = "message", type = Accessor.Type.SETTER)
    public void setMessage(String value)
    {
        getPersistenceContext().setPropertyValue("message", value);
    }


    @Accessor(qualifier = "step", type = Accessor.Type.SETTER)
    public void setStep(StepModel value)
    {
        getPersistenceContext().setPropertyValue("step", value);
    }
}
