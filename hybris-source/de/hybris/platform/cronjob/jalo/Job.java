package de.hybris.platform.cronjob.jalo;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.ContextQueryFilter;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.processing.constants.GeneratedProcessingConstants;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.AfterCronJobFinishedEvent;
import de.hybris.platform.servicelayer.event.events.BeforeCronJobStartEvent;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerManager;
import de.hybris.platform.util.StrandedItemsRegistry;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.threadpool.PoolableThread;
import de.hybris.platform.util.threadpool.ThreadPool;
import java.io.DataInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public abstract class Job extends GeneratedJob
{
    public static final String CTX_CURRENTJOB = "currentJob";
    public static final String CTX_CURRENTCRONJOB = "currentCronJob";
    private static final Logger LOGGER = Logger.getLogger(Job.class.getName());
    private static final String ABOUTTOSTART = "abouttostart";
    private static final ThreadLocal<Stack<JobFileLogContainer>> _CURRENTLOGCONTAINER = new ThreadLocal<>();


    private static void set(JobFileLogContainer cronjob)
    {
        Preconditions.checkArgument((cronjob != null));
        Stack<JobFileLogContainer> stack = _CURRENTLOGCONTAINER.get();
        if(stack == null)
        {
            _CURRENTLOGCONTAINER.set(stack = new Stack<>());
        }
        if(stack.isEmpty() || !((JobFileLogContainer)stack.peek()).equals(cronjob))
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
        Stack<JobFileLogContainer> stack = _CURRENTLOGCONTAINER.get();
        if(stack != null && !stack.isEmpty())
        {
            stack.pop();
        }
    }


    private static JobFileLogContainer get()
    {
        Stack<JobFileLogContainer> stack = _CURRENTLOGCONTAINER.get();
        JobFileLogContainer cronjob = (stack != null && !stack.isEmpty()) ? stack.peek() : null;
        return cronjob;
    }


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!checkMandatoryAttribute("code", allAttributes, missing))
        {
            throw new JaloInvalidParameterException("missing " + missing + " for creating a new Job", 0);
        }
        if(!checkUniqueAttribute(type, "code", allAttributes.get("code")))
        {
            throw new JaloInvalidParameterException("code has to be unique", 0);
        }
        Job job = (Job)super.createItem(ctx, type, allAttributes);
        job.setCode(ctx, (String)allAttributes.get("code"));
        return (Item)job;
    }


    @ForceJALO(reason = "something else")
    public void setNonInitialAttributes(SessionContext ctx, Item item, Item.ItemAttributeMap nonInitialAttributes) throws JaloBusinessException
    {
        Item.ItemAttributeMap myMap = new Item.ItemAttributeMap((Map)nonInitialAttributes);
        super.setNonInitialAttributes(ctx, item, myMap);
    }


    @ForceJALO(reason = "something else")
    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap map = super.getNonInitialAttributes(ctx, allAttributes);
        map.remove("code");
        return map;
    }


    @ForceJALO(reason = "something else")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        checkRemovable(ctx);
        SessionContext myCtx = new SessionContext(ctx);
        myCtx.removeAttribute("is.hmc.session");
        for(CronJob cj : getCronJobs(myCtx))
        {
            cj.remove(myCtx);
        }
        super.remove(ctx);
    }


    @ForceJALO(reason = "something else")
    protected void checkRemovable(SessionContext ctx) throws ConsistencyCheckException
    {
        if(LOGGER.isInfoEnabled())
        {
            LOGGER.info("Check removable job " + getCode() + " ...");
        }
        for(Iterator<CronJob> it = getCronJobs().iterator(); it.hasNext(); )
        {
            CronJob cronJob = it.next();
            if(cronJob.isRunning() || cronJob.isRunningRestart())
            {
                throw new ConsistencyCheckException("Could not remove cronjob " + cronJob
                                .getCode() + " since it is still running", 0);
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe
    public void setCronJobs(SessionContext ctx, Collection<CronJob> cronJobs)
    {
        Collection<CronJob> toRemove = new ArrayList<>(getCronJobs(ctx));
        if(cronJobs != null)
        {
            toRemove.removeAll(cronJobs);
            for(Iterator<CronJob> iter = cronJobs.iterator(); iter.hasNext(); )
            {
                CronJob cronjob = iter.next();
                cronjob.setJob(ctx, this);
            }
        }
        for(Iterator<CronJob> it = toRemove.iterator(); it.hasNext(); )
        {
            try
            {
                ((CronJob)it.next()).remove(ctx);
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e);
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<CronJob> getCronJobsByCode(String code)
    {
        return getCronJobsByCode(getSession().getSessionContext(), code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<CronJob> getCronJobsByCode(SessionContext ctx, String code)
    {
        HashMap<Object, Object> attributes = new HashMap<>();
        attributes.put("me", this);
        attributes.put("code", code);
        return getSession()
                        .getFlexibleSearch()
                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" +
                                                        getSession().getTypeManager().getComposedType(CronJob.class).getCode() + "} WHERE {job} = ?me AND {code} = ?code ORDER BY {" + Item.CREATION_TIME + "} ASC", attributes,
                                        Collections.singletonList(CronJob.class), true, true, 0, -1)
                        .getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected boolean canUndo(CronJob cronJob)
    {
        return false;
    }


    private boolean canUndoInternal(CronJob cronJob)
    {
        if(!cronJob.isChangeRecordingEnabledAsPrimitive())
        {
            return false;
        }
        EnumerationValue stat = cronJob.getStatus();
        if(stat == null)
        {
            return false;
        }
        boolean canUndo = (cronJob.isActiveAsPrimitive() && (cronJob.getPausedStatus().equals(stat) || cronJob.getAbortedStatus().equals(stat) || cronJob.getFinishedStatus().equals(stat)));
        if(!canUndo)
        {
            StringBuilder buffer = new StringBuilder();
            buffer.append("cronJob.isActive: ").append(cronJob.isActiveAsPrimitive()).append(" - ");
            buffer.append("cronJob.getStatus: ").append(stat.getCode()).append(" - ");
            buffer.append("cronJob.getNodeID: ").append(cronJob.getNodeID()).append(" - ");
            buffer.append("Config.getClusterID: ").append(MasterTenant.getInstance().getClusterID());
            LOGGER.error(buffer.toString());
        }
        return canUndo;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isAbortable(CronJob conJob)
    {
        return false;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isUndoable(CronJob cronJob)
    {
        return (isUndoableInternal(cronJob) && cronJob.getTransientObject("abouttostart") == null);
    }


    private boolean isUndoableInternal(CronJob cronJob)
    {
        return (canUndo(cronJob) && canUndoInternal(cronJob));
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected boolean canPerform(CronJob cronJob)
    {
        return true;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isPerformable(CronJob cronJob)
    {
        return isPerformable(cronJob, MasterTenant.getInstance().getClusterID());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isPerformable(CronJob cronJob, int clusternode)
    {
        return (isPerfomableInternal(cronJob, clusternode) && cronJob.getTransientObject("abouttostart") == null);
    }


    private final boolean isPerfomableInternal(CronJob cronJob, int clusterNode)
    {
        return (canPerform(cronJob) && canPerformInternal(cronJob, clusterNode));
    }


    private final boolean canPerformInternal(CronJob cronJob, int clusterNode)
    {
        EnumerationValue stat = cronJob.getStatus();
        if(stat == null)
        {
            return false;
        }
        if(cronJob.isActiveAsPrimitive())
        {
            if(cronJob.getUnknownStatus().equals(stat) || cronJob.getPausedStatus().equals(stat) || cronJob
                            .getAbortedStatus().equals(stat))
            {
                return true;
            }
            if(!cronJob.isSingleExecutableAsPrimitive())
            {
                if(cronJob.getFinishedStatus().equals(stat))
                {
                    return true;
                }
            }
        }
        return false;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public final void perform(CronJob cronJob)
    {
        perform(cronJob, false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public final void perform(CronJob cronJob, boolean synchronous)
    {
        performImpl(cronJob, synchronous, false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public final void perform(CronJob cronJob, Synchronicity synchronousType)
    {
        performImpl(cronJob, (synchronousType == Synchronicity.SYNCHRONOUS), false);
    }


    private final void performImpl(CronJob cronJob, boolean synchronous, boolean undo)
    {
        cronJob.setTransientObject("abouttostart", "true");
        String logFileName = cronJob.getPK().toString() + ".log";
        Level logLevelFile = cronJob.convertEnumToLogLevel(cronJob.getLogLevelFile());
        if(synchronous)
        {
            try
            {
                performImpl(cronJob, logLevelFile, logFileName, undo,
                                prepareBeforeStartEvent(cronJob, true), prepareAfterFinishEvent(cronJob, true));
            }
            finally
            {
                cronJob.setTransientObject("abouttostart", null);
            }
        }
        else
        {
            PoolableThread poolableThread = getPoolableThread(cronJob);
            if(poolableThread != null)
            {
                if(cronJob.getPriority() != null)
                {
                    poolableThread.setPriority(cronJob.getPriority().intValue());
                }
                else
                {
                    poolableThread.setPriority(4);
                }
                poolableThread.execute((Runnable)new JobRunable(this, cronJob.getCode() + "::" + cronJob.getCode(), cronJob, logLevelFile, logFileName, undo,
                                prepareBeforeStartEvent(cronJob, false),
                                prepareAfterFinishEvent(cronJob, false)));
            }
            else
            {
                cronJob.setTransientObject("abouttostart", null);
                cronJob.addLog("cannot start cronjob " + cronJob.getCode() + " since no thread is available", cronJob
                                .getErrorLogLevel());
            }
        }
    }


    private final void performImpl(CronJob cronJob, Level logLevelFile, String logFileTempName, boolean undo, BeforeCronJobStartEvent startEventToSend, AfterCronJobFinishedEvent endEventToSend)
    {
        try
        {
            RevertibleUpdate revertibleUpdate = OperationInfo.updateThread(OperationInfo.builder().withCronJobCode(cronJob.getCode()).build());
            try
            {
                JaloSession previous;
                CronJob.setLog4JMDC(cronJob);
                try
                {
                    previous = startOwnSession(cronJob);
                }
                catch(ConsistencyCheckException e)
                {
                    if(Utilities.isSystemInitialized(getTenant().getDataSource()))
                    {
                        LOGGER.error("Could not start own session due to " + e
                                        .getMessage() + "\n" + Utilities.getStackTraceAsString((Throwable)e));
                    }
                    else
                    {
                        LOGGER.warn("Could not start own session because system is not initialized.");
                    }
                    if(revertibleUpdate != null)
                    {
                        revertibleUpdate.close();
                    }
                    return;
                }
                boolean logToFile = cronJob.isLogToFileAsPrimitive();
                try
                {
                    if(logToFile)
                    {
                        setCurrentLogContainer(new JobFileLogContainer(logLevelFile, logFileTempName));
                    }
                    CronJob.setCurrentlyExecutingCronJob(cronJob);
                    CronJobLogListener.setCurrentContext(createLogContext(cronJob));
                    switchDataSource(cronJob);
                    if(undo)
                    {
                        executeUndo(cronJob);
                    }
                    else
                    {
                        execute(cronJob, startEventToSend, endEventToSend);
                    }
                }
                catch(Throwable e)
                {
                    CronJobManager.getInstance().getStrandedCronJobsRegistry().markOnFailure((Item)cronJob, cj -> {
                        LOGGER.error("Caught throwable " + e.getMessage() + "\n" + Utilities.getStackTraceAsString(e));
                        CronJob.CronJobResult res = cj.getAbortResult();
                        cj.setCronJobResult(res);
                        if(endEventToSend.getResult() == null)
                        {
                            sendFinishedEvent(endEventToSend, res);
                        }
                    }cj1 -> createStrandedItemContext(Boolean.valueOf(logToFile)));
                }
                finally
                {
                    cronJob.finalizeCronJobHistory();
                    unsetAlternativeDataSource();
                    CronJobLogListener.unsetsetCurrentContext();
                    if(logToFile && jobIsCurrentlyRunning())
                    {
                        JobFileLogContainer jobFileLogContainer = getCurrentLogContainer();
                        unsetCurrentLogContainer();
                        saveToLogFile(jobFileLogContainer, CronJob.getCurrentlyExecutingCronJob());
                        jobFileLogContainer.cleanup();
                    }
                    CronJob.unsetCurrentlyExecutingCronJob();
                    if(cronJob.isFinished() && cronJob.getResult().equals(cronJob.getSuccessResult()) && cronJob.isRemoveOnExitAsPrimitive())
                    {
                        try
                        {
                            if(LOGGER.isInfoEnabled())
                            {
                                LOGGER.info("Removing cronjob: " + cronJob.getCode());
                            }
                            cronJob.remove();
                        }
                        catch(ConsistencyCheckException e)
                        {
                            LOGGER.error(e.getMessage(), (Throwable)e);
                        }
                    }
                    stopOwnSession(previous, cronJob);
                }
                if(revertibleUpdate != null)
                {
                    revertibleUpdate.close();
                }
            }
            catch(Throwable previous)
            {
                if(revertibleUpdate != null)
                {
                    try
                    {
                        revertibleUpdate.close();
                    }
                    catch(Throwable throwable)
                    {
                        previous.addSuppressed(throwable);
                    }
                }
                throw previous;
            }
        }
        finally
        {
            CronJob.setLog4JMDC(null);
        }
    }


    private StrandedItemsRegistry.StrandedItemContext createStrandedItemContext(Boolean logToFile)
    {
        CronJobManager.StrandedCronJobContext.Builder contextBuilder = CronJobManager.StrandedCronJobContext.strandedCronJobContextBuilder();
        if(logToFile.booleanValue())
        {
            contextBuilder = contextBuilder.withJobFileLogContainer(get());
        }
        return (StrandedItemsRegistry.StrandedItemContext)contextBuilder.build();
    }


    protected BeforeCronJobStartEvent prepareBeforeStartEvent(CronJob cj, boolean synchronous)
    {
        Job job = cj.getJob();
        BeforeCronJobStartEvent evt = new BeforeCronJobStartEvent();
        SessionContext ctx = getSession().getSessionContext();
        evt.setScheduled(Boolean.TRUE.equals(ctx.getAttribute("cronjob.scheduled")));
        evt.setScheduledByTriggerPk((PK)ctx.getAttribute("cronjob.scheduled.by"));
        evt.setSynchronous(synchronous);
        evt.setCronJobPK(cj.getPK());
        evt.setCronJob(cj.getCode());
        evt.setCronJobType(cj.getComposedType().getCode());
        evt.setJob(job.getCode());
        evt.setJobType(job.getComposedType().getCode());
        return evt;
    }


    protected void sendStartEvent(BeforeCronJobStartEvent event)
    {
        ServicelayerManager.getInstance().publishEvent((AbstractEvent)event);
    }


    protected AfterCronJobFinishedEvent prepareAfterFinishEvent(CronJob cj, boolean synchronous)
    {
        Job job = cj.getJob();
        AfterCronJobFinishedEvent evt = new AfterCronJobFinishedEvent();
        SessionContext ctx = getSession().getSessionContext();
        evt.setScheduled(Boolean.TRUE.equals(ctx.getAttribute("cronjob.scheduled")));
        evt.setScheduledByTriggerPk((PK)ctx.getAttribute("cronjob.scheduled.by"));
        evt.setSynchronous(synchronous);
        evt.setCronJobPK(cj.getPK());
        evt.setCronJob(cj.getCode());
        evt.setCronJobType(cj.getComposedType().getCode());
        evt.setJob(job.getCode());
        evt.setJobType(job.getComposedType().getCode());
        return evt;
    }


    protected void sendFinishedEvent(AfterCronJobFinishedEvent event, CronJob.CronJobResult result)
    {
        ServicelayerManager slm = ServicelayerManager.getInstance();
        event.setResult((CronJobResult)slm.getServiceLayerEnumerationValue(GeneratedProcessingConstants.TC.CRONJOBRESULT, result.getResult().getCode()));
        event.setStatus((CronJobStatus)slm.getServiceLayerEnumerationValue(GeneratedProcessingConstants.TC.CRONJOBSTATUS, result.getStatus().getCode()));
        ServicelayerManager.getInstance().publishEvent((AbstractEvent)event);
    }


    protected void switchDataSource(CronJob cronjob)
    {
        String alternativeDataSourceID = cronjob.getAlternativeDataSourceID();
        if(StringUtils.isNotBlank(alternativeDataSourceID))
        {
            Tenant tenant = Registry.getCurrentTenantNoFallback();
            if(tenant.getAllAlternativeMasterDataSourceIDs().contains(alternativeDataSourceID))
            {
                tenant.activateAlternativeMasterDataSource(alternativeDataSourceID);
            }
            else if(tenant.getAllSlaveDataSourceIDs().contains(alternativeDataSourceID))
            {
                tenant.activateSlaveDataSource(alternativeDataSourceID);
            }
            else
            {
                LOGGER.warn("unknown alternative data source '" + alternativeDataSourceID + "' specified for cronjob " + cronjob + " - falling back to main master");
            }
        }
    }


    protected void unsetAlternativeDataSource()
    {
        Tenant tenant = Registry.getCurrentTenantNoFallback();
        if(tenant != null)
        {
            Registry.getCurrentTenantNoFallback().deactivateAlternativeDataSource();
        }
    }


    protected static final void setCurrentLogContainer(JobFileLogContainer logContainer)
    {
        if(logContainer == null)
        {
            unset();
        }
        else
        {
            set(logContainer);
        }
    }


    protected static final void unsetCurrentLogContainer()
    {
        unset();
    }


    protected static final JobFileLogContainer getCurrentLogContainer()
    {
        JobFileLogContainer logContainer = get();
        if(logContainer == null)
        {
            throw new IllegalStateException("current log container is not set for " + Thread.currentThread());
        }
        return logContainer;
    }


    protected static final boolean jobIsCurrentlyRunning()
    {
        return (get() != null);
    }


    protected static final void saveToLogFile(JobFileLogContainer container, CronJob cronJob)
    {
        try
        {
            DataInputStream dis = (container != null) ? container.getLogData() : null;
            if(dis != null)
            {
                String filename = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date()) + "-" + (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date()) + ".zip";
                LogFile logFile = cronJob.createNewLogFile(filename);
                logFile.setData(dis, filename, "application/zip");
            }
            else if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("No log file available for cronjob " + cronJob.getCode());
            }
        }
        catch(Exception e)
        {
            if(LOGGER.isEnabledFor((Priority)Level.ERROR))
            {
                LOGGER.error("Error saving log file " + e.getLocalizedMessage() + " : " + Utilities.getStackTraceAsString(e));
            }
        }
    }


    protected void logToFile(String message, Level level)
    {
        if(jobIsCurrentlyRunning())
        {
            getCurrentLogContainer().log(message, level);
        }
    }


    public void log(String message, Level level)
    {
        if(jobIsCurrentlyRunning())
        {
            CronJob cronjob = CronJob.getCurrentlyExecutingCronJob();
            if(cronjob.isLogToFileAsPrimitive())
            {
                getCurrentLogContainer().log(message, level);
            }
            if(cronjob.isLogToDatabaseAsPrimitive())
            {
                cronjob.addLog(message, BatchJob.hasCurrentlyExecutingStep() ? BatchJob.getCurrentlyExecutingStep() : null, cronjob
                                .convertLogLevelToEnum(level), false);
            }
        }
        else if(CronJob.hasCurrentlyExecutingCronJob())
        {
            CronJob cronjob = CronJob.getCurrentlyExecutingCronJob();
            if(cronjob.isLogToDatabaseAsPrimitive())
            {
                cronjob.addLog(message, BatchJob.hasCurrentlyExecutingStep() ? BatchJob.getCurrentlyExecutingStep() : null, cronjob
                                .convertLogLevelToEnum(level), false);
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void debug(String message)
    {
        LOGGER.debug(message);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void info(String message)
    {
        LOGGER.info(message);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void warn(String message)
    {
        LOGGER.warn(message);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void error(String message)
    {
        LOGGER.error(message);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void fatal(String message)
    {
        LOGGER.fatal(message);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isEnabledFor(Level level)
    {
        if(jobIsCurrentlyRunning())
        {
            CronJob cronjob = CronJob.getCurrentlyExecutingCronJob();
            Level fileLevel = cronjob.isLogToFileAsPrimitive() ? getCurrentLogContainer().getLevel() : null;
            Level dbLevel = cronjob.isLogToDatabaseAsPrimitive() ? cronjob.convertEnumToLogLevel(cronjob.getLogLevelDatabase()) : null;
            return ((fileLevel != null && level.isGreaterOrEqual((Priority)fileLevel)) || (dbLevel != null && level.isGreaterOrEqual((Priority)dbLevel)));
        }
        return LOGGER.isEnabledFor((Priority)level);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isDebugEnabled()
    {
        return LOGGER.isEnabledFor((Priority)Level.DEBUG);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isInfoEnabled()
    {
        return LOGGER.isEnabledFor((Priority)Level.INFO);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isWarnEnabled()
    {
        return LOGGER.isEnabledFor((Priority)Level.WARN);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isErrorEnabled()
    {
        return LOGGER.isEnabledFor((Priority)Level.ERROR);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public final void undo(CronJob cronJob)
    {
        undo(cronJob, false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public final void undo(CronJob cronJob, boolean synchronous)
    {
        performImpl(cronJob, synchronous, true);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected CronJob.CronJobResult undoCronJob(CronJob cronJob)
    {
        return null;
    }


    protected JaloSession startOwnSession(CronJob cronJob) throws ConsistencyCheckException
    {
        Collection<ContextQueryFilter> filters = new ArrayList<>();
        JaloSession previousSession = JaloSession.hasCurrentSession() ? JaloSession.getCurrentSession() : null;
        JaloSession own = null;
        try
        {
            own = cronJob.createSessionForCronJob(JaloConnection.getInstance().createAnonymousCustomerSession());
        }
        catch(Exception e)
        {
            throw new ConsistencyCheckException(e, 0);
        }
        own.activate();
        own.setAttribute("currentCronJob", cronJob);
        own.setAttribute("currentJob", cronJob.getJob());
        for(JobSearchRestriction config : getRestrictions())
        {
            filters.add(new ContextQueryFilter(config.getCode(), config.getType(), config.getQuery()));
        }
        FlexibleSearch.getInstance().addContextQueryFilters(own.getSessionContext(), filters);
        return previousSession;
    }


    protected void stopOwnSession(JaloSession previousSession, CronJob cronJob)
    {
        try
        {
            FlexibleSearch.getInstance().clearContextQueryFilters();
            cronJob.setTransientObject("abouttostart", null);
            JaloSession own = JaloSession.getCurrentSession();
            if(own != null)
            {
                own.close();
            }
            if(previousSession != null)
            {
                previousSession.activate();
            }
            else
            {
                JaloSession.deactivate();
            }
        }
        catch(Exception e)
        {
            if(LOGGER.isEnabledFor((Priority)Level.ERROR))
            {
                LOGGER.error("Error stoping own jalosession " + e
                                .getLocalizedMessage() + " : " + Utilities.getStackTraceAsString(e));
            }
        }
    }


    private final void executeUndo(CronJob cronJob)
    {
        if(isUndoableInternal(cronJob))
        {
            if(LOGGER.isInfoEnabled())
            {
                LOGGER.info("Starting undo of CronJob " + cronJob.getCode());
            }
            long start = System.currentTimeMillis();
            cronJob.setRunning();
            cronJob.setTransientObject("abouttostart", null);
            cronJob.setCronJobResult(undoCronJob(cronJob));
            long end = System.currentTimeMillis();
            if(LOGGER.isInfoEnabled())
            {
                LOGGER.info("Finished undo of cronjob " + cronJob + " (duration: " + Utilities.formatTime(end - start) + ")");
            }
        }
        else
        {
            LOGGER.error("Cannot undo cronjob " + cronJob);
        }
    }


    private void execute(CronJob cronJob, BeforeCronJobStartEvent startEventToSend, AfterCronJobFinishedEvent endEventToSend)
    {
        long start = System.currentTimeMillis();
        boolean logToFile = cronJob.isLogToFile().booleanValue();
        try
        {
            if(!isPerfomableInternal(cronJob, MasterTenant.getInstance().getClusterID()))
            {
                LOGGER.error("Cronjob " + cronJob + " cannot be performed yet");
                cronJob.createCronJobHistory();
            }
            else
            {
                if(LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("Starting CronJob " + cronJob.getCode() + " ( job:" + cronJob.getJob().getCode() + " )");
                }
                sendStartEvent(startEventToSend);
                cronJob.setRunningOnClusterNode(MasterTenant.getInstance().getClusterID());
                EnumerationValue oldStatus = cronJob.getStatus();
                if(cronJob.isFinished())
                {
                    cronJob.resetFinishedCronJob();
                }
                if(cronJob.getAbortedStatus().equals(oldStatus))
                {
                    cronJob.setRunningRestart();
                    cronJob.setStartTime(new Date());
                    cronJob.setEndTime(null);
                }
                else
                {
                    cronJob.setRunning();
                }
                cronJob.setTransientObject("abouttostart", null);
                cronJob.setRequestAbort(null);
                cronJob.setRequestAbortStep(null);
                cronJob.createCronJobHistory();
                CronJob.CronJobResult result = performCronJobWithReadOnlySetting(cronJob);
                cronJob.setCurrentRetry(0);
                cronJob.setCronJobResult(result);
                sendFinishedEvent(endEventToSend, result);
                if(cronJob.getFinishedStatus().equals(result.getStatus()) || cronJob.getAbortedStatus().equals(result.getStatus()))
                {
                    try
                    {
                        cronJob.sendEmail(result);
                    }
                    catch(EmailException e)
                    {
                        LOGGER.warn("Could not send email. " + e.getMessage(), (Throwable)e);
                    }
                }
                long end = System.currentTimeMillis();
                if(LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("Finished cronjob " + cronJob.getCode() + " - job:" + cronJob.getJob().getCode() + " (duration: " +
                                    Utilities.formatTime(end - start) + ")");
                }
            }
        }
        catch(AbortCronJobException e)
        {
            LOGGER.warn((e.getMessage() != null) ? e.getMessage() : "Cronjob aborted due to request");
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("Aborted at " + Utilities.getStackTraceAsString((Throwable)e));
            }
            CronJob.CronJobResult res = cronJob.getAbortResult();
            cronJob.setCronJobResult(res);
            sendFinishedEvent(endEventToSend, res);
            long end = System.currentTimeMillis();
            if(LOGGER.isInfoEnabled())
            {
                LOGGER.info("Finished cronjob " + cronJob.getCode() + " - job:" + cronJob.getJob().getCode() + " (duration: " +
                                Utilities.formatTime(end - start) + ")");
            }
        }
        finally
        {
            CronJobManager.getInstance().getStrandedCronJobsRegistry()
                            .markOnFailure((Item)cronJob, cj -> {
                                cj.setTransientObject("abouttostart", null);
                                cj.setRunningOnClusterNode(null);
                            }cj1 -> createStrandedItemContext(Boolean.valueOf(logToFile)));
        }
    }


    private CronJob.CronJobResult performCronJobWithReadOnlySetting(CronJob cronJob) throws AbortCronJobException
    {
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        Optional<Boolean> readOnlyEnabled = FlexibleSearch.getInstance().isReadOnlyDataSourceEnabled(ctx);
        try
        {
            Optional.<Boolean>ofNullable(cronJob.isUseReadOnlyDatasource())
                            .ifPresent(useReadOnly -> ctx.setAttribute("ctx.enable.fs.on.read-replica", useReadOnly));
            return performCronJob(cronJob);
        }
        finally
        {
            readOnlyEnabled.ifPresentOrElse(v -> ctx.setAttribute("ctx.enable.fs.on.read-replica", v), () -> ctx.removeAttribute("ctx.enable.fs.on.read-replica"));
        }
    }


    private PoolableThread getPoolableThread(CronJob cronJob)
    {
        PoolableThread poolableThread = null;
        try
        {
            poolableThread = ThreadPool.getInstance().borrowThread();
        }
        catch(Exception e)
        {
            cronJob
                            .addLog("Cannot get worker thread : " + e + "\n" + Utilities.getStackTraceAsString(e), cronJob
                                            .getErrorLogLevel());
            cronJob.setCronJobResult(cronJob.getAbortResult());
        }
        return poolableThread;
    }


    @ForceJALO(reason = "consistency check")
    public void setCode(SessionContext ctx, String code)
    {
        if(getCode() == null || !getCode().equals(code))
        {
            if(!checkUniqueAttribute(TypeManager.getInstance().getComposedType(Job.class), "code", code))
            {
                throw new JaloInvalidParameterException("code has to be unique", 0);
            }
        }
        super.setCode(ctx, code);
    }


    private static boolean checkUniqueAttribute(ComposedType type, String qualifier, Object value)
    {
        Collection result = Collections.EMPTY_LIST;
        try
        {
            Map<Object, Object> values = new HashMap<>();
            values.put("value", value);
            result = FlexibleSearch.getInstance().search("SELECT {" + Item.PK + "} FROM {" + type.getCode() + "*} WHERE {" + qualifier + "} = ?value", values, Collections.singletonList(Item.class), true, true, 0, -1).getResult();
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1006);
        }
        return result.isEmpty();
    }


    @Deprecated(since = "ages", forRemoval = false)
    CronJob createCronjob() throws JaloInvalidParameterException, JaloBusinessException
    {
        if(this instanceof TriggerableJob)
        {
            CronJob cronjob = ((TriggerableJob)this).newExecution();
            configureCronjob(cronjob);
            if(cronjob.isRetryAsPrimitive() && !canPerform(cronjob))
            {
                Calendar cal = Utilities.getDefaultCalendar();
                cal.setTime(new Date());
                HashMap<Object, Object> map = new HashMap<>();
                map.put("cronJob", cronjob);
                map.put("second", Integer.valueOf(cal.get(13)));
                map.put("minute", Integer.valueOf(cal.get(12)));
                map.put("hour", Integer.valueOf(cal.get(11)));
                map.put("day", Integer.valueOf(cal.get(5)));
                map.put("month", Integer.valueOf(cal.get(2)));
                map.put("year", Integer.valueOf(cal.get(1)));
                map.put("relative", Boolean.FALSE);
                map.put("daysOfWeek", null);
                CronJobManager.getInstance().createTrigger(map);
            }
            return cronjob;
        }
        throw new UnsupportedOperationException();
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void configureCronjob(CronJob cronjob) throws JaloInvalidParameterException, JaloBusinessException
    {
        Map<String, String> values = getConfigAttributes(cronjob);
        for(String jobMapping : values.keySet())
        {
            Object jobValueAttribute = getAttribute(JaloSession.getCurrentSession().getSessionContext(), jobMapping);
            if(jobValueAttribute != null)
            {
                cronjob.setAttribute(values.get(jobMapping), jobValueAttribute);
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected Map<String, String> getConfigAttributes(CronJob cronjob)
    {
        Map<String, String> mappingsJob2CronJob = new HashMap<>();
        mappingsJob2CronJob.put("active", "active");
        mappingsJob2CronJob.put("errorMode", "errorMode");
        mappingsJob2CronJob.put("logToFile", "logToFile");
        mappingsJob2CronJob.put("logLevelFile", "logLevelFile");
        mappingsJob2CronJob.put("logToDatabase", "logToDatabase");
        mappingsJob2CronJob.put("logLevelDatabase", "logLevelDatabase");
        mappingsJob2CronJob.put("sessionUser", "sessionUser");
        mappingsJob2CronJob.put("sessionLanguage", "sessionLanguage");
        mappingsJob2CronJob.put("sessionCurrency", "sessionCurrency");
        mappingsJob2CronJob.put("sessionContextValues", "sessionContextValues");
        mappingsJob2CronJob.put("useReadOnlyDatasource", "useReadOnlyDatasource");
        mappingsJob2CronJob.put("emailAddress", "emailAddress");
        mappingsJob2CronJob.put("sendEmail", "sendEmail");
        mappingsJob2CronJob.put("emailNotificationTemplate", "emailNotificationTemplate");
        mappingsJob2CronJob.put("changeRecordingEnabled", "changeRecordingEnabled");
        mappingsJob2CronJob.put("removeOnExit", "removeOnExit");
        mappingsJob2CronJob.put("singleExecutable", "singleExecutable");
        mappingsJob2CronJob.put("retry", "retry");
        mappingsJob2CronJob.put("priority", "priority");
        mappingsJob2CronJob.put("alternativeDataSourceID", "alternativeDataSourceID");
        mappingsJob2CronJob.put("numberOfRetries", "numberOfRetries");
        return mappingsJob2CronJob;
    }


    protected CronJobLogListener.CronJobLogContext createLogContext(CronJob cronjob)
    {
        return (CronJobLogListener.CronJobLogContext)new Object(this, cronjob);
    }


    protected Level getEffectiveLogLevel(CronJob cronjob)
    {
        Level levelDB = cronjob.convertEnumToLogLevel(cronjob.getLogLevelDatabase());
        Level levelFile = cronjob.convertEnumToLogLevel(cronjob.getLogLevelFile());
        return levelDB.isGreaterOrEqual((Priority)levelFile) ? levelFile : levelDB;
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected abstract CronJob.CronJobResult performCronJob(CronJob paramCronJob) throws AbortCronJobException;
}
