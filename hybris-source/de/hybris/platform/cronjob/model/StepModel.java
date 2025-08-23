package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.ErrorMode;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class StepModel extends ItemModel
{
    public static final String _TYPECODE = "Step";
    public static final String _CRONJOBPROCESSEDSTEPSRELATION = "CronJobProcessedStepsRelation";
    public static final String _CRONJOBPENDINGSTEPSRELATION = "CronJobPendingStepsRelation";
    public static final String BATCHJOB = "batchJob";
    public static final String CODE = "code";
    public static final String SEQUENCENUMBER = "sequenceNumber";
    public static final String SYNCHRONOUS = "synchronous";
    public static final String ERRORMODE = "errorMode";
    public static final String PROCESSEDCRONJOBS = "processedCronJobs";
    public static final String PENDINGCRONJOBS = "pendingCronJobs";


    public StepModel()
    {
    }


    public StepModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StepModel(BatchJobModel _batchJob, String _code, Integer _sequenceNumber)
    {
        setBatchJob(_batchJob);
        setCode(_code);
        setSequenceNumber(_sequenceNumber);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StepModel(BatchJobModel _batchJob, String _code, ItemModel _owner, Integer _sequenceNumber)
    {
        setBatchJob(_batchJob);
        setCode(_code);
        setOwner(_owner);
        setSequenceNumber(_sequenceNumber);
    }


    @Accessor(qualifier = "batchJob", type = Accessor.Type.GETTER)
    public BatchJobModel getBatchJob()
    {
        return (BatchJobModel)getPersistenceContext().getPropertyValue("batchJob");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "errorMode", type = Accessor.Type.GETTER)
    public ErrorMode getErrorMode()
    {
        return (ErrorMode)getPersistenceContext().getPropertyValue("errorMode");
    }


    @Accessor(qualifier = "sequenceNumber", type = Accessor.Type.GETTER)
    public Integer getSequenceNumber()
    {
        return (Integer)getPersistenceContext().getPropertyValue("sequenceNumber");
    }


    @Accessor(qualifier = "synchronous", type = Accessor.Type.GETTER)
    public Boolean getSynchronous()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("synchronous");
    }


    @Accessor(qualifier = "batchJob", type = Accessor.Type.SETTER)
    public void setBatchJob(BatchJobModel value)
    {
        getPersistenceContext().setPropertyValue("batchJob", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "errorMode", type = Accessor.Type.SETTER)
    public void setErrorMode(ErrorMode value)
    {
        getPersistenceContext().setPropertyValue("errorMode", value);
    }


    @Accessor(qualifier = "sequenceNumber", type = Accessor.Type.SETTER)
    public void setSequenceNumber(Integer value)
    {
        getPersistenceContext().setPropertyValue("sequenceNumber", value);
    }


    @Accessor(qualifier = "synchronous", type = Accessor.Type.SETTER)
    public void setSynchronous(Boolean value)
    {
        getPersistenceContext().setPropertyValue("synchronous", value);
    }
}
