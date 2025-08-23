package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.ErrorMode;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;

public class JobModel extends ItemModel
{
    public static final String _TYPECODE = "Job";
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


    public JobModel()
    {
    }


    public JobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public JobModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public JobModel(String _code, Integer _nodeID, ItemModel _owner)
    {
        setCode(_code);
        setNodeID(_nodeID);
        setOwner(_owner);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
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


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "cronJobs", type = Accessor.Type.GETTER)
    public Collection<CronJobModel> getCronJobs()
    {
        return (Collection<CronJobModel>)getPersistenceContext().getPropertyValue("cronJobs");
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


    @Accessor(qualifier = "errorMode", type = Accessor.Type.GETTER)
    public ErrorMode getErrorMode()
    {
        return (ErrorMode)getPersistenceContext().getPropertyValue("errorMode");
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


    @Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
    public Integer getPriority()
    {
        return (Integer)getPersistenceContext().getPropertyValue("priority");
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


    @Accessor(qualifier = "restrictions", type = Accessor.Type.GETTER)
    public List<JobSearchRestrictionModel> getRestrictions()
    {
        return (List<JobSearchRestrictionModel>)getPersistenceContext().getPropertyValue("restrictions");
    }


    @Accessor(qualifier = "retry", type = Accessor.Type.GETTER)
    public Boolean getRetry()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("retry");
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


    @Accessor(qualifier = "errorMode", type = Accessor.Type.SETTER)
    public void setErrorMode(ErrorMode value)
    {
        getPersistenceContext().setPropertyValue("errorMode", value);
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


    @Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
    public void setPriority(Integer value)
    {
        getPersistenceContext().setPropertyValue("priority", value);
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


    @Accessor(qualifier = "restrictions", type = Accessor.Type.SETTER)
    public void setRestrictions(List<JobSearchRestrictionModel> value)
    {
        getPersistenceContext().setPropertyValue("restrictions", value);
    }


    @Accessor(qualifier = "retry", type = Accessor.Type.SETTER)
    public void setRetry(Boolean value)
    {
        getPersistenceContext().setPropertyValue("retry", value);
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
