package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.enums.ErrorMode;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.processengine.enums.BooleanOperator;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class CronJobModel extends ItemModel
{
    public static final String _TYPECODE = "CronJob";
    public static final String _JOBCRONJOBRELATION = "JobCronJobRelation";
    public static final String CODE = "code";
    public static final String ERRORMODE = "errorMode";
    public static final String LOGTOFILE = "logToFile";
    public static final String LOGTODATABASE = "logToDatabase";
    public static final String LOGLEVELFILE = "logLevelFile";
    public static final String LOGLEVELDATABASE = "logLevelDatabase";
    public static final String LOGFILES = "logFiles";
    public static final String SESSIONUSER = "sessionUser";
    public static final String SESSIONLANGUAGE = "sessionLanguage";
    public static final String SESSIONCURRENCY = "sessionCurrency";
    public static final String SESSIONCONTEXTVALUES = "sessionContextValues";
    public static final String ACTIVE = "active";
    public static final String RETRY = "retry";
    public static final String SINGLEEXECUTABLE = "singleExecutable";
    public static final String EMAILADDRESS = "emailAddress";
    public static final String SENDEMAIL = "sendEmail";
    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";
    public static final String STATUS = "status";
    public static final String RESULT = "result";
    public static final String LOGTEXT = "logText";
    public static final String NODEID = "nodeID";
    public static final String NODEGROUP = "nodeGroup";
    public static final String RUNNINGONCLUSTERNODE = "runningOnClusterNode";
    public static final String CURRENTSTEP = "currentStep";
    public static final String CHANGERECORDINGENABLED = "changeRecordingEnabled";
    public static final String CHANGES = "changes";
    public static final String REQUESTABORT = "requestAbort";
    public static final String REQUESTABORTSTEP = "requestAbortStep";
    public static final String TIMETABLE = "timeTable";
    public static final String PRIORITY = "priority";
    public static final String REMOVEONEXIT = "removeOnExit";
    public static final String EMAILNOTIFICATIONTEMPLATE = "emailNotificationTemplate";
    public static final String ALTERNATIVEDATASOURCEID = "alternativeDataSourceID";
    public static final String LOGSDAYSOLD = "logsDaysOld";
    public static final String LOGSCOUNT = "logsCount";
    public static final String LOGSOPERATOR = "logsOperator";
    public static final String FILESDAYSOLD = "filesDaysOld";
    public static final String FILESCOUNT = "filesCount";
    public static final String FILESOPERATOR = "filesOperator";
    public static final String QUERYCOUNT = "queryCount";
    public static final String ACTIVECRONJOBHISTORY = "activeCronJobHistory";
    public static final String USEREADONLYDATASOURCE = "useReadOnlyDatasource";
    public static final String NUMBEROFRETRIES = "numberOfRetries";
    public static final String CURRENTRETRY = "currentRetry";
    public static final String PROCESSEDSTEPS = "processedSteps";
    public static final String PENDINGSTEPS = "pendingSteps";
    public static final String LOGS = "logs";
    public static final String TRIGGERS = "triggers";
    public static final String JOB = "job";
    public static final String CRONJOBHISTORYENTRIES = "cronJobHistoryEntries";


    public CronJobModel()
    {
    }


    public CronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
    }


    @Accessor(qualifier = "activeCronJobHistory", type = Accessor.Type.GETTER)
    public CronJobHistoryModel getActiveCronJobHistory()
    {
        return (CronJobHistoryModel)getPersistenceContext().getPropertyValue("activeCronJobHistory");
    }


    @Accessor(qualifier = "alternativeDataSourceID", type = Accessor.Type.GETTER)
    public String getAlternativeDataSourceID()
    {
        return (String)getPersistenceContext().getPropertyValue("alternativeDataSourceID");
    }


    @Accessor(qualifier = "changeRecordingEnabled", type = Accessor.Type.GETTER)
    public Boolean getChangeRecordingEnabled()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("changeRecordingEnabled");
    }


    @Accessor(qualifier = "changes", type = Accessor.Type.GETTER)
    public Collection<ChangeDescriptorModel> getChanges()
    {
        return (Collection<ChangeDescriptorModel>)getPersistenceContext().getPropertyValue("changes");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "cronJobHistoryEntries", type = Accessor.Type.GETTER)
    public List<CronJobHistoryModel> getCronJobHistoryEntries()
    {
        return (List<CronJobHistoryModel>)getPersistenceContext().getPropertyValue("cronJobHistoryEntries");
    }


    @Accessor(qualifier = "currentRetry", type = Accessor.Type.GETTER)
    public int getCurrentRetry()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("currentRetry"));
    }


    @Accessor(qualifier = "currentStep", type = Accessor.Type.GETTER)
    public StepModel getCurrentStep()
    {
        return (StepModel)getPersistenceContext().getPropertyValue("currentStep");
    }


    @Accessor(qualifier = "emailAddress", type = Accessor.Type.GETTER)
    public String getEmailAddress()
    {
        return (String)getPersistenceContext().getPropertyValue("emailAddress");
    }


    @Accessor(qualifier = "emailNotificationTemplate", type = Accessor.Type.GETTER)
    public RendererTemplateModel getEmailNotificationTemplate()
    {
        return (RendererTemplateModel)getPersistenceContext().getPropertyValue("emailNotificationTemplate");
    }


    @Accessor(qualifier = "endTime", type = Accessor.Type.GETTER)
    public Date getEndTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("endTime");
    }


    @Accessor(qualifier = "errorMode", type = Accessor.Type.GETTER)
    public ErrorMode getErrorMode()
    {
        return (ErrorMode)getPersistenceContext().getPropertyValue("errorMode");
    }


    @Accessor(qualifier = "filesCount", type = Accessor.Type.GETTER)
    public int getFilesCount()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("filesCount"));
    }


    @Accessor(qualifier = "filesDaysOld", type = Accessor.Type.GETTER)
    public int getFilesDaysOld()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("filesDaysOld"));
    }


    @Accessor(qualifier = "filesOperator", type = Accessor.Type.GETTER)
    public BooleanOperator getFilesOperator()
    {
        return (BooleanOperator)getPersistenceContext().getPropertyValue("filesOperator");
    }


    @Accessor(qualifier = "job", type = Accessor.Type.GETTER)
    public JobModel getJob()
    {
        return (JobModel)getPersistenceContext().getPropertyValue("job");
    }


    @Accessor(qualifier = "logFiles", type = Accessor.Type.GETTER)
    public Collection<LogFileModel> getLogFiles()
    {
        return (Collection<LogFileModel>)getPersistenceContext().getPropertyValue("logFiles");
    }


    @Accessor(qualifier = "logLevelDatabase", type = Accessor.Type.GETTER)
    public JobLogLevel getLogLevelDatabase()
    {
        return (JobLogLevel)getPersistenceContext().getPropertyValue("logLevelDatabase");
    }


    @Accessor(qualifier = "logLevelFile", type = Accessor.Type.GETTER)
    public JobLogLevel getLogLevelFile()
    {
        return (JobLogLevel)getPersistenceContext().getPropertyValue("logLevelFile");
    }


    @Accessor(qualifier = "logs", type = Accessor.Type.GETTER)
    public List<JobLogModel> getLogs()
    {
        return (List<JobLogModel>)getPersistenceContext().getPropertyValue("logs");
    }


    @Accessor(qualifier = "logsCount", type = Accessor.Type.GETTER)
    public int getLogsCount()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("logsCount"));
    }


    @Accessor(qualifier = "logsDaysOld", type = Accessor.Type.GETTER)
    public int getLogsDaysOld()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("logsDaysOld"));
    }


    @Accessor(qualifier = "logsOperator", type = Accessor.Type.GETTER)
    public BooleanOperator getLogsOperator()
    {
        return (BooleanOperator)getPersistenceContext().getPropertyValue("logsOperator");
    }


    @Accessor(qualifier = "logText", type = Accessor.Type.GETTER)
    public String getLogText()
    {
        return (String)getPersistenceContext().getPropertyValue("logText");
    }


    @Accessor(qualifier = "logToDatabase", type = Accessor.Type.GETTER)
    public Boolean getLogToDatabase()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("logToDatabase");
    }


    @Accessor(qualifier = "logToFile", type = Accessor.Type.GETTER)
    public Boolean getLogToFile()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("logToFile");
    }


    @Accessor(qualifier = "nodeGroup", type = Accessor.Type.GETTER)
    public String getNodeGroup()
    {
        return (String)getPersistenceContext().getPropertyValue("nodeGroup");
    }


    @Accessor(qualifier = "nodeID", type = Accessor.Type.GETTER)
    public Integer getNodeID()
    {
        return (Integer)getPersistenceContext().getPropertyValue("nodeID");
    }


    @Accessor(qualifier = "numberOfRetries", type = Accessor.Type.GETTER)
    public int getNumberOfRetries()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("numberOfRetries"));
    }


    @Accessor(qualifier = "pendingSteps", type = Accessor.Type.GETTER)
    public List<StepModel> getPendingSteps()
    {
        return (List<StepModel>)getPersistenceContext().getPropertyValue("pendingSteps");
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
    public Integer getPriority()
    {
        return (Integer)getPersistenceContext().getPropertyValue("priority");
    }


    @Accessor(qualifier = "processedSteps", type = Accessor.Type.GETTER)
    public List<StepModel> getProcessedSteps()
    {
        return (List<StepModel>)getPersistenceContext().getPropertyValue("processedSteps");
    }


    @Accessor(qualifier = "queryCount", type = Accessor.Type.GETTER)
    public int getQueryCount()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("queryCount"));
    }


    @Accessor(qualifier = "removeOnExit", type = Accessor.Type.GETTER)
    public Boolean getRemoveOnExit()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("removeOnExit");
    }


    @Accessor(qualifier = "requestAbort", type = Accessor.Type.GETTER)
    public Boolean getRequestAbort()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("requestAbort");
    }


    @Accessor(qualifier = "requestAbortStep", type = Accessor.Type.GETTER)
    public Boolean getRequestAbortStep()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("requestAbortStep");
    }


    @Accessor(qualifier = "result", type = Accessor.Type.GETTER)
    public CronJobResult getResult()
    {
        return (CronJobResult)getPersistenceContext().getPropertyValue("result");
    }


    @Accessor(qualifier = "retry", type = Accessor.Type.GETTER)
    public Boolean getRetry()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("retry");
    }


    @Accessor(qualifier = "runningOnClusterNode", type = Accessor.Type.GETTER)
    public Integer getRunningOnClusterNode()
    {
        return (Integer)getPersistenceContext().getPropertyValue("runningOnClusterNode");
    }


    @Accessor(qualifier = "sendEmail", type = Accessor.Type.GETTER)
    public Boolean getSendEmail()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("sendEmail");
    }


    @Accessor(qualifier = "sessionCurrency", type = Accessor.Type.GETTER)
    public CurrencyModel getSessionCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("sessionCurrency");
    }


    @Accessor(qualifier = "sessionLanguage", type = Accessor.Type.GETTER)
    public LanguageModel getSessionLanguage()
    {
        return (LanguageModel)getPersistenceContext().getPropertyValue("sessionLanguage");
    }


    @Accessor(qualifier = "sessionUser", type = Accessor.Type.GETTER)
    public UserModel getSessionUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("sessionUser");
    }


    @Accessor(qualifier = "singleExecutable", type = Accessor.Type.GETTER)
    public Boolean getSingleExecutable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("singleExecutable");
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


    @Accessor(qualifier = "timeTable", type = Accessor.Type.GETTER)
    public String getTimeTable()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "timeTable");
    }


    @Accessor(qualifier = "triggers", type = Accessor.Type.GETTER)
    public List<TriggerModel> getTriggers()
    {
        return (List<TriggerModel>)getPersistenceContext().getPropertyValue("triggers");
    }


    @Accessor(qualifier = "useReadOnlyDatasource", type = Accessor.Type.GETTER)
    public Boolean getUseReadOnlyDatasource()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("useReadOnlyDatasource");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
    }


    @Accessor(qualifier = "activeCronJobHistory", type = Accessor.Type.SETTER)
    public void setActiveCronJobHistory(CronJobHistoryModel value)
    {
        getPersistenceContext().setPropertyValue("activeCronJobHistory", value);
    }


    @Accessor(qualifier = "alternativeDataSourceID", type = Accessor.Type.SETTER)
    public void setAlternativeDataSourceID(String value)
    {
        getPersistenceContext().setPropertyValue("alternativeDataSourceID", value);
    }


    @Accessor(qualifier = "changeRecordingEnabled", type = Accessor.Type.SETTER)
    public void setChangeRecordingEnabled(Boolean value)
    {
        getPersistenceContext().setPropertyValue("changeRecordingEnabled", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "cronJobHistoryEntries", type = Accessor.Type.SETTER)
    public void setCronJobHistoryEntries(List<CronJobHistoryModel> value)
    {
        getPersistenceContext().setPropertyValue("cronJobHistoryEntries", value);
    }


    @Accessor(qualifier = "currentRetry", type = Accessor.Type.SETTER)
    public void setCurrentRetry(int value)
    {
        getPersistenceContext().setPropertyValue("currentRetry", toObject(value));
    }


    @Accessor(qualifier = "emailAddress", type = Accessor.Type.SETTER)
    public void setEmailAddress(String value)
    {
        getPersistenceContext().setPropertyValue("emailAddress", value);
    }


    @Accessor(qualifier = "emailNotificationTemplate", type = Accessor.Type.SETTER)
    public void setEmailNotificationTemplate(RendererTemplateModel value)
    {
        getPersistenceContext().setPropertyValue("emailNotificationTemplate", value);
    }


    @Accessor(qualifier = "endTime", type = Accessor.Type.SETTER)
    public void setEndTime(Date value)
    {
        getPersistenceContext().setPropertyValue("endTime", value);
    }


    @Accessor(qualifier = "errorMode", type = Accessor.Type.SETTER)
    public void setErrorMode(ErrorMode value)
    {
        getPersistenceContext().setPropertyValue("errorMode", value);
    }


    @Accessor(qualifier = "filesCount", type = Accessor.Type.SETTER)
    public void setFilesCount(int value)
    {
        getPersistenceContext().setPropertyValue("filesCount", toObject(value));
    }


    @Accessor(qualifier = "filesDaysOld", type = Accessor.Type.SETTER)
    public void setFilesDaysOld(int value)
    {
        getPersistenceContext().setPropertyValue("filesDaysOld", toObject(value));
    }


    @Accessor(qualifier = "filesOperator", type = Accessor.Type.SETTER)
    public void setFilesOperator(BooleanOperator value)
    {
        getPersistenceContext().setPropertyValue("filesOperator", value);
    }


    @Accessor(qualifier = "job", type = Accessor.Type.SETTER)
    public void setJob(JobModel value)
    {
        getPersistenceContext().setPropertyValue("job", value);
    }


    @Accessor(qualifier = "logFiles", type = Accessor.Type.SETTER)
    public void setLogFiles(Collection<LogFileModel> value)
    {
        getPersistenceContext().setPropertyValue("logFiles", value);
    }


    @Accessor(qualifier = "logLevelDatabase", type = Accessor.Type.SETTER)
    public void setLogLevelDatabase(JobLogLevel value)
    {
        getPersistenceContext().setPropertyValue("logLevelDatabase", value);
    }


    @Accessor(qualifier = "logLevelFile", type = Accessor.Type.SETTER)
    public void setLogLevelFile(JobLogLevel value)
    {
        getPersistenceContext().setPropertyValue("logLevelFile", value);
    }


    @Accessor(qualifier = "logsCount", type = Accessor.Type.SETTER)
    public void setLogsCount(int value)
    {
        getPersistenceContext().setPropertyValue("logsCount", toObject(value));
    }


    @Accessor(qualifier = "logsDaysOld", type = Accessor.Type.SETTER)
    public void setLogsDaysOld(int value)
    {
        getPersistenceContext().setPropertyValue("logsDaysOld", toObject(value));
    }


    @Accessor(qualifier = "logsOperator", type = Accessor.Type.SETTER)
    public void setLogsOperator(BooleanOperator value)
    {
        getPersistenceContext().setPropertyValue("logsOperator", value);
    }


    @Accessor(qualifier = "logToDatabase", type = Accessor.Type.SETTER)
    public void setLogToDatabase(Boolean value)
    {
        getPersistenceContext().setPropertyValue("logToDatabase", value);
    }


    @Accessor(qualifier = "logToFile", type = Accessor.Type.SETTER)
    public void setLogToFile(Boolean value)
    {
        getPersistenceContext().setPropertyValue("logToFile", value);
    }


    @Accessor(qualifier = "nodeGroup", type = Accessor.Type.SETTER)
    public void setNodeGroup(String value)
    {
        getPersistenceContext().setPropertyValue("nodeGroup", value);
    }


    @Accessor(qualifier = "nodeID", type = Accessor.Type.SETTER)
    public void setNodeID(Integer value)
    {
        getPersistenceContext().setPropertyValue("nodeID", value);
    }


    @Accessor(qualifier = "numberOfRetries", type = Accessor.Type.SETTER)
    public void setNumberOfRetries(int value)
    {
        getPersistenceContext().setPropertyValue("numberOfRetries", toObject(value));
    }


    @Accessor(qualifier = "pendingSteps", type = Accessor.Type.SETTER)
    public void setPendingSteps(List<StepModel> value)
    {
        getPersistenceContext().setPropertyValue("pendingSteps", value);
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
    public void setPriority(Integer value)
    {
        getPersistenceContext().setPropertyValue("priority", value);
    }


    @Accessor(qualifier = "processedSteps", type = Accessor.Type.SETTER)
    public void setProcessedSteps(List<StepModel> value)
    {
        getPersistenceContext().setPropertyValue("processedSteps", value);
    }


    @Accessor(qualifier = "queryCount", type = Accessor.Type.SETTER)
    public void setQueryCount(int value)
    {
        getPersistenceContext().setPropertyValue("queryCount", toObject(value));
    }


    @Accessor(qualifier = "removeOnExit", type = Accessor.Type.SETTER)
    public void setRemoveOnExit(Boolean value)
    {
        getPersistenceContext().setPropertyValue("removeOnExit", value);
    }


    @Accessor(qualifier = "requestAbort", type = Accessor.Type.SETTER)
    public void setRequestAbort(Boolean value)
    {
        getPersistenceContext().setPropertyValue("requestAbort", value);
    }


    @Accessor(qualifier = "requestAbortStep", type = Accessor.Type.SETTER)
    public void setRequestAbortStep(Boolean value)
    {
        getPersistenceContext().setPropertyValue("requestAbortStep", value);
    }


    @Accessor(qualifier = "result", type = Accessor.Type.SETTER)
    public void setResult(CronJobResult value)
    {
        getPersistenceContext().setPropertyValue("result", value);
    }


    @Accessor(qualifier = "retry", type = Accessor.Type.SETTER)
    public void setRetry(Boolean value)
    {
        getPersistenceContext().setPropertyValue("retry", value);
    }


    @Accessor(qualifier = "runningOnClusterNode", type = Accessor.Type.SETTER)
    public void setRunningOnClusterNode(Integer value)
    {
        getPersistenceContext().setPropertyValue("runningOnClusterNode", value);
    }


    @Accessor(qualifier = "sendEmail", type = Accessor.Type.SETTER)
    public void setSendEmail(Boolean value)
    {
        getPersistenceContext().setPropertyValue("sendEmail", value);
    }


    @Accessor(qualifier = "sessionCurrency", type = Accessor.Type.SETTER)
    public void setSessionCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("sessionCurrency", value);
    }


    @Accessor(qualifier = "sessionLanguage", type = Accessor.Type.SETTER)
    public void setSessionLanguage(LanguageModel value)
    {
        getPersistenceContext().setPropertyValue("sessionLanguage", value);
    }


    @Accessor(qualifier = "sessionUser", type = Accessor.Type.SETTER)
    public void setSessionUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("sessionUser", value);
    }


    @Accessor(qualifier = "singleExecutable", type = Accessor.Type.SETTER)
    public void setSingleExecutable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("singleExecutable", value);
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


    @Accessor(qualifier = "triggers", type = Accessor.Type.SETTER)
    public void setTriggers(List<TriggerModel> value)
    {
        getPersistenceContext().setPropertyValue("triggers", value);
    }


    @Accessor(qualifier = "useReadOnlyDatasource", type = Accessor.Type.SETTER)
    public void setUseReadOnlyDatasource(Boolean value)
    {
        getPersistenceContext().setPropertyValue("useReadOnlyDatasource", value);
    }
}
