package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.commons.jalo.renderer.RendererTemplate;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.processing.constants.GeneratedProcessingConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCronJob extends GenericItem
{
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
    protected static String CRONJOBPROCESSEDSTEPSRELATION_SRC_ORDERED = "relation.CronJobProcessedStepsRelation.source.ordered";
    protected static String CRONJOBPROCESSEDSTEPSRELATION_TGT_ORDERED = "relation.CronJobProcessedStepsRelation.target.ordered";
    protected static String CRONJOBPROCESSEDSTEPSRELATION_MARKMODIFIED = "relation.CronJobProcessedStepsRelation.markmodified";
    public static final String PENDINGSTEPS = "pendingSteps";
    protected static String CRONJOBPENDINGSTEPSRELATION_SRC_ORDERED = "relation.CronJobPendingStepsRelation.source.ordered";
    protected static String CRONJOBPENDINGSTEPSRELATION_TGT_ORDERED = "relation.CronJobPendingStepsRelation.target.ordered";
    protected static String CRONJOBPENDINGSTEPSRELATION_MARKMODIFIED = "relation.CronJobPendingStepsRelation.markmodified";
    public static final String LOGS = "logs";
    public static final String TRIGGERS = "triggers";
    public static final String JOB = "job";
    public static final String CRONJOBHISTORYENTRIES = "cronJobHistoryEntries";
    protected static final OneToManyHandler<JobLog> LOGSHANDLER = new OneToManyHandler(GeneratedProcessingConstants.TC.JOBLOG, true, "cronJob", null, false, true, 2);
    protected static final OneToManyHandler<Trigger> TRIGGERSHANDLER = new OneToManyHandler(GeneratedProcessingConstants.TC.TRIGGER, true, "cronJob", null, false, true, 2);
    protected static final BidirectionalOneToManyHandler<GeneratedCronJob> JOBHANDLER = new BidirectionalOneToManyHandler(GeneratedProcessingConstants.TC.CRONJOB, false, "job", null, false, true, 0);
    protected static final OneToManyHandler<CronJobHistory> CRONJOBHISTORYENTRIESHANDLER = new OneToManyHandler(GeneratedProcessingConstants.TC.CRONJOBHISTORY, true, "cronJob", "creationtime", false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("errorMode", Item.AttributeMode.INITIAL);
        tmp.put("logToFile", Item.AttributeMode.INITIAL);
        tmp.put("logToDatabase", Item.AttributeMode.INITIAL);
        tmp.put("logLevelFile", Item.AttributeMode.INITIAL);
        tmp.put("logLevelDatabase", Item.AttributeMode.INITIAL);
        tmp.put("sessionUser", Item.AttributeMode.INITIAL);
        tmp.put("sessionLanguage", Item.AttributeMode.INITIAL);
        tmp.put("sessionCurrency", Item.AttributeMode.INITIAL);
        tmp.put("sessionContextValues", Item.AttributeMode.INITIAL);
        tmp.put("active", Item.AttributeMode.INITIAL);
        tmp.put("retry", Item.AttributeMode.INITIAL);
        tmp.put("singleExecutable", Item.AttributeMode.INITIAL);
        tmp.put("emailAddress", Item.AttributeMode.INITIAL);
        tmp.put("sendEmail", Item.AttributeMode.INITIAL);
        tmp.put("startTime", Item.AttributeMode.INITIAL);
        tmp.put("endTime", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("result", Item.AttributeMode.INITIAL);
        tmp.put("nodeID", Item.AttributeMode.INITIAL);
        tmp.put("nodeGroup", Item.AttributeMode.INITIAL);
        tmp.put("runningOnClusterNode", Item.AttributeMode.INITIAL);
        tmp.put("currentStep", Item.AttributeMode.INITIAL);
        tmp.put("changeRecordingEnabled", Item.AttributeMode.INITIAL);
        tmp.put("requestAbort", Item.AttributeMode.INITIAL);
        tmp.put("requestAbortStep", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("removeOnExit", Item.AttributeMode.INITIAL);
        tmp.put("emailNotificationTemplate", Item.AttributeMode.INITIAL);
        tmp.put("alternativeDataSourceID", Item.AttributeMode.INITIAL);
        tmp.put("logsDaysOld", Item.AttributeMode.INITIAL);
        tmp.put("logsCount", Item.AttributeMode.INITIAL);
        tmp.put("logsOperator", Item.AttributeMode.INITIAL);
        tmp.put("filesDaysOld", Item.AttributeMode.INITIAL);
        tmp.put("filesCount", Item.AttributeMode.INITIAL);
        tmp.put("filesOperator", Item.AttributeMode.INITIAL);
        tmp.put("queryCount", Item.AttributeMode.INITIAL);
        tmp.put("activeCronJobHistory", Item.AttributeMode.INITIAL);
        tmp.put("useReadOnlyDatasource", Item.AttributeMode.INITIAL);
        tmp.put("numberOfRetries", Item.AttributeMode.INITIAL);
        tmp.put("currentRetry", Item.AttributeMode.INITIAL);
        tmp.put("job", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isActive(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "active");
    }


    public Boolean isActive()
    {
        return isActive(getSession().getSessionContext());
    }


    public boolean isActiveAsPrimitive(SessionContext ctx)
    {
        Boolean value = isActive(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isActiveAsPrimitive()
    {
        return isActiveAsPrimitive(getSession().getSessionContext());
    }


    public void setActive(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "active", value);
    }


    public void setActive(Boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public void setActive(SessionContext ctx, boolean value)
    {
        setActive(ctx, Boolean.valueOf(value));
    }


    public void setActive(boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public CronJobHistory getActiveCronJobHistory(SessionContext ctx)
    {
        return (CronJobHistory)getProperty(ctx, "activeCronJobHistory");
    }


    public CronJobHistory getActiveCronJobHistory()
    {
        return getActiveCronJobHistory(getSession().getSessionContext());
    }


    public void setActiveCronJobHistory(SessionContext ctx, CronJobHistory value)
    {
        setProperty(ctx, "activeCronJobHistory", value);
    }


    public void setActiveCronJobHistory(CronJobHistory value)
    {
        setActiveCronJobHistory(getSession().getSessionContext(), value);
    }


    public String getAlternativeDataSourceID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "alternativeDataSourceID");
    }


    public String getAlternativeDataSourceID()
    {
        return getAlternativeDataSourceID(getSession().getSessionContext());
    }


    public void setAlternativeDataSourceID(SessionContext ctx, String value)
    {
        setProperty(ctx, "alternativeDataSourceID", value);
    }


    public void setAlternativeDataSourceID(String value)
    {
        setAlternativeDataSourceID(getSession().getSessionContext(), value);
    }


    public Boolean isChangeRecordingEnabled(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "changeRecordingEnabled");
    }


    public Boolean isChangeRecordingEnabled()
    {
        return isChangeRecordingEnabled(getSession().getSessionContext());
    }


    public boolean isChangeRecordingEnabledAsPrimitive(SessionContext ctx)
    {
        Boolean value = isChangeRecordingEnabled(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isChangeRecordingEnabledAsPrimitive()
    {
        return isChangeRecordingEnabledAsPrimitive(getSession().getSessionContext());
    }


    public void setChangeRecordingEnabled(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "changeRecordingEnabled", value);
    }


    public void setChangeRecordingEnabled(Boolean value)
    {
        setChangeRecordingEnabled(getSession().getSessionContext(), value);
    }


    public void setChangeRecordingEnabled(SessionContext ctx, boolean value)
    {
        setChangeRecordingEnabled(ctx, Boolean.valueOf(value));
    }


    public void setChangeRecordingEnabled(boolean value)
    {
        setChangeRecordingEnabled(getSession().getSessionContext(), value);
    }


    public Collection<ChangeDescriptor> getChanges()
    {
        return getChanges(getSession().getSessionContext());
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        JOBHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public List<CronJobHistory> getCronJobHistoryEntries(SessionContext ctx)
    {
        return (List<CronJobHistory>)CRONJOBHISTORYENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public List<CronJobHistory> getCronJobHistoryEntries()
    {
        return getCronJobHistoryEntries(getSession().getSessionContext());
    }


    public void setCronJobHistoryEntries(SessionContext ctx, List<CronJobHistory> value)
    {
        CRONJOBHISTORYENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setCronJobHistoryEntries(List<CronJobHistory> value)
    {
        setCronJobHistoryEntries(getSession().getSessionContext(), value);
    }


    public void addToCronJobHistoryEntries(SessionContext ctx, CronJobHistory value)
    {
        CRONJOBHISTORYENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToCronJobHistoryEntries(CronJobHistory value)
    {
        addToCronJobHistoryEntries(getSession().getSessionContext(), value);
    }


    public void removeFromCronJobHistoryEntries(SessionContext ctx, CronJobHistory value)
    {
        CRONJOBHISTORYENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromCronJobHistoryEntries(CronJobHistory value)
    {
        removeFromCronJobHistoryEntries(getSession().getSessionContext(), value);
    }


    public Integer getCurrentRetry(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "currentRetry");
    }


    public Integer getCurrentRetry()
    {
        return getCurrentRetry(getSession().getSessionContext());
    }


    public int getCurrentRetryAsPrimitive(SessionContext ctx)
    {
        Integer value = getCurrentRetry(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getCurrentRetryAsPrimitive()
    {
        return getCurrentRetryAsPrimitive(getSession().getSessionContext());
    }


    public void setCurrentRetry(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "currentRetry", value);
    }


    public void setCurrentRetry(Integer value)
    {
        setCurrentRetry(getSession().getSessionContext(), value);
    }


    public void setCurrentRetry(SessionContext ctx, int value)
    {
        setCurrentRetry(ctx, Integer.valueOf(value));
    }


    public void setCurrentRetry(int value)
    {
        setCurrentRetry(getSession().getSessionContext(), value);
    }


    public Step getCurrentStep(SessionContext ctx)
    {
        return (Step)getProperty(ctx, "currentStep");
    }


    public Step getCurrentStep()
    {
        return getCurrentStep(getSession().getSessionContext());
    }


    public String getEmailAddress(SessionContext ctx)
    {
        return (String)getProperty(ctx, "emailAddress");
    }


    public String getEmailAddress()
    {
        return getEmailAddress(getSession().getSessionContext());
    }


    public void setEmailAddress(SessionContext ctx, String value)
    {
        setProperty(ctx, "emailAddress", value);
    }


    public void setEmailAddress(String value)
    {
        setEmailAddress(getSession().getSessionContext(), value);
    }


    public RendererTemplate getEmailNotificationTemplate(SessionContext ctx)
    {
        return (RendererTemplate)getProperty(ctx, "emailNotificationTemplate");
    }


    public RendererTemplate getEmailNotificationTemplate()
    {
        return getEmailNotificationTemplate(getSession().getSessionContext());
    }


    public void setEmailNotificationTemplate(SessionContext ctx, RendererTemplate value)
    {
        setProperty(ctx, "emailNotificationTemplate", value);
    }


    public void setEmailNotificationTemplate(RendererTemplate value)
    {
        setEmailNotificationTemplate(getSession().getSessionContext(), value);
    }


    public Date getEndTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "endTime");
    }


    public Date getEndTime()
    {
        return getEndTime(getSession().getSessionContext());
    }


    public void setEndTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "endTime", value);
    }


    public void setEndTime(Date value)
    {
        setEndTime(getSession().getSessionContext(), value);
    }


    public EnumerationValue getErrorMode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "errorMode");
    }


    public EnumerationValue getErrorMode()
    {
        return getErrorMode(getSession().getSessionContext());
    }


    public void setErrorMode(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "errorMode", value);
    }


    public void setErrorMode(EnumerationValue value)
    {
        setErrorMode(getSession().getSessionContext(), value);
    }


    public Integer getFilesCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "filesCount");
    }


    public Integer getFilesCount()
    {
        return getFilesCount(getSession().getSessionContext());
    }


    public int getFilesCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getFilesCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getFilesCountAsPrimitive()
    {
        return getFilesCountAsPrimitive(getSession().getSessionContext());
    }


    public void setFilesCount(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "filesCount", value);
    }


    public void setFilesCount(Integer value)
    {
        setFilesCount(getSession().getSessionContext(), value);
    }


    public void setFilesCount(SessionContext ctx, int value)
    {
        setFilesCount(ctx, Integer.valueOf(value));
    }


    public void setFilesCount(int value)
    {
        setFilesCount(getSession().getSessionContext(), value);
    }


    public Integer getFilesDaysOld(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "filesDaysOld");
    }


    public Integer getFilesDaysOld()
    {
        return getFilesDaysOld(getSession().getSessionContext());
    }


    public int getFilesDaysOldAsPrimitive(SessionContext ctx)
    {
        Integer value = getFilesDaysOld(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getFilesDaysOldAsPrimitive()
    {
        return getFilesDaysOldAsPrimitive(getSession().getSessionContext());
    }


    public void setFilesDaysOld(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "filesDaysOld", value);
    }


    public void setFilesDaysOld(Integer value)
    {
        setFilesDaysOld(getSession().getSessionContext(), value);
    }


    public void setFilesDaysOld(SessionContext ctx, int value)
    {
        setFilesDaysOld(ctx, Integer.valueOf(value));
    }


    public void setFilesDaysOld(int value)
    {
        setFilesDaysOld(getSession().getSessionContext(), value);
    }


    public EnumerationValue getFilesOperator(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "filesOperator");
    }


    public EnumerationValue getFilesOperator()
    {
        return getFilesOperator(getSession().getSessionContext());
    }


    public void setFilesOperator(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "filesOperator", value);
    }


    public void setFilesOperator(EnumerationValue value)
    {
        setFilesOperator(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Step");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CRONJOBPROCESSEDSTEPSRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("Step");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CRONJOBPENDINGSTEPSRELATION_MARKMODIFIED);
        }
        return true;
    }


    public Job getJob(SessionContext ctx)
    {
        return (Job)getProperty(ctx, "job");
    }


    public Job getJob()
    {
        return getJob(getSession().getSessionContext());
    }


    protected void setJob(SessionContext ctx, Job value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'job' is not changeable", 0);
        }
        JOBHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setJob(Job value)
    {
        setJob(getSession().getSessionContext(), value);
    }


    public Collection<LogFile> getLogFiles()
    {
        return getLogFiles(getSession().getSessionContext());
    }


    public void setLogFiles(Collection<LogFile> value)
    {
        setLogFiles(getSession().getSessionContext(), value);
    }


    public EnumerationValue getLogLevelDatabase(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "logLevelDatabase");
    }


    public EnumerationValue getLogLevelDatabase()
    {
        return getLogLevelDatabase(getSession().getSessionContext());
    }


    public void setLogLevelDatabase(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "logLevelDatabase", value);
    }


    public void setLogLevelDatabase(EnumerationValue value)
    {
        setLogLevelDatabase(getSession().getSessionContext(), value);
    }


    public EnumerationValue getLogLevelFile(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "logLevelFile");
    }


    public EnumerationValue getLogLevelFile()
    {
        return getLogLevelFile(getSession().getSessionContext());
    }


    public void setLogLevelFile(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "logLevelFile", value);
    }


    public void setLogLevelFile(EnumerationValue value)
    {
        setLogLevelFile(getSession().getSessionContext(), value);
    }


    public List<JobLog> getLogs(SessionContext ctx)
    {
        return (List<JobLog>)LOGSHANDLER.getValues(ctx, (Item)this);
    }


    public List<JobLog> getLogs()
    {
        return getLogs(getSession().getSessionContext());
    }


    public Integer getLogsCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "logsCount");
    }


    public Integer getLogsCount()
    {
        return getLogsCount(getSession().getSessionContext());
    }


    public int getLogsCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getLogsCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getLogsCountAsPrimitive()
    {
        return getLogsCountAsPrimitive(getSession().getSessionContext());
    }


    public void setLogsCount(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "logsCount", value);
    }


    public void setLogsCount(Integer value)
    {
        setLogsCount(getSession().getSessionContext(), value);
    }


    public void setLogsCount(SessionContext ctx, int value)
    {
        setLogsCount(ctx, Integer.valueOf(value));
    }


    public void setLogsCount(int value)
    {
        setLogsCount(getSession().getSessionContext(), value);
    }


    public Integer getLogsDaysOld(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "logsDaysOld");
    }


    public Integer getLogsDaysOld()
    {
        return getLogsDaysOld(getSession().getSessionContext());
    }


    public int getLogsDaysOldAsPrimitive(SessionContext ctx)
    {
        Integer value = getLogsDaysOld(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getLogsDaysOldAsPrimitive()
    {
        return getLogsDaysOldAsPrimitive(getSession().getSessionContext());
    }


    public void setLogsDaysOld(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "logsDaysOld", value);
    }


    public void setLogsDaysOld(Integer value)
    {
        setLogsDaysOld(getSession().getSessionContext(), value);
    }


    public void setLogsDaysOld(SessionContext ctx, int value)
    {
        setLogsDaysOld(ctx, Integer.valueOf(value));
    }


    public void setLogsDaysOld(int value)
    {
        setLogsDaysOld(getSession().getSessionContext(), value);
    }


    public EnumerationValue getLogsOperator(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "logsOperator");
    }


    public EnumerationValue getLogsOperator()
    {
        return getLogsOperator(getSession().getSessionContext());
    }


    public void setLogsOperator(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "logsOperator", value);
    }


    public void setLogsOperator(EnumerationValue value)
    {
        setLogsOperator(getSession().getSessionContext(), value);
    }


    public String getLogText()
    {
        return getLogText(getSession().getSessionContext());
    }


    public Boolean isLogToDatabase(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "logToDatabase");
    }


    public Boolean isLogToDatabase()
    {
        return isLogToDatabase(getSession().getSessionContext());
    }


    public boolean isLogToDatabaseAsPrimitive(SessionContext ctx)
    {
        Boolean value = isLogToDatabase(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isLogToDatabaseAsPrimitive()
    {
        return isLogToDatabaseAsPrimitive(getSession().getSessionContext());
    }


    public void setLogToDatabase(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "logToDatabase", value);
    }


    public void setLogToDatabase(Boolean value)
    {
        setLogToDatabase(getSession().getSessionContext(), value);
    }


    public void setLogToDatabase(SessionContext ctx, boolean value)
    {
        setLogToDatabase(ctx, Boolean.valueOf(value));
    }


    public void setLogToDatabase(boolean value)
    {
        setLogToDatabase(getSession().getSessionContext(), value);
    }


    public Boolean isLogToFile(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "logToFile");
    }


    public Boolean isLogToFile()
    {
        return isLogToFile(getSession().getSessionContext());
    }


    public boolean isLogToFileAsPrimitive(SessionContext ctx)
    {
        Boolean value = isLogToFile(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isLogToFileAsPrimitive()
    {
        return isLogToFileAsPrimitive(getSession().getSessionContext());
    }


    public void setLogToFile(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "logToFile", value);
    }


    public void setLogToFile(Boolean value)
    {
        setLogToFile(getSession().getSessionContext(), value);
    }


    public void setLogToFile(SessionContext ctx, boolean value)
    {
        setLogToFile(ctx, Boolean.valueOf(value));
    }


    public void setLogToFile(boolean value)
    {
        setLogToFile(getSession().getSessionContext(), value);
    }


    public String getNodeGroup(SessionContext ctx)
    {
        return (String)getProperty(ctx, "nodeGroup");
    }


    public String getNodeGroup()
    {
        return getNodeGroup(getSession().getSessionContext());
    }


    public void setNodeGroup(SessionContext ctx, String value)
    {
        setProperty(ctx, "nodeGroup", value);
    }


    public void setNodeGroup(String value)
    {
        setNodeGroup(getSession().getSessionContext(), value);
    }


    public Integer getNodeID(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "nodeID");
    }


    public Integer getNodeID()
    {
        return getNodeID(getSession().getSessionContext());
    }


    public int getNodeIDAsPrimitive(SessionContext ctx)
    {
        Integer value = getNodeID(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getNodeIDAsPrimitive()
    {
        return getNodeIDAsPrimitive(getSession().getSessionContext());
    }


    public void setNodeID(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "nodeID", value);
    }


    public void setNodeID(Integer value)
    {
        setNodeID(getSession().getSessionContext(), value);
    }


    public void setNodeID(SessionContext ctx, int value)
    {
        setNodeID(ctx, Integer.valueOf(value));
    }


    public void setNodeID(int value)
    {
        setNodeID(getSession().getSessionContext(), value);
    }


    public Integer getNumberOfRetries(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "numberOfRetries");
    }


    public Integer getNumberOfRetries()
    {
        return getNumberOfRetries(getSession().getSessionContext());
    }


    public int getNumberOfRetriesAsPrimitive(SessionContext ctx)
    {
        Integer value = getNumberOfRetries(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getNumberOfRetriesAsPrimitive()
    {
        return getNumberOfRetriesAsPrimitive(getSession().getSessionContext());
    }


    public void setNumberOfRetries(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "numberOfRetries", value);
    }


    public void setNumberOfRetries(Integer value)
    {
        setNumberOfRetries(getSession().getSessionContext(), value);
    }


    public void setNumberOfRetries(SessionContext ctx, int value)
    {
        setNumberOfRetries(ctx, Integer.valueOf(value));
    }


    public void setNumberOfRetries(int value)
    {
        setNumberOfRetries(getSession().getSessionContext(), value);
    }


    public List<Step> getPendingSteps(SessionContext ctx)
    {
        List<Step> items = getLinkedItems(ctx, true, GeneratedProcessingConstants.Relations.CRONJOBPENDINGSTEPSRELATION, "Step", null,
                        Utilities.getRelationOrderingOverride(CRONJOBPENDINGSTEPSRELATION_SRC_ORDERED, true), false);
        return items;
    }


    public List<Step> getPendingSteps()
    {
        return getPendingSteps(getSession().getSessionContext());
    }


    public long getPendingStepsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedProcessingConstants.Relations.CRONJOBPENDINGSTEPSRELATION, "Step", null);
    }


    public long getPendingStepsCount()
    {
        return getPendingStepsCount(getSession().getSessionContext());
    }


    public void setPendingSteps(SessionContext ctx, List<Step> value)
    {
        setLinkedItems(ctx, true, GeneratedProcessingConstants.Relations.CRONJOBPENDINGSTEPSRELATION, null, value,
                        Utilities.getRelationOrderingOverride(CRONJOBPENDINGSTEPSRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CRONJOBPENDINGSTEPSRELATION_MARKMODIFIED));
    }


    public void setPendingSteps(List<Step> value)
    {
        setPendingSteps(getSession().getSessionContext(), value);
    }


    public void addToPendingSteps(SessionContext ctx, Step value)
    {
        addLinkedItems(ctx, true, GeneratedProcessingConstants.Relations.CRONJOBPENDINGSTEPSRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CRONJOBPENDINGSTEPSRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CRONJOBPENDINGSTEPSRELATION_MARKMODIFIED));
    }


    public void addToPendingSteps(Step value)
    {
        addToPendingSteps(getSession().getSessionContext(), value);
    }


    public void removeFromPendingSteps(SessionContext ctx, Step value)
    {
        removeLinkedItems(ctx, true, GeneratedProcessingConstants.Relations.CRONJOBPENDINGSTEPSRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CRONJOBPENDINGSTEPSRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CRONJOBPENDINGSTEPSRELATION_MARKMODIFIED));
    }


    public void removeFromPendingSteps(Step value)
    {
        removeFromPendingSteps(getSession().getSessionContext(), value);
    }


    public Integer getPriority(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "priority");
    }


    public Integer getPriority()
    {
        return getPriority(getSession().getSessionContext());
    }


    public int getPriorityAsPrimitive(SessionContext ctx)
    {
        Integer value = getPriority(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPriorityAsPrimitive()
    {
        return getPriorityAsPrimitive(getSession().getSessionContext());
    }


    public void setPriority(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "priority", value);
    }


    public void setPriority(Integer value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public void setPriority(SessionContext ctx, int value)
    {
        setPriority(ctx, Integer.valueOf(value));
    }


    public void setPriority(int value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public List<Step> getProcessedSteps(SessionContext ctx)
    {
        List<Step> items = getLinkedItems(ctx, true, GeneratedProcessingConstants.Relations.CRONJOBPROCESSEDSTEPSRELATION, "Step", null,
                        Utilities.getRelationOrderingOverride(CRONJOBPROCESSEDSTEPSRELATION_SRC_ORDERED, true), false);
        return items;
    }


    public List<Step> getProcessedSteps()
    {
        return getProcessedSteps(getSession().getSessionContext());
    }


    public long getProcessedStepsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedProcessingConstants.Relations.CRONJOBPROCESSEDSTEPSRELATION, "Step", null);
    }


    public long getProcessedStepsCount()
    {
        return getProcessedStepsCount(getSession().getSessionContext());
    }


    public void setProcessedSteps(SessionContext ctx, List<Step> value)
    {
        setLinkedItems(ctx, true, GeneratedProcessingConstants.Relations.CRONJOBPROCESSEDSTEPSRELATION, null, value,
                        Utilities.getRelationOrderingOverride(CRONJOBPROCESSEDSTEPSRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CRONJOBPROCESSEDSTEPSRELATION_MARKMODIFIED));
    }


    public void setProcessedSteps(List<Step> value)
    {
        setProcessedSteps(getSession().getSessionContext(), value);
    }


    public void addToProcessedSteps(SessionContext ctx, Step value)
    {
        addLinkedItems(ctx, true, GeneratedProcessingConstants.Relations.CRONJOBPROCESSEDSTEPSRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CRONJOBPROCESSEDSTEPSRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CRONJOBPROCESSEDSTEPSRELATION_MARKMODIFIED));
    }


    public void addToProcessedSteps(Step value)
    {
        addToProcessedSteps(getSession().getSessionContext(), value);
    }


    public void removeFromProcessedSteps(SessionContext ctx, Step value)
    {
        removeLinkedItems(ctx, true, GeneratedProcessingConstants.Relations.CRONJOBPROCESSEDSTEPSRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CRONJOBPROCESSEDSTEPSRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CRONJOBPROCESSEDSTEPSRELATION_MARKMODIFIED));
    }


    public void removeFromProcessedSteps(Step value)
    {
        removeFromProcessedSteps(getSession().getSessionContext(), value);
    }


    public Integer getQueryCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "queryCount");
    }


    public Integer getQueryCount()
    {
        return getQueryCount(getSession().getSessionContext());
    }


    public int getQueryCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getQueryCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getQueryCountAsPrimitive()
    {
        return getQueryCountAsPrimitive(getSession().getSessionContext());
    }


    public void setQueryCount(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "queryCount", value);
    }


    public void setQueryCount(Integer value)
    {
        setQueryCount(getSession().getSessionContext(), value);
    }


    public void setQueryCount(SessionContext ctx, int value)
    {
        setQueryCount(ctx, Integer.valueOf(value));
    }


    public void setQueryCount(int value)
    {
        setQueryCount(getSession().getSessionContext(), value);
    }


    public Boolean isRemoveOnExit(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "removeOnExit");
    }


    public Boolean isRemoveOnExit()
    {
        return isRemoveOnExit(getSession().getSessionContext());
    }


    public boolean isRemoveOnExitAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRemoveOnExit(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRemoveOnExitAsPrimitive()
    {
        return isRemoveOnExitAsPrimitive(getSession().getSessionContext());
    }


    public void setRemoveOnExit(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "removeOnExit", value);
    }


    public void setRemoveOnExit(Boolean value)
    {
        setRemoveOnExit(getSession().getSessionContext(), value);
    }


    public void setRemoveOnExit(SessionContext ctx, boolean value)
    {
        setRemoveOnExit(ctx, Boolean.valueOf(value));
    }


    public void setRemoveOnExit(boolean value)
    {
        setRemoveOnExit(getSession().getSessionContext(), value);
    }


    public Boolean isRequestAbort(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "requestAbort");
    }


    public Boolean isRequestAbort()
    {
        return isRequestAbort(getSession().getSessionContext());
    }


    public boolean isRequestAbortAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRequestAbort(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRequestAbortAsPrimitive()
    {
        return isRequestAbortAsPrimitive(getSession().getSessionContext());
    }


    public void setRequestAbort(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "requestAbort", value);
    }


    public void setRequestAbort(Boolean value)
    {
        setRequestAbort(getSession().getSessionContext(), value);
    }


    public void setRequestAbort(SessionContext ctx, boolean value)
    {
        setRequestAbort(ctx, Boolean.valueOf(value));
    }


    public void setRequestAbort(boolean value)
    {
        setRequestAbort(getSession().getSessionContext(), value);
    }


    public Boolean isRequestAbortStep(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "requestAbortStep");
    }


    public Boolean isRequestAbortStep()
    {
        return isRequestAbortStep(getSession().getSessionContext());
    }


    public boolean isRequestAbortStepAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRequestAbortStep(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRequestAbortStepAsPrimitive()
    {
        return isRequestAbortStepAsPrimitive(getSession().getSessionContext());
    }


    public void setRequestAbortStep(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "requestAbortStep", value);
    }


    public void setRequestAbortStep(Boolean value)
    {
        setRequestAbortStep(getSession().getSessionContext(), value);
    }


    public void setRequestAbortStep(SessionContext ctx, boolean value)
    {
        setRequestAbortStep(ctx, Boolean.valueOf(value));
    }


    public void setRequestAbortStep(boolean value)
    {
        setRequestAbortStep(getSession().getSessionContext(), value);
    }


    public EnumerationValue getResult(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "result");
    }


    public EnumerationValue getResult()
    {
        return getResult(getSession().getSessionContext());
    }


    public void setResult(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "result", value);
    }


    public void setResult(EnumerationValue value)
    {
        setResult(getSession().getSessionContext(), value);
    }


    public Boolean isRetry(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "retry");
    }


    public Boolean isRetry()
    {
        return isRetry(getSession().getSessionContext());
    }


    public boolean isRetryAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRetry(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRetryAsPrimitive()
    {
        return isRetryAsPrimitive(getSession().getSessionContext());
    }


    public void setRetry(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "retry", value);
    }


    public void setRetry(Boolean value)
    {
        setRetry(getSession().getSessionContext(), value);
    }


    public void setRetry(SessionContext ctx, boolean value)
    {
        setRetry(ctx, Boolean.valueOf(value));
    }


    public void setRetry(boolean value)
    {
        setRetry(getSession().getSessionContext(), value);
    }


    public Integer getRunningOnClusterNode(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "runningOnClusterNode");
    }


    public Integer getRunningOnClusterNode()
    {
        return getRunningOnClusterNode(getSession().getSessionContext());
    }


    public int getRunningOnClusterNodeAsPrimitive(SessionContext ctx)
    {
        Integer value = getRunningOnClusterNode(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getRunningOnClusterNodeAsPrimitive()
    {
        return getRunningOnClusterNodeAsPrimitive(getSession().getSessionContext());
    }


    public void setRunningOnClusterNode(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "runningOnClusterNode", value);
    }


    public void setRunningOnClusterNode(Integer value)
    {
        setRunningOnClusterNode(getSession().getSessionContext(), value);
    }


    public void setRunningOnClusterNode(SessionContext ctx, int value)
    {
        setRunningOnClusterNode(ctx, Integer.valueOf(value));
    }


    public void setRunningOnClusterNode(int value)
    {
        setRunningOnClusterNode(getSession().getSessionContext(), value);
    }


    public Boolean isSendEmail(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "sendEmail");
    }


    public Boolean isSendEmail()
    {
        return isSendEmail(getSession().getSessionContext());
    }


    public boolean isSendEmailAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSendEmail(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSendEmailAsPrimitive()
    {
        return isSendEmailAsPrimitive(getSession().getSessionContext());
    }


    public void setSendEmail(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "sendEmail", value);
    }


    public void setSendEmail(Boolean value)
    {
        setSendEmail(getSession().getSessionContext(), value);
    }


    public void setSendEmail(SessionContext ctx, boolean value)
    {
        setSendEmail(ctx, Boolean.valueOf(value));
    }


    public void setSendEmail(boolean value)
    {
        setSendEmail(getSession().getSessionContext(), value);
    }


    Map getSessionContextValues(SessionContext ctx)
    {
        return (Map)getProperty(ctx, "sessionContextValues");
    }


    Map getSessionContextValues()
    {
        return getSessionContextValues(getSession().getSessionContext());
    }


    void setSessionContextValues(SessionContext ctx, Map value)
    {
        setProperty(ctx, "sessionContextValues", value);
    }


    void setSessionContextValues(Map value)
    {
        setSessionContextValues(getSession().getSessionContext(), value);
    }


    public Currency getSessionCurrency(SessionContext ctx)
    {
        return (Currency)getProperty(ctx, "sessionCurrency");
    }


    public Currency getSessionCurrency()
    {
        return getSessionCurrency(getSession().getSessionContext());
    }


    public void setSessionCurrency(SessionContext ctx, Currency value)
    {
        setProperty(ctx, "sessionCurrency", value);
    }


    public void setSessionCurrency(Currency value)
    {
        setSessionCurrency(getSession().getSessionContext(), value);
    }


    public Language getSessionLanguage(SessionContext ctx)
    {
        return (Language)getProperty(ctx, "sessionLanguage");
    }


    public Language getSessionLanguage()
    {
        return getSessionLanguage(getSession().getSessionContext());
    }


    public void setSessionLanguage(SessionContext ctx, Language value)
    {
        setProperty(ctx, "sessionLanguage", value);
    }


    public void setSessionLanguage(Language value)
    {
        setSessionLanguage(getSession().getSessionContext(), value);
    }


    public User getSessionUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "sessionUser");
    }


    public User getSessionUser()
    {
        return getSessionUser(getSession().getSessionContext());
    }


    public void setSessionUser(SessionContext ctx, User value)
    {
        setProperty(ctx, "sessionUser", value);
    }


    public void setSessionUser(User value)
    {
        setSessionUser(getSession().getSessionContext(), value);
    }


    public Boolean isSingleExecutable(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "singleExecutable");
    }


    public Boolean isSingleExecutable()
    {
        return isSingleExecutable(getSession().getSessionContext());
    }


    public boolean isSingleExecutableAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSingleExecutable(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSingleExecutableAsPrimitive()
    {
        return isSingleExecutableAsPrimitive(getSession().getSessionContext());
    }


    public void setSingleExecutable(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "singleExecutable", value);
    }


    public void setSingleExecutable(Boolean value)
    {
        setSingleExecutable(getSession().getSessionContext(), value);
    }


    public void setSingleExecutable(SessionContext ctx, boolean value)
    {
        setSingleExecutable(ctx, Boolean.valueOf(value));
    }


    public void setSingleExecutable(boolean value)
    {
        setSingleExecutable(getSession().getSessionContext(), value);
    }


    public Date getStartTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "startTime");
    }


    public Date getStartTime()
    {
        return getStartTime(getSession().getSessionContext());
    }


    public void setStartTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "startTime", value);
    }


    public void setStartTime(Date value)
    {
        setStartTime(getSession().getSessionContext(), value);
    }


    public EnumerationValue getStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "status");
    }


    public EnumerationValue getStatus()
    {
        return getStatus(getSession().getSessionContext());
    }


    public void setStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "status", value);
    }


    public void setStatus(EnumerationValue value)
    {
        setStatus(getSession().getSessionContext(), value);
    }


    public List<Trigger> getTriggers(SessionContext ctx)
    {
        return (List<Trigger>)TRIGGERSHANDLER.getValues(ctx, (Item)this);
    }


    public List<Trigger> getTriggers()
    {
        return getTriggers(getSession().getSessionContext());
    }


    public void setTriggers(SessionContext ctx, List<Trigger> value)
    {
        TRIGGERSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setTriggers(List<Trigger> value)
    {
        setTriggers(getSession().getSessionContext(), value);
    }


    public void addToTriggers(SessionContext ctx, Trigger value)
    {
        TRIGGERSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToTriggers(Trigger value)
    {
        addToTriggers(getSession().getSessionContext(), value);
    }


    public void removeFromTriggers(SessionContext ctx, Trigger value)
    {
        TRIGGERSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromTriggers(Trigger value)
    {
        removeFromTriggers(getSession().getSessionContext(), value);
    }


    public Boolean isUseReadOnlyDatasource(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "useReadOnlyDatasource");
    }


    public Boolean isUseReadOnlyDatasource()
    {
        return isUseReadOnlyDatasource(getSession().getSessionContext());
    }


    public boolean isUseReadOnlyDatasourceAsPrimitive(SessionContext ctx)
    {
        Boolean value = isUseReadOnlyDatasource(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isUseReadOnlyDatasourceAsPrimitive()
    {
        return isUseReadOnlyDatasourceAsPrimitive(getSession().getSessionContext());
    }


    public void setUseReadOnlyDatasource(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "useReadOnlyDatasource", value);
    }


    public void setUseReadOnlyDatasource(Boolean value)
    {
        setUseReadOnlyDatasource(getSession().getSessionContext(), value);
    }


    public void setUseReadOnlyDatasource(SessionContext ctx, boolean value)
    {
        setUseReadOnlyDatasource(ctx, Boolean.valueOf(value));
    }


    public void setUseReadOnlyDatasource(boolean value)
    {
        setUseReadOnlyDatasource(getSession().getSessionContext(), value);
    }


    public abstract Collection<ChangeDescriptor> getChanges(SessionContext paramSessionContext);


    public abstract Collection<LogFile> getLogFiles(SessionContext paramSessionContext);


    public abstract void setLogFiles(SessionContext paramSessionContext, Collection<LogFile> paramCollection);


    public abstract String getLogText(SessionContext paramSessionContext);
}
