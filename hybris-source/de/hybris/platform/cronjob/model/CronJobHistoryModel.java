package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class CronJobHistoryModel extends ItemModel
{
    public static final String _TYPECODE = "CronJobHistory";
    public static final String _CRONJOBHISTORYRELATION = "CronJobHistoryRelation";
    public static final String CRONJOBCODE = "cronJobCode";
    public static final String JOBCODE = "jobCode";
    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";
    public static final String NODEID = "nodeID";
    public static final String SCHEDULED = "scheduled";
    public static final String USERUID = "userUid";
    public static final String STATUS = "status";
    public static final String RESULT = "result";
    public static final String FAILUREMESSAGE = "failureMessage";
    public static final String PROGRESS = "progress";
    public static final String STATUSLINE = "statusLine";
    public static final String CRONJOB = "cronJob";


    public CronJobHistoryModel()
    {
    }


    public CronJobHistoryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CronJobHistoryModel(String _cronJobCode, String _jobCode, int _nodeID, Date _startTime)
    {
        setCronJobCode(_cronJobCode);
        setJobCode(_jobCode);
        setNodeID(_nodeID);
        setStartTime(_startTime);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CronJobHistoryModel(String _cronJobCode, String _jobCode, int _nodeID, ItemModel _owner, Date _startTime)
    {
        setCronJobCode(_cronJobCode);
        setJobCode(_jobCode);
        setNodeID(_nodeID);
        setOwner(_owner);
        setStartTime(_startTime);
    }


    @Accessor(qualifier = "cronJob", type = Accessor.Type.GETTER)
    public CronJobModel getCronJob()
    {
        return (CronJobModel)getPersistenceContext().getPropertyValue("cronJob");
    }


    @Accessor(qualifier = "cronJobCode", type = Accessor.Type.GETTER)
    public String getCronJobCode()
    {
        return (String)getPersistenceContext().getPropertyValue("cronJobCode");
    }


    @Accessor(qualifier = "endTime", type = Accessor.Type.GETTER)
    public Date getEndTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("endTime");
    }


    @Accessor(qualifier = "failureMessage", type = Accessor.Type.GETTER)
    public String getFailureMessage()
    {
        return (String)getPersistenceContext().getPropertyValue("failureMessage");
    }


    @Accessor(qualifier = "jobCode", type = Accessor.Type.GETTER)
    public String getJobCode()
    {
        return (String)getPersistenceContext().getPropertyValue("jobCode");
    }


    @Accessor(qualifier = "nodeID", type = Accessor.Type.GETTER)
    public int getNodeID()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("nodeID"));
    }


    @Accessor(qualifier = "progress", type = Accessor.Type.GETTER)
    public Double getProgress()
    {
        return (Double)getPersistenceContext().getPropertyValue("progress");
    }


    @Accessor(qualifier = "result", type = Accessor.Type.GETTER)
    public CronJobResult getResult()
    {
        return (CronJobResult)getPersistenceContext().getPropertyValue("result");
    }


    @Accessor(qualifier = "scheduled", type = Accessor.Type.GETTER)
    public Boolean getScheduled()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("scheduled");
    }


    @Accessor(qualifier = "startTime", type = Accessor.Type.GETTER)
    public Date getStartTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("startTime");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public CronJobStatus getStatus()
    {
        return (CronJobStatus)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "statusLine", type = Accessor.Type.GETTER)
    public String getStatusLine()
    {
        return (String)getPersistenceContext().getPropertyValue("statusLine");
    }


    @Accessor(qualifier = "userUid", type = Accessor.Type.GETTER)
    public String getUserUid()
    {
        return (String)getPersistenceContext().getPropertyValue("userUid");
    }


    @Accessor(qualifier = "cronJob", type = Accessor.Type.SETTER)
    public void setCronJob(CronJobModel value)
    {
        getPersistenceContext().setPropertyValue("cronJob", value);
    }


    @Accessor(qualifier = "cronJobCode", type = Accessor.Type.SETTER)
    public void setCronJobCode(String value)
    {
        getPersistenceContext().setPropertyValue("cronJobCode", value);
    }


    @Accessor(qualifier = "endTime", type = Accessor.Type.SETTER)
    public void setEndTime(Date value)
    {
        getPersistenceContext().setPropertyValue("endTime", value);
    }


    @Accessor(qualifier = "failureMessage", type = Accessor.Type.SETTER)
    public void setFailureMessage(String value)
    {
        getPersistenceContext().setPropertyValue("failureMessage", value);
    }


    @Accessor(qualifier = "jobCode", type = Accessor.Type.SETTER)
    public void setJobCode(String value)
    {
        getPersistenceContext().setPropertyValue("jobCode", value);
    }


    @Accessor(qualifier = "nodeID", type = Accessor.Type.SETTER)
    public void setNodeID(int value)
    {
        getPersistenceContext().setPropertyValue("nodeID", toObject(value));
    }


    @Accessor(qualifier = "progress", type = Accessor.Type.SETTER)
    public void setProgress(Double value)
    {
        getPersistenceContext().setPropertyValue("progress", value);
    }


    @Accessor(qualifier = "result", type = Accessor.Type.SETTER)
    public void setResult(CronJobResult value)
    {
        getPersistenceContext().setPropertyValue("result", value);
    }


    @Accessor(qualifier = "scheduled", type = Accessor.Type.SETTER)
    public void setScheduled(Boolean value)
    {
        getPersistenceContext().setPropertyValue("scheduled", value);
    }


    @Accessor(qualifier = "startTime", type = Accessor.Type.SETTER)
    public void setStartTime(Date value)
    {
        getPersistenceContext().setPropertyValue("startTime", value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(CronJobStatus value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }


    @Accessor(qualifier = "statusLine", type = Accessor.Type.SETTER)
    public void setStatusLine(String value)
    {
        getPersistenceContext().setPropertyValue("statusLine", value);
    }


    @Accessor(qualifier = "userUid", type = Accessor.Type.SETTER)
    public void setUserUid(String value)
    {
        getPersistenceContext().setPropertyValue("userUid", value);
    }
}
