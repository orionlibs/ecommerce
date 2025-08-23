package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.commons.jalo.renderer.RendererTemplate;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.processing.constants.GeneratedProcessingConstants;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedJob extends GenericItem
{
    public static final String CODE = "code";
    public static final String NODEID = "nodeID";
    public static final String NODEGROUP = "nodeGroup";
    public static final String ERRORMODE = "errorMode";
    public static final String LOGTOFILE = "logToFile";
    public static final String LOGTODATABASE = "logToDatabase";
    public static final String LOGLEVELFILE = "logLevelFile";
    public static final String LOGLEVELDATABASE = "logLevelDatabase";
    public static final String SESSIONUSER = "sessionUser";
    public static final String SESSIONLANGUAGE = "sessionLanguage";
    public static final String SESSIONCURRENCY = "sessionCurrency";
    public static final String SESSIONCONTEXTVALUES = "sessionContextValues";
    public static final String ACTIVE = "active";
    public static final String RETRY = "retry";
    public static final String SINGLEEXECUTABLE = "singleExecutable";
    public static final String EMAILADDRESS = "emailAddress";
    public static final String SENDEMAIL = "sendEmail";
    public static final String CHANGERECORDINGENABLED = "changeRecordingEnabled";
    public static final String REQUESTABORT = "requestAbort";
    public static final String REQUESTABORTSTEP = "requestAbortStep";
    public static final String PRIORITY = "priority";
    public static final String REMOVEONEXIT = "removeOnExit";
    public static final String EMAILNOTIFICATIONTEMPLATE = "emailNotificationTemplate";
    public static final String ALTERNATIVEDATASOURCEID = "alternativeDataSourceID";
    public static final String USEREADONLYDATASOURCE = "useReadOnlyDatasource";
    public static final String NUMBEROFRETRIES = "numberOfRetries";
    public static final String RESTRICTIONS = "restrictions";
    public static final String TRIGGERS = "triggers";
    public static final String CRONJOBS = "cronJobs";
    protected static final OneToManyHandler<JobSearchRestriction> RESTRICTIONSHANDLER = new OneToManyHandler(GeneratedProcessingConstants.TC.JOBSEARCHRESTRICTION, true, "job", "jobPOS", true, true, 2);
    protected static final OneToManyHandler<Trigger> TRIGGERSHANDLER = new OneToManyHandler(GeneratedProcessingConstants.TC.TRIGGER, true, "job", null, false, true, 2);
    protected static final OneToManyHandler<CronJob> CRONJOBSHANDLER = new OneToManyHandler(GeneratedProcessingConstants.TC.CRONJOB, false, "job", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("nodeID", Item.AttributeMode.INITIAL);
        tmp.put("nodeGroup", Item.AttributeMode.INITIAL);
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
        tmp.put("changeRecordingEnabled", Item.AttributeMode.INITIAL);
        tmp.put("requestAbort", Item.AttributeMode.INITIAL);
        tmp.put("requestAbortStep", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("removeOnExit", Item.AttributeMode.INITIAL);
        tmp.put("emailNotificationTemplate", Item.AttributeMode.INITIAL);
        tmp.put("alternativeDataSourceID", Item.AttributeMode.INITIAL);
        tmp.put("useReadOnlyDatasource", Item.AttributeMode.INITIAL);
        tmp.put("numberOfRetries", Item.AttributeMode.INITIAL);
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


    public Collection<CronJob> getCronJobs(SessionContext ctx)
    {
        return CRONJOBSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<CronJob> getCronJobs()
    {
        return getCronJobs(getSession().getSessionContext());
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


    protected void setNodeID(SessionContext ctx, Integer value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'nodeID' is not changeable", 0);
        }
        setProperty(ctx, "nodeID", value);
    }


    protected void setNodeID(Integer value)
    {
        setNodeID(getSession().getSessionContext(), value);
    }


    protected void setNodeID(SessionContext ctx, int value)
    {
        setNodeID(ctx, Integer.valueOf(value));
    }


    protected void setNodeID(int value)
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


    public List<JobSearchRestriction> getRestrictions(SessionContext ctx)
    {
        return (List<JobSearchRestriction>)RESTRICTIONSHANDLER.getValues(ctx, (Item)this);
    }


    public List<JobSearchRestriction> getRestrictions()
    {
        return getRestrictions(getSession().getSessionContext());
    }


    public void setRestrictions(SessionContext ctx, List<JobSearchRestriction> value)
    {
        RESTRICTIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setRestrictions(List<JobSearchRestriction> value)
    {
        setRestrictions(getSession().getSessionContext(), value);
    }


    public void addToRestrictions(SessionContext ctx, JobSearchRestriction value)
    {
        RESTRICTIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToRestrictions(JobSearchRestriction value)
    {
        addToRestrictions(getSession().getSessionContext(), value);
    }


    public void removeFromRestrictions(SessionContext ctx, JobSearchRestriction value)
    {
        RESTRICTIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromRestrictions(JobSearchRestriction value)
    {
        removeFromRestrictions(getSession().getSessionContext(), value);
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
}
