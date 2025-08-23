package de.hybris.platform.cronjob.jalo;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.commons.jalo.CommonsManager;
import de.hybris.platform.commons.jalo.renderer.RendererTemplate;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.constants.GeneratedCronJobConstants;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.persistence.hjmp.HJMPException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.task.jalo.TriggerTask;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.util.logging.context.LoggingContextFactory;
import de.hybris.platform.util.mail.MailUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class CronJob extends GeneratedCronJob
{
    private static final Logger LOG = Logger.getLogger(CronJob.class.getName());
    private Map changeListeners;
    public static final String CFG_FILTERED_CTX_ATTRIBUTES = "cronjob.ctx.filtered.attributes";
    protected static final String IMPORT_MODE = "import.mode";
    public static final String FILTERED_CTX_ATTRIBUTES_IN_IMPORT_MODE = "cronjob.ctx.filtered.attributes.in.impex.import.mode";
    private static final ThreadLocal<ArrayStack> _CURRENT_CRONJOB_STACK = (ThreadLocal<ArrayStack>)new Object();


    protected static final void setCurrentlyExecutingCronJob(CronJob cronjob)
    {
        if(cronjob == null)
        {
            throw new NullPointerException("current CronJob cannot be null");
        }
        set(cronjob);
    }


    private static void set(CronJob cronjob)
    {
        Preconditions.checkArgument((cronjob != null));
        ArrayStack stack = _CURRENT_CRONJOB_STACK.get();
        if(stack.isEmpty() || !stack.peek().equals(cronjob))
        {
            if(stack.contains(cronjob))
            {
                stack.remove(cronjob);
            }
            stack.push(cronjob);
        }
    }


    private static void unset()
    {
        ArrayStack stack = _CURRENT_CRONJOB_STACK.get();
        if(!stack.isEmpty())
        {
            stack.pop();
        }
    }


    private static CronJob get()
    {
        ArrayStack stack = _CURRENT_CRONJOB_STACK.get();
        return stack.isEmpty() ? null : (CronJob)stack.peek();
    }


    protected static final void unsetCurrentlyExecutingCronJob()
    {
        unset();
    }


    protected static final CronJob getCurrentlyExecutingCronJob()
    {
        CronJob cronjob = getCurrentlyExecutingCronJobFailSave();
        if(cronjob == null)
        {
            throw new IllegalStateException("current cronjob is not set");
        }
        return cronjob;
    }


    protected static final CronJob getCurrentlyExecutingCronJobFailSave()
    {
        return get();
    }


    protected static boolean hasCurrentlyExecutingCronJob()
    {
        return (get() != null);
    }


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!checkMandatoryAttribute("job", allAttributes, missing))
        {
            throw new JaloInvalidParameterException("missing " + missing + " for creating a new CronJob", 0);
        }
        adjustAttributes(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    protected void adjustAttributes(SessionContext ctx, Item.ItemAttributeMap attributes)
    {
        Job job = (Job)attributes.get("job");
        if(job instanceof BatchJob)
        {
            attributes.put("pendingSteps", ((BatchJob)job).getSteps(ctx));
        }
        if(attributes.get("emailNotificationTemplate") == null)
        {
            RendererTemplate template = CronJobManager.getInstance().getDefaultCronJobFinishNotificationTemplate();
            if(template != null)
            {
                attributes.put("emailNotificationTemplate", template);
            }
        }
        if(attributes.get("code") == null)
        {
            attributes.put("code", CronJobManager.getInstance().getNextCronjobNumber());
        }
        attributes.put("active", Boolean.valueOf(Boolean.TRUE.equals(attributes.get("active"))));
        if(attributes.get("status") == null)
        {
            attributes.put("status",
                            EnumerationManager.getInstance().getEnumerationValue(GeneratedCronJobConstants.TC.CRONJOBSTATUS, GeneratedCronJobConstants.Enumerations.CronJobStatus.UNKNOWN));
        }
        if(attributes.get("result") == null)
        {
            attributes.put("result",
                            EnumerationManager.getInstance().getEnumerationValue(GeneratedCronJobConstants.TC.CRONJOBRESULT, GeneratedCronJobConstants.Enumerations.CronJobResult.UNKNOWN));
        }
        if(attributes.get("sessionUser") == null)
        {
            User user = (ctx != null) ? ctx.getUser() : null;
            if(user == null)
            {
                user = JaloSession.getCurrentSession().getUser();
            }
            attributes.put("sessionUser", user);
        }
        if(attributes.get("sessionLanguage") == null)
        {
            Language language = (ctx != null) ? ctx.getLanguage() : null;
            if(language == null)
            {
                language = JaloSession.getCurrentSession().getSessionContext().getLanguage();
            }
            attributes.put("sessionLanguage", language);
        }
        if(attributes.get("sessionCurrency") == null)
        {
            Currency currency = (ctx != null) ? ctx.getCurrency() : null;
            if(currency == null)
            {
                currency = JaloSession.getCurrentSession().getSessionContext().getCurrency();
            }
            attributes.put("sessionCurrency", currency);
        }
        Map<String, Object> sessionCtxAttributes = new HashMap<>((ctx != null) ? ctx.getAttributes() : JaloSession.getCurrentSession().getSessionContext().getAttributes());
        Map<String, Object> extraValues = filterSessionCtxAttributesAddedDuringImportCronJobByImpex(sessionCtxAttributes);
        for(Iterator<Map.Entry<String, Object>> it = extraValues.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry<String, Object> mapentry = it.next();
            String key = mapentry.getKey();
            if(key.equals("currency") || key.equals("user") || key.equals("language") || "_slsession_"
                            .equals(key))
            {
                it.remove();
                continue;
            }
            Object val = mapentry.getValue();
            try
            {
                val = Utilities.copyViaSerializationIfNecessary(val);
            }
            catch(Throwable e2)
            {
                if(LOG.isInfoEnabled())
                {
                    LOG.info("WARN: not serializable object in session while creating CronJob: " + val.getClass().getName());
                }
                it.remove();
            }
        }
        attributes.put("sessionContextValues", extraValues);
    }


    protected Map<String, Object> filterSessionCtxAttributesAddedDuringImportCronJobByImpex(Map<String, Object> sessionContextAttributes)
    {
        Objects.requireNonNull(sessionContextAttributes, "sessionContextAttributes cannot be null");
        if(isCronJobBeingImportedByImpex(sessionContextAttributes))
        {
            Set<String> sessionCtxAttributesToIgnore = getSessionCtxAttributesToIgnore();
            HashMap<String, Object> filteredSessionCtxAttributes = new HashMap<>();
            Stream<Map.Entry<String, Object>> entryStream = sessionContextAttributes.entrySet().stream().filter(entry -> !sessionCtxAttributesToIgnore.contains(entry.getKey()));
            entryStream.forEach(entry -> filteredSessionCtxAttributes.put(entry.getKey(), entry.getValue()));
            return filteredSessionCtxAttributes;
        }
        return sessionContextAttributes;
    }


    protected boolean isCronJobBeingImportedByImpex(Map<String, Object> sessionContextAttributes)
    {
        if(sessionContextAttributes.containsKey("import.mode"))
        {
            return Boolean.TRUE.equals(sessionContextAttributes.get("import.mode"));
        }
        return false;
    }


    protected Set<String> getSessionCtxAttributesToIgnore()
    {
        String[] ctxAttributesToIgnoreArray = Config.getParameter("cronjob.ctx.filtered.attributes.in.impex.import.mode").split(",");
        return new HashSet<>(Arrays.asList(ctxAttributesToIgnoreArray));
    }


    @ForceJALO(reason = "something else")
    protected void setJob(SessionContext ctx, Job job)
    {
        if(job == null)
        {
            throw new JaloInvalidParameterException("job cannot be null", 0);
        }
        if(job instanceof BatchJob)
        {
            setPendingSteps(ctx, ((BatchJob)job).getSteps(ctx));
        }
        else
        {
            setPendingSteps(ctx, Collections.EMPTY_LIST);
        }
        super.setJob(ctx, job);
    }


    @ForceJALO(reason = "something else")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        SessionContext myCtx = new SessionContext(ctx);
        myCtx.removeAttribute("is.hmc.session");
        Collection<LogFile> logfiles = getLogFiles(myCtx);
        for(LogFile f : logfiles)
        {
            f.remove(myCtx);
        }
        Collection<Trigger> triggers = getTriggers(myCtx);
        for(Trigger trigger : triggers)
        {
            trigger.remove(myCtx);
        }
        JaloSession jalosession = getSession();
        Collection<PK> logPKs = getLogPKs(myCtx);
        for(PK logPK : logPKs)
        {
            JobLog log = (JobLog)jalosession.getItem(logPK);
            log.remove(myCtx);
        }
        Collection<PK> changePKs = getChangePKs(myCtx);
        for(PK changePK : changePKs)
        {
            ChangeDescriptor changedesc = (ChangeDescriptor)jalosession.getItem(changePK);
            changedesc.remove(myCtx);
        }
        super.remove(ctx);
    }


    public void resetCounter()
    {
        setTransientObject("processCounter", null);
    }


    public int getCounter()
    {
        Integer counter = (Integer)getTransientObject("processCounter");
        return (counter == null) ? 0 : counter.intValue();
    }


    public int addToCounter(int add)
    {
        Integer counter = (Integer)getTransientObject("processCounter");
        setTransientObject("processCounter", (counter == null) ? Integer.valueOf(add) : Integer.valueOf(counter.intValue() + add));
        return (counter == null) ? add : (counter.intValue() + add);
    }


    public JobLog addLog(String message)
    {
        return addLog(message, null, getInfoLogLevel());
    }


    @Deprecated(since = "4.3", forRemoval = false)
    public JobLog addLog(String message, Step forStep)
    {
        return addLog(message, forStep, getInfoLogLevel());
    }


    public JobLog addLog(String message, EnumerationValue level)
    {
        return addLog(message, null, level);
    }


    @Deprecated(since = "4.3", forRemoval = false)
    public JobLog addLog(String message, Step forStep, EnumerationValue level)
    {
        return addLog(message, forStep, level, true);
    }


    public EnumerationValue convertLogLevelToEnum(Level level)
    {
        switch(level.toInt())
        {
            case 10000:
                return getDebugLogLevel();
            case 20000:
                return getInfoLogLevel();
            case 30000:
                return getWarnLogLevel();
            case 40000:
                return getErrorLogLevel();
            case 50000:
                return getFatalLogLevel();
        }
        return getUnknownLogLevel();
    }


    public Level convertEnumToLogLevel(EnumerationValue enumvalue)
    {
        if(enumvalue == null)
        {
            return Level.INFO;
        }
        String level = enumvalue.getCode();
        if(GeneratedCronJobConstants.Enumerations.JobLogLevel.DEBUG.equalsIgnoreCase(level))
        {
            return Level.DEBUG;
        }
        if(GeneratedCronJobConstants.Enumerations.JobLogLevel.INFO.equalsIgnoreCase(level))
        {
            return Level.INFO;
        }
        if(GeneratedCronJobConstants.Enumerations.JobLogLevel.WARNING.equalsIgnoreCase(level))
        {
            return Level.WARN;
        }
        if(GeneratedCronJobConstants.Enumerations.JobLogLevel.ERROR.equalsIgnoreCase(level))
        {
            return Level.ERROR;
        }
        if(GeneratedCronJobConstants.Enumerations.JobLogLevel.FATAL.equalsIgnoreCase(level))
        {
            return Level.FATAL;
        }
        return Level.INFO;
    }


    protected JobLog addLog(String message, Step forStep, EnumerationValue level, boolean appendToLogFile)
    {
        if(isLogToDatabaseAsPrimitive())
        {
            if(CronJobUtils.isWritingLogToDb())
            {
                return null;
            }
            CronJobUtils.startWritingLogToDb();
            try
            {
                Level messageLevel = convertEnumToLogLevel(level);
                if(appendToLogFile && getJob() != null)
                {
                    getJob().logToFile(message, messageLevel);
                }
                Level configuredLevel = convertEnumToLogLevel(getLogLevelDatabase());
                if(messageLevel.isGreaterOrEqual((Priority)configuredLevel))
                {
                    Map<Object, Object> values = new HashMap<>();
                    if(message != null)
                    {
                        values.put("message", message);
                    }
                    values.put("cronJob", this);
                    values.put("level", (level == null) ? getInfoLogLevel() : level);
                    if(forStep != null)
                    {
                        values.put("step", forStep);
                    }
                    try
                    {
                        return (JobLog)getSession().getTypeManager().getComposedType(JobLog.class).newInstance(values);
                    }
                    catch(JaloGenericCreationException e)
                    {
                        Throwable cause = e.getCause();
                        if(cause == null)
                        {
                            throw new JaloSystemException(e);
                        }
                        if(cause instanceof RuntimeException)
                        {
                            throw (RuntimeException)cause;
                        }
                        throw new JaloSystemException(cause);
                    }
                    catch(JaloAbstractTypeException e)
                    {
                        throw new JaloInternalException(e);
                    }
                    catch(JaloItemNotFoundException e)
                    {
                        throw new JaloInternalException(e);
                    }
                }
            }
            finally
            {
                CronJobUtils.stopWritingLogToDb();
            }
        }
        return null;
    }


    private final BitSet isEnabledForCacheActive = new BitSet();
    private final BitSet isEnabledForCache = new BitSet();
    private long isEnabledForCheck = 0L;
    private long isEnabledForCheckDiff = 1000L;


    @ForceJALO(reason = "something else")
    public EnumerationValue getLogLevelDatabase(SessionContext ctx)
    {
        EnumerationValue configuredInDb = super.getLogLevelDatabase(ctx);
        if(!checkLogLevel(configuredInDb, "cronjob.logtodb.threshold", "WARN"))
        {
            String configuredTreshold = Config.getString("cronjob.logtodb.threshold", "WARN");
            LOG.info("Cronjob " +
                            getCode() + ": log to db level is " + ((configuredInDb == null) ? "null" : configuredInDb.getCode()) + " but global log threshold is " + configuredTreshold + ". (see cronjob.logtodb.threshold at project.properties). Changing back to " + configuredTreshold);
            configuredInDb = convertLogLevelToEnum(Level.toLevel(configuredTreshold));
        }
        return configuredInDb;
    }


    @ForceJALO(reason = "something else")
    public EnumerationValue getLogLevelFile(SessionContext ctx)
    {
        EnumerationValue configuredInDb = super.getLogLevelFile(ctx);
        if(!checkLogLevel(configuredInDb, "cronjob.logtofile.threshold", "INFO"))
        {
            String configuredTreshold = Config.getString("cronjob.logtofile.threshold", "INFO");
            LOG.info("Cronjob " + getCode() + ": log to file level is " + (
                            (configuredInDb == null) ? "null" : configuredInDb.getCode()) + " but global log threshold is " + configuredTreshold + ". (see cronjob.logtofile.threshold at project.properties). Changing back to " + configuredTreshold);
            configuredInDb = convertLogLevelToEnum(Level.toLevel(configuredTreshold));
        }
        return configuredInDb;
    }


    public boolean checkLogLevel(EnumerationValue configuredLevel, String configProperty, String defaultLevel)
    {
        Level _configuredLevel = convertEnumToLogLevel(configuredLevel);
        Level tresholdLevel = Level.toLevel(Config.getString(configProperty, defaultLevel));
        if(tresholdLevel.isGreaterOrEqual((Priority)_configuredLevel) && !tresholdLevel.equals(_configuredLevel))
        {
            return false;
        }
        return true;
    }


    public void setIsEnabledForCheckDifference(long diff)
    {
        this.isEnabledForCheckDiff = diff;
    }


    public final boolean isEnabledFor(Level level)
    {
        long now = System.currentTimeMillis();
        int syslogequ = level.getSyslogEquivalent();
        if(now - this.isEnabledForCheck > this.isEnabledForCheckDiff)
        {
            this.isEnabledForCheck = now;
            this.isEnabledForCache.clear();
            this.isEnabledForCacheActive.clear();
        }
        else if(this.isEnabledForCacheActive.get(syslogequ))
        {
            return this.isEnabledForCache.get(syslogequ);
        }
        boolean newValue = ((isLogToDatabaseAsPrimitive() && level.isGreaterOrEqual((Priority)convertEnumToLogLevel(getLogLevelDatabase()))) || (isLogToFileAsPrimitive() && level.isGreaterOrEqual((Priority)convertEnumToLogLevel(getLogLevelFile()))));
        this.isEnabledForCacheActive.set(syslogequ);
        if(newValue)
        {
            this.isEnabledForCache.set(syslogequ);
        }
        return newValue;
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "abstract method implementation")
    public String getLogText(SessionContext ctx)
    {
        StringBuilder logtext = new StringBuilder();
        List<JobLog> logs = new ArrayList<>(getLogs(ctx, 0, 500, "DESC"));
        for(Iterator<JobLog> it = logs.iterator(); it.hasNext(); )
        {
            JobLog jobLog = it.next();
            EnumerationValue level = jobLog.getLevel(ctx);
            StringBuilder strLevel = new StringBuilder();
            if(level != null)
            {
                strLevel.append(level.getCode()).append(": ");
            }
            Step step = jobLog.getStep(ctx);
            StringBuilder strStep = new StringBuilder();
            if(step != null)
            {
                strStep.append(step.getCode()).append(": ");
            }
            SimpleDateFormat dateformat = Utilities.getSimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            logtext.append(dateformat.format(jobLog.getCreationTime())).append(": ").append(strLevel);
            logtext.append(strStep);
            try
            {
                String jlMessage = jobLog.getMessage(ctx);
                if(jlMessage == null)
                {
                    jlMessage = "<empty>";
                }
                BufferedReader buffreader = new BufferedReader(new StringReader(jlMessage));
                for(int i = 0; i < 10; i++)
                {
                    String line = buffreader.readLine();
                    if(line == null)
                    {
                        break;
                    }
                    logtext.append(line);
                    if(i == 9)
                    {
                        logtext.append(" ...");
                    }
                    logtext.append("\n");
                }
            }
            catch(IOException e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        return logtext.toString();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List<JobLog> getLogs(SessionContext ctx, int start, int count)
    {
        return getLogs(ctx, start, count, null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    private List<JobLog> getLogs(SessionContext ctx, int start, int count, String order)
    {
        String theOrder;
        if(order == null || (!"ASC".equalsIgnoreCase(order) && !"DESC".equalsIgnoreCase(order)))
        {
            theOrder = "ASC";
        }
        else
        {
            theOrder = order;
        }
        return getSession()
                        .getFlexibleSearch()
                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" + GeneratedCronJobConstants.TC.JOBLOG + "} WHERE {cronJob} = ?me ORDER BY {" + Item.CREATION_TIME + "} " + theOrder,
                                        Collections.singletonMap("me", this),
                                        Collections.singletonList(JobLog.class), true, true, start, count).getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    private Collection<PK> getLogPKs(SessionContext ctx)
    {
        return getSession()
                        .getFlexibleSearch()
                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" + GeneratedCronJobConstants.TC.JOBLOG + "} WHERE {cronJob} = ?me ORDER BY {" + Item.CREATION_TIME + "} ASC",
                                        Collections.singletonMap("me", this),
                                        Collections.singletonList(PK.class), true, true, 0, -1).getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "abstract method implementation")
    public Collection<ChangeDescriptor> getChanges(SessionContext ctx)
    {
        return getChanges(ctx, null, null, 0, -1);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ChangeDescriptor> getChanges(SessionContext ctx, Step step)
    {
        return getChanges(ctx, step, null, 0, -1);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ChangeDescriptor> getChanges(Step step)
    {
        return getChanges(getSession().getSessionContext(), step);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ChangeDescriptor> getChanges(String changeType)
    {
        return getChanges(getSession().getSessionContext(), changeType, 0, -1);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ChangeDescriptor> getChanges(SessionContext ctx, String changeType)
    {
        return getChanges(ctx, null, changeType, 0, -1);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ChangeDescriptor> getChanges(Step step, String changeType)
    {
        return getChanges(getSession().getSessionContext(), step, changeType);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ChangeDescriptor> getChanges(SessionContext ctx, Step step, String changeType)
    {
        return getChanges(ctx, step, changeType, 0, -1);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ChangeDescriptor> getChanges(int start, int count)
    {
        return getChanges(getSession().getSessionContext(), start, count);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ChangeDescriptor> getChanges(SessionContext ctx, int start, int count)
    {
        return getChanges(ctx, null, null, start, count);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ChangeDescriptor> getChanges(Step step, int start, int count)
    {
        return getChanges(getSession().getSessionContext(), step, start, count);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ChangeDescriptor> getChanges(SessionContext ctx, Step step, int start, int count)
    {
        return getChanges(ctx, step, null, start, count);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ChangeDescriptor> getChanges(String changeType, int start, int count)
    {
        return getChanges(getSession().getSessionContext(), changeType, start, count);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ChangeDescriptor> getChanges(SessionContext ctx, String changeType, int start, int count)
    {
        return getChanges(ctx, null, changeType, start, count);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ChangeDescriptor> getChanges(Step step, String changeType, int start, int count)
    {
        return getChanges(getSession().getSessionContext(), step, changeType, start, count);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ChangeDescriptor> getChanges(SessionContext ctx, Step step, String changeType, int start, int count)
    {
        return getChanges(getSession().getSessionContext(), step, changeType, start, count, true);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ChangeDescriptor> getChanges(Step step, String changeType, int start, int count, boolean ascending)
    {
        return getChanges(getSession().getSessionContext(), step, changeType, start, count, ascending);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ChangeDescriptor> getChanges(SessionContext ctx, Step step, String changeType, int start, int count, boolean ascending)
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("me", this);
        if(changeType != null)
        {
            values.put("changeType", changeType);
        }
        if(step != null)
        {
            values.put("step", step);
        }
        return getSession()
                        .getFlexibleSearch()
                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" + GeneratedCronJobConstants.TC.CHANGEDESCRIPTOR + "} WHERE {cronJob} = ?me " + (
                                                        (changeType == null) ? "" : "AND {changeType}=?changeType ") + (
                                                        (step == null) ? "" : "AND {step}=?step ") + "ORDER BY {sequenceNumber} " + (
                                                        ascending ? "ASC" : "DESC"), values,
                                        Collections.singletonList(ChangeDescriptor.class), true, true, start, count).getResult();
    }


    private Collection<PK> getChangePKs(SessionContext ctx)
    {
        return getSession()
                        .getFlexibleSearch()
                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" + GeneratedCronJobConstants.TC.CHANGEDESCRIPTOR + "} WHERE {cronJob} = ?me ORDER BY {sequenceNumber} ASC",
                                        Collections.singletonMap("me", this), Collections.singletonList(PK.class), true, true, 0, -1).getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ChangeDescriptor getMostRecentChange(Step step, String changeType)
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("me", this);
        if(changeType != null)
        {
            values.put("changeType", changeType);
        }
        if(step != null)
        {
            values.put("step", step);
        }
        List<ChangeDescriptor> list = getSession().getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {" + getSession().getTypeManager().getComposedType(ChangeDescriptor.class).getCode() + "} WHERE {cronJob} = ?me " + ((changeType == null) ? "" : "AND {changeType}=?changeType ") + ((step == null) ? "" : "AND {step}=?step ")
                                        + "ORDER BY {sequenceNumber} DESC", values, Collections.singletonList(ChangeDescriptor.class), true, true, 0, 1).getResult();
        return list.isEmpty() ? null : list.iterator().next();
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected ChangeDescriptor addChangeDescriptor(Step forStep, String changeType, Item item, String description)
    {
        return addChangeDescriptor(getSession().getTypeManager().getComposedType(ChangeDescriptor.class), forStep, changeType, item, description);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected Integer getNextChangeNumber(ComposedType changeDescriptorType)
    {
        List<Integer> rows = FlexibleSearch.getInstance().search("SELECT MAX({sequenceNumber}) FROM {" + changeDescriptorType.getCode() + "} WHERE {cronJob}=?me ", Collections.singletonMap("me", this), Integer.class).getResult();
        return Integer.valueOf((rows.isEmpty() || rows.get(0) == null) ? 0 : (((Integer)rows.get(0)).intValue() + 1));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ChangeDescriptor addChangeDescriptor(ComposedType changeDescriptorType, Step forStep, String changeType, Item item, String description)
    {
        return addChangeDescriptor(changeDescriptorType, forStep, changeType, item, description, null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ChangeDescriptor addChangeDescriptor(ComposedType changeDescriptorType, Step forStep, String changeType, Item item, String description, Map additionalAttributes)
    {
        return addChangeDescriptor(changeDescriptorType, forStep, changeType, (item == null) ? null : item.getPK(), description, additionalAttributes);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ChangeDescriptor addChangeDescriptor(ComposedType changeDescriptorType, Step forStep, String changeType, PK item, String description, Map additionalAttributes)
    {
        if(isChangeRecordingEnabledAsPrimitive())
        {
            try
            {
                Item.ItemAttributeMap<String, CronJob> itemAttributeMap = (additionalAttributes == null) ? new Item.ItemAttributeMap() : new Item.ItemAttributeMap(additionalAttributes);
                itemAttributeMap.put("cronJob", this);
                itemAttributeMap.put("changeType", changeType);
                if(!itemAttributeMap.containsKey("sequenceNumber"))
                {
                    itemAttributeMap.put("sequenceNumber", getNextChangeNumber(changeDescriptorType));
                }
                if(forStep != null)
                {
                    itemAttributeMap.put("step", forStep);
                }
                if(item != null)
                {
                    itemAttributeMap.put("changedItem", new ItemPropertyValue(item));
                }
                if(description != null)
                {
                    itemAttributeMap.put("description", description);
                }
                ChangeDescriptor changedesc = (ChangeDescriptor)changeDescriptorType.newInstance((Map)itemAttributeMap);
                notifyChangeListeners(new ChangeEvent(changedesc));
                return changedesc;
            }
            catch(JaloItemNotFoundException e)
            {
                throw new JaloInternalException(e);
            }
            catch(JaloGenericCreationException e)
            {
                Throwable nested = e.getCause();
                if(nested == null)
                {
                    throw new JaloSystemException(e);
                }
                if(nested instanceof RuntimeException)
                {
                    throw (RuntimeException)nested;
                }
                throw new JaloSystemException(nested);
            }
            catch(JaloAbstractTypeException e)
            {
                throw new JaloSystemException(e);
            }
        }
        return null;
    }


    @ForceJALO(reason = "something else")
    public void setStatus(EnumerationValue status)
    {
        setProperty("status", status);
    }


    @ForceJALO(reason = "something else")
    public void setResult(EnumerationValue result)
    {
        setProperty("result", result);
    }


    @ForceJALO(reason = "something else")
    public void setStartTime(Date start)
    {
        setProperty("startTime", start);
    }


    @ForceJALO(reason = "something else")
    public void setEndTime(Date end)
    {
        setProperty("endTime", end);
    }


    protected void setCurrentStep(Step step, boolean undo)
    {
        setProperty("currentStep", step);
        if(step != null)
        {
            if(undo)
            {
                removeFromProcessedSteps(step);
            }
            else
            {
                removeFromPendingSteps(step);
            }
        }
    }


    protected void currentStepDone(boolean undo)
    {
        if(undo)
        {
            LinkedList<Step> pending = new LinkedList(getPendingSteps());
            pending.addFirst(getCurrentStep());
            setPendingSteps(pending);
        }
        else
        {
            addToProcessedSteps(getCurrentStep());
        }
        setCurrentStep(null, undo);
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return getCode() + "(" + getCode() + ")";
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getFatalLogLevel()
    {
        return getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.JOBLOGLEVEL, GeneratedCronJobConstants.Enumerations.JobLogLevel.FATAL);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getErrorLogLevel()
    {
        return getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.JOBLOGLEVEL, GeneratedCronJobConstants.Enumerations.JobLogLevel.ERROR);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getWarnLogLevel()
    {
        return getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.JOBLOGLEVEL, GeneratedCronJobConstants.Enumerations.JobLogLevel.WARNING);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getInfoLogLevel()
    {
        return getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.JOBLOGLEVEL, GeneratedCronJobConstants.Enumerations.JobLogLevel.INFO);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getDebugLogLevel()
    {
        return getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.JOBLOGLEVEL, GeneratedCronJobConstants.Enumerations.JobLogLevel.DEBUG);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getUnknownLogLevel()
    {
        return getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.JOBLOGLEVEL, GeneratedCronJobConstants.Enumerations.JobLogLevel.UNKNOWN);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getFinishedStatus()
    {
        return getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.CRONJOBSTATUS, GeneratedCronJobConstants.Enumerations.CronJobStatus.FINISHED);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getRunningStatus()
    {
        return getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.CRONJOBSTATUS, GeneratedCronJobConstants.Enumerations.CronJobStatus.RUNNING);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getRunningRestartStatus()
    {
        return getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.CRONJOBSTATUS, GeneratedCronJobConstants.Enumerations.CronJobStatus.RUNNINGRESTART);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getPausedStatus()
    {
        return getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.CRONJOBSTATUS, GeneratedCronJobConstants.Enumerations.CronJobStatus.PAUSED);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getAbortedStatus()
    {
        return getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.CRONJOBSTATUS, GeneratedCronJobConstants.Enumerations.CronJobStatus.ABORTED);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getUnknownStatus()
    {
        return getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.CRONJOBSTATUS, GeneratedCronJobConstants.Enumerations.CronJobStatus.UNKNOWN);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getErrorResult()
    {
        return getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.CRONJOBRESULT, GeneratedCronJobConstants.Enumerations.CronJobResult.ERROR);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getFailureResult()
    {
        return getSession().getEnumerationManager().getEnumerationValue(GeneratedCronJobConstants.TC.CRONJOBRESULT, GeneratedCronJobConstants.Enumerations.CronJobResult.FAILURE);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getSuccessResult()
    {
        return EnumerationManager.getInstance().getEnumerationValue(GeneratedCronJobConstants.TC.CRONJOBRESULT, GeneratedCronJobConstants.Enumerations.CronJobResult.SUCCESS);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getUnknownResult()
    {
        return EnumerationManager.getInstance().getEnumerationValue(GeneratedCronJobConstants.TC.CRONJOBRESULT, GeneratedCronJobConstants.Enumerations.CronJobResult.UNKNOWN);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void tryToStop(long maxWait) throws JaloInvalidParameterException
    {
        if(isRunning() || isRunningRestart())
        {
            if(!isAbortable())
            {
                throw new JaloInvalidParameterException("job " + getJob() + " is not abortable", 0);
            }
            setRequestAbort(true);
            while((maxWait >= 0L && isRunning()) || isRunningRestart())
            {
                try
                {
                    Thread.sleep(1000L);
                }
                catch(InterruptedException interruptedException)
                {
                }
                maxWait -= 1000L;
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isRunning()
    {
        EnumerationValue stat = getStatus();
        String code = (stat == null) ? null : stat.getCode();
        return GeneratedCronJobConstants.Enumerations.CronJobStatus.RUNNING.equalsIgnoreCase(code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isRunningRestart()
    {
        EnumerationValue stat = getStatus();
        String code = (stat == null) ? null : stat.getCode();
        return GeneratedCronJobConstants.Enumerations.CronJobStatus.RUNNINGRESTART.equalsIgnoreCase(code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isPaused()
    {
        EnumerationValue stat = getStatus();
        String code = (stat == null) ? null : stat.getCode();
        return GeneratedCronJobConstants.Enumerations.CronJobStatus.PAUSED.equalsIgnoreCase(code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isFinished()
    {
        EnumerationValue stat = getStatus();
        String code = (stat == null) ? null : stat.getCode();
        return GeneratedCronJobConstants.Enumerations.CronJobStatus.FINISHED.equalsIgnoreCase(code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void setRunning() throws IllegalStateException
    {
        if(isRunning())
        {
            throw new IllegalStateException("cannot set running - CronJob is already running");
        }
        if(!isPaused())
        {
            setStartTime(new Date());
            setEndTime(null);
        }
        setStatus(getRunningStatus());
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void setRunningRestart() throws IllegalStateException
    {
        if(isRunningRestart() || isRunning())
        {
            throw new IllegalStateException("cannot set running - CronJob is already running");
        }
        setStatus(getRunningRestartStatus());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setPaused() throws IllegalStateException
    {
        if(!isRunning() && !isPaused() && !isRunningRestart())
        {
            throw new IllegalStateException("cannot set paused - CronJob is not running");
        }
        setStatus(getPausedStatus());
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void setAborted()
    {
        if(!isRunning() && !isRunningRestart())
        {
            throw new IllegalStateException("cannot set aborted - CronJob is not running or restarted");
        }
        setStatus(getAbortedStatus());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CronJobResult getFinishedResult(boolean success)
    {
        return new CronJobResult(getFinishedStatus(), success ? getSuccessResult() : getFailureResult());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CronJobResult getUndoFinishedResult(boolean success)
    {
        return new CronJobResult(getUnknownStatus(), success ? getSuccessResult() : getFailureResult());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CronJobResult getAbortResult()
    {
        return new CronJobResult(getAbortedStatus(), getErrorResult());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CronJobResult getPausedResult()
    {
        return new CronJobResult(getPausedStatus(), getUnknownResult());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setCronJobResult(CronJobResult res)
    {
        if(res == null)
        {
            setResult(getUnknownResult());
            setStatus(getUnknownStatus());
            if(LOG.isDebugEnabled())
            {
                LOG.debug("setting CronJobResult (status: " + getUnknownStatus().getCode() + ", result: " +
                                getUnknownResult().getCode() + ")");
            }
        }
        else
        {
            setResult(res.getResult());
            setStatus(res.getStatus());
            if(LOG.isDebugEnabled())
            {
                LOG.debug("setting CronJobResult (status: " + res.getStatus().getCode() + ", result: " + res.getResult().getCode() + ")");
            }
        }
        if(isFinished())
        {
            setEndTime(new Date());
        }
    }


    protected JaloSession createSessionForCronJob(JaloSession jaloSession)
    {
        if(getSessionUser() == null || getSessionCurrency() == null || getSessionLanguage() == null)
        {
            throw new IllegalStateException("Session attribute for cronjob '" +
                            getCode() + "' were null! [user:" + getSessionUser() + ", currency:" +
                            getSessionCurrency() + ", language:" + getSessionLanguage());
        }
        Map<String, Object> extraValues = getSessionContextValues();
        if(MapUtils.isNotEmpty(extraValues))
        {
            Map<String, Object> filteredExtraValuesFromAttributesAddedDuringImport = filterSessionCtxAttributesAddedDuringImportCronJobByImpex(extraValues);
            jaloSession.getSessionContext()
                            .setAttributes(filterSessionContextValuesFromCronJob(filteredExtraValuesFromAttributesAddedDuringImport));
        }
        jaloSession.setUser(getSessionUser());
        jaloSession.getSessionContext().setLanguage(getSessionLanguage());
        jaloSession.getSessionContext().setCurrency(getSessionCurrency());
        return jaloSession;
    }


    protected Map<String, Object> filterSessionContextValuesFromCronJob(Map<String, Object> sessionContextValues)
    {
        if(MapUtils.isNotEmpty(sessionContextValues))
        {
            Set<String> toFilter = new HashSet<>(Arrays.asList(Config.getString("cronjob.ctx.filtered.attributes", "dont.change.existing.links")
                            .split("[,; ]+")));
            Map<Object, Object> copy = new LinkedHashMap<>(sessionContextValues.size() * 2);
            for(Map.Entry<String, Object> e : sessionContextValues.entrySet())
            {
                if(!toFilter.contains(e.getKey()))
                {
                    copy.put(e.getKey(), e.getValue());
                }
            }
            return (Map)copy;
        }
        return sessionContextValues;
    }


    protected void resetFinishedCronJob() throws IllegalStateException
    {
        if(!isFinished())
        {
            throw new IllegalStateException("cannot reset non-finished CronJob");
        }
        List<Step> steps = getProcessedSteps();
        setPendingSteps(steps);
        setProcessedSteps(Collections.EMPTY_LIST);
        setCurrentStep(null, false);
        setStatus(getUnknownStatus());
        setStartTime(null);
        setEndTime(null);
    }


    public void sendEmail(CronJobResult cronJobResult) throws EmailException
    {
        if(isSendEmailAsPrimitive())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Trying to send finished mail...");
            }
            try
            {
                String tomail = getEmailAddress();
                String msg = getFinishedEmailBody(cronJobResult);
                RendererTemplate template = getEmailNotificationTemplate();
                Email email = MailUtils.getPreConfiguredEmail();
                MailUtils.validateEmailAddress(tomail, "TO");
                email.addTo(tomail);
                if(getResult().getCode().equals(GeneratedCronJobConstants.Enumerations.CronJobResult.SUCCESS))
                {
                    subject = Config.getParameter(Config.Params.CRONJOB_MAIL_SUBJECT_SUCCESS);
                    MailUtils.validateParameter(subject, Config.Params.CRONJOB_MAIL_SUBJECT_SUCCESS);
                }
                else
                {
                    subject = Config.getParameter(Config.Params.CRONJOB_MAIL_SUBJECT_FAIL);
                    MailUtils.validateParameter(subject, Config.Params.CRONJOB_MAIL_SUBJECT_FAIL);
                }
                String subject = MessageFormat.format(subject, new Object[] {getCode(), getJob().getCode()});
                email.setSubject(subject);
                if(template == null)
                {
                    email.setMsg(msg);
                    if(LOG.isInfoEnabled())
                    {
                        LOG.info("No mail template found for cronJob " + getCode() + ".\n");
                    }
                }
                else if(JaloSession.getCurrentSession().getSessionContext().getLanguage() == null)
                {
                    String errormsg = "CronJob " + getCode() + " has no session language set!";
                    email.setMsg(msg + "\n" + msg);
                    LOG.error(errormsg);
                }
                else
                {
                    StringWriter mailMessage = new StringWriter();
                    CommonsManager.getInstance().render(template, getRendererNotificationContext(), mailMessage);
                    ((HtmlEmail)email).setHtmlMsg(mailMessage.toString());
                }
                email.send();
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Email successfully sent.");
                }
            }
            catch(EmailException e)
            {
                throw new EmailException("Make sure your mail properties (mail.*) are correctly set.", e);
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("email successfully sent.");
            }
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("Email sending is disabled.");
        }
    }


    protected CronJobNotificationTemplateContext getRendererNotificationContext()
    {
        return (CronJobNotificationTemplateContext)new DefaultCronJobNotificationTemplateContext(this);
    }


    protected String getFinishedEmailBody(CronJobResult cronJobResult)
    {
        String status = "n/a";
        String result = "n/a";
        if(cronJobResult != null)
        {
            SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
            if(ctx.getLanguage() == null)
            {
                status = cronJobResult.getStatus().getCode();
                result = cronJobResult.getResult().getCode();
            }
            else
            {
                status = (cronJobResult.getStatus().getName() == null) ? cronJobResult.getStatus().getCode() : cronJobResult.getStatus().getName();
                result = (cronJobResult.getResult().getName() == null) ? cronJobResult.getResult().getCode() : cronJobResult.getResult().getName();
            }
        }
        String endTime = (getEndTime() == null) ? "n/a" : getEndTime().toString();
        String startTime = (getStartTime() == null) ? "n/a" : getStartTime().toString();
        return "Finished CronJob " + getCode() + " (" + getJob()
                        .getCode() + ")\nStatus:\t" + status + "\nResult:\t" + result + "\nStart:\t" + startTime + "\nEnd:\t" + endTime;
    }


    public synchronized void addChangeListener(String topic, ChangeListener listener)
    {
        if(this.changeListeners == null)
        {
            this.changeListeners = new HashMap<>();
        }
        Set<ChangeListener> listeners = (Set)this.changeListeners.get(topic);
        if(listeners == null)
        {
            listeners = new HashSet();
            this.changeListeners.put(topic, listeners);
        }
        listeners.add(listener);
    }


    public synchronized void removeChangeListener(String topic, ChangeListener listener)
    {
        if(this.changeListeners != null)
        {
            Set listeners = (Set)this.changeListeners.get(topic);
            if(listeners != null)
            {
                listeners.remove(listener);
            }
        }
    }


    public synchronized void removeAllChangeListener()
    {
        if(this.changeListeners != null)
        {
            this.changeListeners = null;
        }
    }


    protected final void notifyChangeListeners(ChangeEvent event)
    {
        if(this.changeListeners != null)
        {
            Set listeners = (Set)this.changeListeners.get(event.getTopic());
            if(listeners != null)
            {
                for(Iterator<ChangeListener> it = listeners.iterator(); it.hasNext(); )
                {
                    ChangeListener listener = it.next();
                    listener.notify(event);
                }
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getTimeTable(SessionContext ctx)
    {
        Collection<Trigger> triggers = getTriggers();
        if(triggers == null || triggers.isEmpty())
        {
            return localize("cronjob.timetable.notscheduled");
        }
        if(triggers.size() == 1)
        {
            return ((Trigger)triggers.iterator().next()).getTimeTable(ctx);
        }
        return localize("cronjob.timetable.severalexectimes");
    }


    @Deprecated(since = "4.3", forRemoval = false)
    public void addToPendingSteps(SessionContext ctx, Step step)
    {
        super.addToPendingSteps(ctx, step);
    }


    @Deprecated(since = "4.3", forRemoval = false)
    public void addToPendingSteps(Step step)
    {
        super.addToPendingSteps(step);
    }


    @Deprecated(since = "4.3", forRemoval = false)
    public void addToProcessedSteps(SessionContext ctx, Step step)
    {
        super.addToProcessedSteps(ctx, step);
    }


    @Deprecated(since = "4.3", forRemoval = false)
    public void addToProcessedSteps(Step step)
    {
        super.addToProcessedSteps(step);
    }


    public boolean mustRunOnOtherNode()
    {
        return (getNodeID() != null && getNodeID().intValue() != MasterTenant.getInstance().getClusterID());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void createRunOnceTrigger(Date date)
    {
        CronJobManager.getInstance().createTrigger(this, date);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getTimeTable()
    {
        return getTimeTable(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isAbortable()
    {
        return getJob().isAbortable(this);
    }


    @ForceJALO(reason = "something else")
    public void setRequestAbort(SessionContext ctx, Boolean abortRequested)
    {
        boolean successful = false;
        while(!successful)
        {
            try
            {
                super.setRequestAbort(ctx, abortRequested);
                successful = true;
            }
            catch(HJMPException e)
            {
                if(e.getMessage() == null || e.getMessage().indexOf("modified concurrently") <= -1)
                {
                    throw e;
                }
            }
        }
    }


    private String localize(String key)
    {
        return Localization.getLocalizedString(key);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public LogFile createNewLogFile(String fileName)
    {
        Item.ItemAttributeMap params = new Item.ItemAttributeMap();
        params.put("code", fileName);
        params.put(Item.OWNER, this);
        try
        {
            return (LogFile)TypeManager.getInstance().getComposedType(LogFile.class).newInstance((Map)params);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    @ForceJALO(reason = "abstract method implementation")
    public Collection<LogFile> getLogFiles(SessionContext ctx)
    {
        return
                        FlexibleSearch.getInstance()
                                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" + GeneratedCronJobConstants.TC.LOGFILE + "}WHERE {" + Item.OWNER + "}=?me ORDER BY {" + Item.CREATION_TIME + "} ASC ",
                                                        Collections.singletonMap("me", this),
                                                        Collections.singletonList(LogFile.class), true, true, 0, -1).getResult();
    }


    @ForceJALO(reason = "abstract method implementation")
    public void setLogFiles(SessionContext ctx, Collection<LogFile> param)
    {
        Collection<LogFile> toRemove = new HashSet<>(getLogFiles(ctx));
        if(param != null)
        {
            toRemove.removeAll(param);
            for(LogFile lf : param)
            {
                try
                {
                    lf.setOwner((Item)this);
                }
                catch(ConsistencyCheckException e)
                {
                    throw new JaloSystemException(e);
                }
            }
        }
        for(LogFile lf : toRemove)
        {
            try
            {
                lf.remove(ctx);
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e);
            }
        }
    }


    @ForceJALO(reason = "something else")
    Map getSessionContextValues(SessionContext ctx)
    {
        try
        {
            return super.getSessionContextValues(ctx);
        }
        catch(de.hybris.platform.jalo.Item.JaloCachedComputationException e)
        {
            if(e.getCause() instanceof RuntimeException && e.getCause().getCause() instanceof java.io.InvalidClassException)
            {
                LOG.warn("Can not read sessionContextValues for CronJob " +
                                getCode() + ". Nevertheless the cronjob will run but without using session attributes applied at cronjob creation. Please re-create this cronjob to get rid of this warning. Cause is:" + e
                                .getMessage());
                return Collections.EMPTY_MAP;
            }
            throw e;
        }
    }


    @ForceJALO(reason = "something else")
    public void setNodeID(SessionContext ctx, Integer value)
    {
        super.setNodeID(ctx, value);
        List<Trigger> triggers = getTriggers();
        if(CollectionUtils.isNotEmpty(triggers))
        {
            for(Trigger trigger : triggers)
            {
                TriggerTask triggerTask = CronJobManager.getInstance().findTaskForTrigger(trigger);
                if(triggerTask != null)
                {
                    triggerTask.setNodeId(value);
                }
            }
        }
    }


    @ForceJALO(reason = "something else")
    public void setNodeGroup(SessionContext ctx, String value)
    {
        super.setNodeGroup(ctx, value);
        List<Trigger> triggers = getTriggers();
        if(CollectionUtils.isNotEmpty(triggers))
        {
            for(Trigger trigger : triggers)
            {
                TriggerTask triggerTask = CronJobManager.getInstance().findTaskForTrigger(trigger);
                if(triggerTask != null)
                {
                    triggerTask.setNodeGroup(value);
                }
            }
        }
    }


    public static CronJobThreadSettings getCronJobThreadSettings()
    {
        return new CronJobThreadSettings();
    }


    public static void activateCronJobThreadSettings(CronJobThreadSettings settings)
    {
        if(settings == null)
        {
            throw new IllegalArgumentException("settings are null");
        }
        CronJob current4Thread = getCurrentlyExecutingCronJobFailSave();
        Job.JobFileLogContainer currentLog4Thread = Job.jobIsCurrentlyRunning() ? Job.getCurrentLogContainer() : null;
        CronJob cronjob = settings.getCurrent();
        if(cronjob != null && !cronjob.equals(current4Thread))
        {
            setCurrentlyExecutingCronJob(cronjob);
        }
        Job.JobFileLogContainer logCont = settings.getLogContainer();
        if(logCont != null && !logCont.equals(currentLog4Thread))
        {
            Job.setCurrentLogContainer(logCont);
        }
        if(cronjob != null && CronJobLogListener.getCurrentContext() == null)
        {
            CronJobLogListener.setCurrentContext(cronjob.getJob().createLogContext(cronjob));
        }
    }


    public static void unsetCronJobThreadSettings(CronJobThreadSettings settings)
    {
        if(settings == null)
        {
            throw new IllegalArgumentException("settings are null");
        }
        CronJob current4Thread = getCurrentlyExecutingCronJobFailSave();
        Job.JobFileLogContainer currentLog4Thread = Job.jobIsCurrentlyRunning() ? Job.getCurrentLogContainer() : null;
        CronJob cronjob = settings.getCurrent();
        if(cronjob != null && cronjob.equals(current4Thread))
        {
            unsetCurrentlyExecutingCronJob();
        }
        Job.JobFileLogContainer logCont = settings.getLogContainer();
        if(logCont != null && logCont.equals(currentLog4Thread))
        {
            Job.unsetCurrentLogContainer();
        }
    }


    static void setLog4JMDC(CronJob cronJob)
    {
        if(cronJob == null)
        {
            LoggingContextFactory.getLoggingContextHandler().put("CronJob", "");
        }
        else
        {
            LoggingContextFactory.getLoggingContextHandler().put("CronJob", "(" + cronJob.getCode() + ") ");
        }
    }


    void createCronJobHistory()
    {
        try
        {
            CronJob cronJob = this;
            Transaction.current().execute((TransactionBody)new Object(this, cronJob));
        }
        catch(Exception e)
        {
            throw new SystemException(e);
        }
    }


    protected CronJobHistory instantiateCronJobHistory() throws JaloGenericCreationException, JaloAbstractTypeException
    {
        ComposedType composedType = TypeManager.getInstance().getComposedType(CronJobHistory.class);
        ImmutableMap immutableMap = ImmutableMap.of("cronJobCode", getCode(), "jobCode", getJob().getCode(), "startTime", new Date(), "status",
                        getStatus());
        return (CronJobHistory)composedType.newInstance((Map)immutableMap);
    }


    protected void finalizeCronJobHistory()
    {
        CronJobHistory history = getActiveCronJobHistory();
        if(Objects.isNull(history))
        {
            return;
        }
        try
        {
            Transaction.current().execute((TransactionBody)new Object(this, history));
        }
        catch(Exception e)
        {
            logException(e);
            throw new SystemException(e);
        }
    }


    private void logException(Exception ex)
    {
        String msg = ex.getMessage();
        LOG.error(msg);
        LOG.debug(msg, ex);
    }
}
