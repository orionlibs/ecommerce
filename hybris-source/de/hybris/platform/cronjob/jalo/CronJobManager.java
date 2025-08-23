package de.hybris.platform.cronjob.jalo;

import com.google.common.base.Suppliers;
import de.hybris.platform.cluster.ClusterNodeManagementService;
import de.hybris.platform.cluster.DefaultClusterNodeManagementService;
import de.hybris.platform.cluster.legacy.LegacyBroadcastHandler;
import de.hybris.platform.cluster.legacy.MessageBroadcastListener;
import de.hybris.platform.commons.jalo.CommonsManager;
import de.hybris.platform.commons.jalo.renderer.RendererTemplate;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.Initialization;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.cronjob.StaleCronJobUnlocker;
import de.hybris.platform.cronjob.constants.GeneratedCronJobConstants;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.QueryOptions;
import de.hybris.platform.jalo.media.MediaFolder;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.AfterCronJobCrashAbortEvent;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerManager;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.task.Task;
import de.hybris.platform.task.TaskEngine;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.jalo.TriggerTask;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.StrandedItemsRegistry;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.logging.HybrisLogListener;
import de.hybris.platform.util.logging.HybrisLogger;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class CronJobManager extends GeneratedCronJobManager
{
    public static final String LOADONSTARTUP_CONFIG_PARAM = "cronjob.timertask.loadonstartup";
    public static final String CRON_JOB_HISTORY_RESET_THRESHOLD_HOURS = "cronjob.history.reset.threshold.hours";
    private static final String DEFAULT_CRONJOB_FINISH_NOTIFICATION_TEMPLATE_CODE = "DefaultCronJobFinishNotificationTemplate";
    private static final Logger LOG = Logger.getLogger(CronJobManager.class.getName());
    private static final int ERROR = 1812;
    private static final String PREFIX = "CRONJOB";
    private static final String COMMAND_START = "START";
    private static final String DELIMITER = "|";
    public static final String BEAN_NAME = "processing.manager";
    private final AtomicReference<StaleCronJobUnlocker> staleCronJobUnlocker = new AtomicReference<>(null);
    private static final String QUERY_WITH_STALE_NODES = "SELECT {" + CronJob.PK + "}, {runningOnClusterNode} FROM {" + GeneratedCronJobConstants.TC.CRONJOB + "} WHERE {runningOnClusterNode} IN (?staleNodeIds) AND {status} IN (?status) ORDER BY {" + CronJob.PK + "} ";
    private static final String QUERY_WITH_ALL_NODES = "SELECT {" + CronJob.PK + "}, {runningOnClusterNode} FROM {" + GeneratedCronJobConstants.TC.CRONJOB + "} WHERE ({runningOnClusterNode} NOT IN (?allNodeIds)) AND {status} IN (?status) ORDER BY {" + CronJob.PK + "} ";
    private static final String QUERY_WITH_STALE_AND_ALL_NODES =
                    "SELECT {" + CronJob.PK + "}, {runningOnClusterNode} FROM {" + GeneratedCronJobConstants.TC.CRONJOB + "} WHERE ({runningOnClusterNode} IN (?staleNodeIds) OR {runningOnClusterNode} NOT IN (?allNodeIds)) AND {status} IN (?status) ORDER BY {" + CronJob.PK + "} ";
    private static final String QUERY_CRON_JOB_HISTORY_ENTRIES = "SELECT {" + CronJobHistory.PK + "}, {status} FROM {" + GeneratedCronJobConstants.TC.CRONJOBHISTORY + "} WHERE {cronJob} = (?pk) AND {status} IN (?status)";
    private static final String QUERY_ALL_CRON_JOB_HISTORY_ENTRIES = "SELECT {" + CronJobHistory.PK + "}, {cronJob}, {status} FROM {" + GeneratedCronJobConstants.TC.CRONJOBHISTORY + "} WHERE {status} IN (?status) AND {" + CronJobHistory.CREATION_TIME + "} < ?threshold";
    private final Supplier<StrandedItemsRegistry> strandedCronJobsRegistry = (Supplier<StrandedItemsRegistry>)Suppliers.memoize(this::createStrandedCronJobsRegistry);
    public static final CronJobTenantListener listener = new CronJobTenantListener();
    public static final CronJobLogListener cronjobLogger = new CronJobLogListener();
    private static Pattern startRequestPattern;
    private KeyGenerator keyGenerator;
    private volatile MessageBroadcastListener messageListener;

    static
    {
        HybrisLogger.addListener((HybrisLogListener)cronjobLogger);
        Registry.registerTenantListener((TenantListener)listener);
    }

    private static Pattern getStartRequestPattern()
    {
        if(startRequestPattern == null)
        {
            startRequestPattern = Pattern.compile("START\\|(\\d+)\\|(\\d+)\\|(.+)\\|(.+)");
        }
        return startRequestPattern;
    }


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }


    protected void processStartMessage(String str)
    {
        try
        {
            int ownClusterID = MasterTenant.getInstance().getClusterID();
            Matcher startMatcher = getStartRequestPattern().matcher(str);
            if(startMatcher.matches())
            {
                int srcClusterNodeID = Integer.parseInt(startMatcher.group(1));
                int tgtClusterNodeID = Integer.parseInt(startMatcher.group(2));
                if(srcClusterNodeID != ownClusterID && tgtClusterNodeID == ownClusterID)
                {
                    Tenant currentTenant = Registry.getCurrentTenantNoFallback();
                    Registry.setCurrentTenantByID(startMatcher.group(3));
                    JaloSession jalosession = null;
                    try
                    {
                        jalosession = JaloConnection.getInstance().createAnonymousCustomerSession();
                        jalosession.activate();
                        getInstance().doStart(startMatcher.group(4));
                    }
                    finally
                    {
                        try
                        {
                            jalosession.close();
                        }
                        catch(Exception exception)
                        {
                        }
                        if(currentTenant == null)
                        {
                            Registry.unsetCurrentTenant();
                        }
                        else
                        {
                            Registry.setCurrentTenant(currentTenant);
                        }
                    }
                }
            }
            else
            {
                LOG.error("Received illegal cronjob start message '" + str + "'");
            }
        }
        catch(Exception e)
        {
            LOG.error("unexpected error parsing cronjob UDP packet : " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }


    protected void doStart(String cronJobPK)
    {
        try
        {
            CronJob cronjob = (CronJob)getSession().getItem(PK.parse(cronJobPK));
            cronjob.getJob().perform(cronjob, false);
        }
        catch(JaloItemNotFoundException e)
        {
            LOG.error("cannot perform remote start request - cronjob '" + cronJobPK + "' is not valid");
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void performOnOtherNode(int nodeID, CronJob cronJob)
    {
        MasterTenant masterTenant = Registry.getMasterTenant();
        if(masterTenant.getClusterID() == nodeID)
        {
            cronJob.getJob().perform(cronJob, false);
        }
        else if(masterTenant.isClusteringEnabled())
        {
            StringBuilder text = new StringBuilder();
            text.append("START").append("|");
            text.append(masterTenant.getClusterID()).append("|");
            text.append(nodeID).append("|");
            text.append(getTenant().getTenantID()).append("|");
            text.append(cronJob.getPK().toString());
            LegacyBroadcastHandler.getInstance().sendCustomPacket(
                            getCronjobBroadcastMessagePrefix(), text
                                            .toString());
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Sending cronjob " + cronJob.getCode() + " to node " + nodeID + ". Tenant is " +
                                getTenant().getTenantID());
            }
        }
        else
        {
            throw new IllegalStateException("cannot start on other node since clustering is not enabled");
        }
    }


    public void onFirstSessionCreation()
    {
        startupCronjobEngine();
    }


    protected void startupCronjobEngine()
    {
        if(Utilities.isSystemInitialized(Registry.getCurrentTenant().getDataSource()))
        {
            abortRunningCronJobsForClusterNode(MasterTenant.getInstance().getClusterID());
            LegacyBroadcastHandler.getInstance().registerMessageListener(
                            getCronjobBroadcastMessagePrefix(), this.messageListener = (MessageBroadcastListener)new Object(this));
            startStaleCronJobUnlocker();
        }
    }


    private void startStaleCronJobUnlocker()
    {
        ClusterNodeManagementService clusterManagement = DefaultClusterNodeManagementService.getInstance();
        if(!clusterManagement.isAutoDiscoveryEnabled())
        {
            return;
        }
        StaleCronJobUnlocker newThread = new StaleCronJobUnlocker(Registry.getCurrentTenant());
        if(this.staleCronJobUnlocker.compareAndSet(null, newThread))
        {
            newThread.start();
        }
    }


    private void stopStaleCronJobUnlocker()
    {
        StaleCronJobUnlocker thread = this.staleCronJobUnlocker.getAndSet(null);
        if(thread != null)
        {
            thread.interrupt();
            thread.stopUpdatingAndFinish(Duration.ofSeconds(10L).toMillis());
        }
    }


    protected void stopConjobEngine()
    {
        if(this.messageListener != null)
        {
            try
            {
                LegacyBroadcastHandler.getInstance().unregisterMessageListener(getCronjobBroadcastMessagePrefix());
                stopStaleCronJobUnlocker();
            }
            catch(de.hybris.platform.core.AbstractTenant.TenantNotYetStartedException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(e.getMessage(), (Throwable)e);
                }
            }
            catch(Exception e)
            {
                String msg = "Problem occured during stopping CronJob Engine [reason: " + e.getMessage() + "]";
                LOG.error(msg);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(msg, e);
                }
            }
            finally
            {
                this.messageListener = null;
            }
        }
    }


    protected String getCronjobBroadcastMessagePrefix()
    {
        return "CRONJOB_" + Registry.getMasterTenant().getClusterIslandPK();
    }


    public static CronJobManager getInstance()
    {
        return (CronJobManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("processing");
    }


    public static CronJobManager getInstance(Tenant tenant)
    {
        CronJobManager currentManager = getInstance();
        if(tenant != currentManager.getTenant())
        {
            throw new IllegalStateException("Current cron job manager 'processing.manager' : " + currentManager + " belongs to wrong tenant (expected " + tenant + " but was " + currentManager
                            .getTenant() + ").");
        }
        return currentManager;
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
        if(item instanceof Trigger)
        {
            TriggerTask triggerTask = findTaskForTrigger((Trigger)item);
            if(triggerTask != null)
            {
                triggerTask.remove();
            }
        }
    }


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
    }


    @Deprecated(since = "4.3", forRemoval = false)
    public BatchJob createBatchJob(String code)
    {
        Map<Object, Object> map = new HashMap<>();
        map.put("code", code);
        return createBatchJob(map);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CronJob createCronJob(Job job, String code, boolean active)
    {
        int node = MasterTenant.getInstance().getClusterID();
        return createCronJob(job, code, active, node);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CronJob createCronJob(Job job, String code, boolean active, int node)
    {
        try
        {
            Map<Object, Object> map = new HashMap<>();
            map.put("job", job);
            map.put("code", code);
            map.put("active", Boolean.valueOf(active));
            map.put("nodeID", Integer.valueOf(node));
            return createCronJob(map);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public TypeSystemExportJob createTypeSystemExportJob(String code)
    {
        try
        {
            Map<Object, Object> map = new HashMap<>();
            map.put("code", code);
            return createTypeSystemExportJob(map);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Trigger createTrigger(CronJob cronJob, Date date)
    {
        Calendar cal = Utilities.getDefaultCalendar();
        cal.setTime(date);
        return createTrigger(cronJob, cal.get(13), cal.get(12), cal.get(11), cal
                        .get(5), cal.get(2), cal.get(1), null, false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Trigger createTrigger(CronJob cronJob, int second, boolean relative)
    {
        return createTrigger(cronJob, second, -1, -1, -1, -1, -1, null, relative);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Trigger createTrigger(CronJob cronJob, int second, int minute, boolean relative)
    {
        return createTrigger(cronJob, second, minute, -1, -1, -1, -1, null, relative);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Trigger createTrigger(CronJob cronJob, int second, int minute, int hour, boolean relative)
    {
        return createTrigger(cronJob, second, minute, hour, -1, -1, -1, null, relative);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Trigger createTrigger(CronJob cronJob, int second, int minute, int hour, int day, boolean relative)
    {
        return createTrigger(cronJob, second, minute, hour, day, -1, -1, null, relative);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Trigger createTrigger(CronJob cronJob, int second, int minute, int hour, int day, int month, boolean relative)
    {
        return createTrigger(cronJob, second, minute, hour, day, month, -1, null, relative);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Trigger createTrigger(CronJob cronJob, int second, int minute, int hour, int day, int month, int year)
    {
        return createTrigger(cronJob, second, minute, hour, day, month, year, null, false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Trigger createTrigger(CronJob cronJob, int second, int minute, int hour, List daysOfWeek, boolean relative)
    {
        return createTrigger(cronJob, second, minute, hour, -1, -1, -1, daysOfWeek, false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Trigger createTrigger(CronJob cronJob, int second, int minute, int hour, int day, int month, int year, List daysOfWeek, boolean relative)
    {
        Trigger trigger = null;
        try
        {
            Map<Object, Object> map = new HashMap<>();
            map.put("cronJob", cronJob);
            map.put("second", Integer.valueOf(second));
            map.put("minute", Integer.valueOf(minute));
            map.put("hour", Integer.valueOf(hour));
            map.put("day", Integer.valueOf(day));
            map.put("month", Integer.valueOf(month));
            map.put("year", Integer.valueOf(year));
            map.put("relative", Boolean.valueOf(relative));
            map.put("daysOfWeek", daysOfWeek);
            trigger = createTrigger(map);
            trigger.setActivationTime(new Date(trigger.getNextTime(System.currentTimeMillis())));
            trigger.setMaxAcceptableDelay(-1);
            return trigger;
        }
        catch(RuntimeException e)
        {
            if(trigger != null)
            {
                try
                {
                    trigger.remove();
                }
                catch(Exception ex)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(ex.getMessage());
                    }
                }
            }
            throw e;
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Job getJob(String code)
    {
        GenericCondition codeCondition = GenericCondition.equals(new GenericSearchField(GeneratedCronJobConstants.TC.JOB, "code"), code);
        GenericConditionList genericcondlist = GenericCondition.createConditionList(codeCondition);
        GenericQuery query = new GenericQuery(GeneratedCronJobConstants.TC.JOB, (GenericCondition)genericcondlist);
        List<Job> result = getSession().search(query, getSession().createSearchContext()).getResult();
        if(result.isEmpty())
        {
            return null;
        }
        if(result.size() > 1)
        {
            LOG.error("Warning: " + result.size() + " matching job for code " + code + "! Returning first job!");
        }
        return result.iterator().next();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<CronJob> getRunningOrRestartedCronJobsForNode(int nodeID)
    {
        boolean createdLocally = assureAdminSession();
        JaloSession current = getSession();
        try
        {
            EnumerationManager eumman = current.getEnumerationManager();
            EnumerationValue running = eumman.getEnumerationValue(GeneratedCronJobConstants.TC.CRONJOBSTATUS, GeneratedCronJobConstants.Enumerations.CronJobStatus.RUNNING);
            EnumerationValue runningRestart = eumman.getEnumerationValue(GeneratedCronJobConstants.TC.CRONJOBSTATUS, GeneratedCronJobConstants.Enumerations.CronJobStatus.RUNNINGRESTART);
            GenericCondition nodeCondition = GenericCondition.equals(new GenericSearchField(GeneratedCronJobConstants.TC.CRONJOB, "runningOnClusterNode"),
                            Integer.valueOf(nodeID));
            GenericCondition runningCondition = GenericCondition.equals(new GenericSearchField(GeneratedCronJobConstants.TC.CRONJOB, "status"), running);
            GenericCondition runningRestartCondition = GenericCondition.equals(new GenericSearchField(GeneratedCronJobConstants.TC.CRONJOB, "status"), runningRestart);
            GenericConditionList genericcondlist = GenericCondition.createConditionList(nodeCondition);
            Collection<GenericCondition> statusConditions = Arrays.asList(new GenericCondition[] {runningCondition, runningRestartCondition});
            GenericConditionList statusList = GenericCondition.createConditionList(statusConditions, Operator.OR);
            genericcondlist.addToConditionList((GenericCondition)statusList);
            GenericQuery query = new GenericQuery(GeneratedCronJobConstants.TC.CRONJOB, (GenericCondition)genericcondlist);
            return current.search(query, current.createSearchContext()).getResult();
        }
        finally
        {
            if(createdLocally)
            {
                if(current != null)
                {
                    current.close();
                }
            }
        }
    }


    private boolean assureAdminSession()
    {
        JaloSession current = getSession();
        if(current == null)
        {
            JaloConnection conn = JaloConnection.getInstance();
            try
            {
                current = conn.createAnonymousCustomerSession();
                current.setUser((User)UserManager.getInstance().getAdminEmployee());
                current.activate();
                return true;
            }
            catch(JaloSystemException jse)
            {
                if(Initialization.isTenantInitializingLocally(Registry.getCurrentTenantNoFallback()))
                {
                    LOG.warn("Can not create admin session " + jse.getMessage());
                }
                else
                {
                    throw new IllegalStateException("Can not  create admin session " + jse.getMessage(), jse);
                }
            }
            catch(JaloSecurityException e)
            {
                if(Initialization.isTenantInitializingLocally(Registry.getCurrentTenantNoFallback()))
                {
                    LOG.warn("Can not create admin session " + e.getMessage());
                }
                else
                {
                    throw new IllegalStateException("Can not  create admin session " + e.getMessage(), e);
                }
            }
        }
        return false;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean abortRunningCronJobsForClusterNode(int nodeID)
    {
        boolean done = true;
        for(Iterator<CronJob> iter = getRunningOrRestartedCronJobsForNode(nodeID).iterator(); iter.hasNext(); )
        {
            CronJob cronJob = iter.next();
            if(cronJob.isRunning() || cronJob.isRunningRestart())
            {
                String logMessage = createAbortedCronJobLogMessage(cronJob.getCode(), Set.of(Integer.valueOf(nodeID)));
                LOG.warn(logMessage);
                cronJob.setAborted();
                cronJob.addLog(logMessage);
                LOG.warn("CronJob " + cronJob.getCode() + " is set aborted");
                sendAfterCronJobCrashAbortEvent(prepareCrashAbortEvent(cronJob));
                findAndUpdateCronJobHistoryEntry(cronJob.getPK());
                continue;
            }
            LOG.warn("Ooops. CronJob " + cronJob.getCode() + " is not running on node " + cronJob.getRunningOnClusterNode());
            done = false;
        }
        return done;
    }


    private String createAbortedCronJobLogMessage(String code, Set<Integer> runningOnClusterNode)
    {
        return "CronJob " + code + " is marked as running on one of cluster nodes " + runningOnClusterNode + ". It will be marked as ABORTED. This could happen if the system is shut down with a hard signal (e.g. CTRL-C) and a job is still in running state.";
    }


    @Deprecated(since = "2011", forRemoval = true)
    public void abortRunningCronJobsForClusterNodes(Set<Integer> nodeIds, int maxCronJobsCount)
    {
        abortRunningCronJobsForClusterNodes(nodeIds, Set.of(), maxCronJobsCount);
    }


    @Deprecated(since = "internal")
    public void abortRunningCronJobsForClusterNodes(Set<Integer> staleNodeIds, Set<Integer> allNodeIds, int maxCronJobsCount)
    {
        List<Pair<PK, Integer>> result;
        if(CollectionUtils.isEmpty(staleNodeIds) && CollectionUtils.isEmpty(allNodeIds))
        {
            return;
        }
        if(CollectionUtils.isEmpty(allNodeIds))
        {
            result = getCronJobsToBeAborted(staleNodeIds, maxCronJobsCount);
        }
        else
        {
            result = getCronJobsToBeAborted(staleNodeIds, allNodeIds, maxCronJobsCount);
        }
        Function<CronJob, String> logEntrySupplier = cronJob -> createAbortedCronJobLogMessage(cronJob.getCode(), staleNodeIds);
        abortCronJobs(result, logEntrySupplier);
    }


    private boolean findAndUpdateCronJobHistoryEntry(PK abortedCronJobPk)
    {
        Optional<Pair<PK, PK>> cronJobHistoryPK = Optional.empty();
        try
        {
            cronJobHistoryPK = findActiveCronJobHistoryEntry(abortedCronJobPk);
            return ((Boolean)cronJobHistoryPK.<Boolean>map(pk -> Boolean.valueOf(updateCronJobHistoryEntry((PK)pk.getLeft(), abortedCronJobPk, (PK)pk.getRight()))).orElse(Boolean.valueOf(false))).booleanValue();
        }
        catch(Exception ex)
        {
            LOG.error(String.format("Error while updating history entry %s for CronJob %s", new Object[] {cronJobHistoryPK, abortedCronJobPk}));
            return false;
        }
    }


    private boolean updateCronJobHistoryEntry(PK cronJobHistoryPK, PK abortedCronJobPk, PK statusPK)
    {
        JDBCValueMappings jdbcValueMappings = JDBCValueMappings.getInstance();
        JDBCValueMappings.ValueWriter<PK, ?> pkWriter = jdbcValueMappings.getValueWriter(PK.class);
        ComposedType cronJobHistoryType = TypeManager.getInstance().getRootComposedType(cronJobHistoryPK.getTypeCode());
        AttributeDescriptor statusColumn = cronJobHistoryType.getAttributeDescriptor("status");
        AttributeDescriptor cronJobColumn = cronJobHistoryType.getAttributeDescriptor("cronJob");
        PK abortedStatusPK = getAbortedStatusPK();
        String update = String.format("UPDATE %s SET %s=?, HJMPTS=HJMPTS + 1 WHERE %s=? AND %s=? AND PK=?", new Object[] {cronJobHistoryType.getTable(), statusColumn.getDatabaseColumn(), statusColumn.getDatabaseColumn(), cronJobColumn.getDatabaseColumn()});
        int updatedRows = getJdbcTemplate().update(update, preparedStatement -> {
            pkWriter.setValue(preparedStatement, 1, abortedStatusPK);
            pkWriter.setValue(preparedStatement, 2, statusPK);
            pkWriter.setValue(preparedStatement, 3, abortedCronJobPk);
            pkWriter.setValue(preparedStatement, 4, cronJobHistoryPK);
        });
        Utilities.invalidateCache(cronJobHistoryPK);
        LOG.debug(String.format("Updated history entry %s for cron job %s", new Object[] {cronJobHistoryPK, abortedCronJobPk}));
        return (updatedRows != 0);
    }


    private Optional<Pair<PK, PK>> findActiveCronJobHistoryEntry(PK abortedCronJobPk)
    {
        QueryOptions queryOptions = QueryOptions.newBuilder().withQuery(QUERY_CRON_JOB_HISTORY_ENTRIES).withResultClasses(List.of(PK.class, PK.class)).withValues(Map.of("pk", abortedCronJobPk, "status", (PK)Set.of(getRunningRestartStatusPk(), getRunningStatusPk()))).withDontNeedTotal(true)
                        .withFailOnUnknownFields(true).build();
        List<?> pks = FlexibleSearch.getInstance().search(queryOptions).getResult();
        if(pks.size() == 0)
        {
            LOG.error(String.format("Found no history entries for cron job %s", new Object[] {abortedCronJobPk}));
            return Optional.empty();
        }
        if(pks.size() > 1)
        {
            LOG.error(String.format("CronJob %s has more than one active history entry", new Object[] {abortedCronJobPk}));
        }
        List<PK> row = (List<PK>)pks.get(0);
        return Optional.of(Pair.of(row.get(0), row.get(1)));
    }


    public void findAndFixAllCronJobHistoryEntries()
    {
        List<List<PK>> allCronJobsHistoryEntries = findAllCronJobHistoryEntries();
        LOG.debug("Found " + allCronJobsHistoryEntries.size() + " cron job history entries");
        allCronJobsHistoryEntries.stream().forEach(entry -> updateCronJobHistoryEntry(entry.get(0), entry.get(1), entry.get(2)));
    }


    protected List<List<PK>> findAllCronJobHistoryEntries()
    {
        Instant minus = Instant.now().minus(Duration.ofHours(Config.getInt("cronjob.history.reset.threshold.hours", 24)));
        QueryOptions queryOptions = QueryOptions.newBuilder().withQuery(QUERY_ALL_CRON_JOB_HISTORY_ENTRIES).withResultClasses(List.of(PK.class, PK.class, PK.class)).withValues(Map.of("status", (Date)Set.of(getRunningRestartStatusPk(), getRunningStatusPk()), "threshold", Date.from(minus)))
                        .withDontNeedTotal(true).withFailOnUnknownFields(true).build();
        return FlexibleSearch.getInstance().search(queryOptions).getResult();
    }


    private Set<PK> abortCronJobs(List<Pair<PK, Integer>> result, Function<CronJob, String> logEntrySupplier)
    {
        Set<PK> pks = updateCronJobsStatusToAborted(result);
        if(pks.isEmpty())
        {
            return pks;
        }
        invalidateCache(pks);
        publishAfterCrashAbortEvent(logEntrySupplier, pks);
        return pks;
    }


    private void invalidateCache(Set<PK> pks)
    {
        pks.forEach(Utilities::invalidateCache);
    }


    private void publishAfterCrashAbortEvent(Function<CronJob, String> logEntrySupplier, Set<PK> pks)
    {
        for(PK pk : pks)
        {
            try
            {
                CronJob cronJob = (CronJob)getSession().getItem(pk);
                String msg = logEntrySupplier.apply(cronJob);
                cronJob.addLog(msg);
                LOG.warn(msg);
                AfterCronJobCrashAbortEvent afterCronJobCrashAbortEvent = prepareCrashAbortEvent(cronJob);
                sendAfterCronJobCrashAbortEvent(afterCronJobCrashAbortEvent);
            }
            catch(Exception ex)
            {
                LOG.warn(String.format("An exception has been thrown while publishing AfterCronJobCrashAbortEvent for cronjob %s", new Object[] {pk}), ex);
            }
        }
    }


    @Deprecated(since = "internal")
    protected List<Pair<PK, Integer>> getLocalCronJobsToBeAborted(Set<PK> cronJobsToCheck)
    {
        PK runningPk = getRunningStatusPk();
        PK runningRestartPk = getRunningRestartStatusPk();
        String query = "SELECT {" + CronJob.PK + "}, {runningOnClusterNode} FROM {" + GeneratedCronJobConstants.TC.CRONJOB + "} WHERE {" + CronJob.PK + "} IN (?pks) AND {runningOnClusterNode} IN (?currentNodeId) AND {status} IN (?status)";
        QueryOptions queryOptions = QueryOptions.newBuilder().withQuery(query).withResultClasses(List.of(PK.class, Integer.class)).withValues(Map.of("pks", cronJobsToCheck, "currentNodeId", Integer.valueOf(Registry.getClusterID()), "status", Set.of(runningPk, runningRestartPk)))
                        .withDontNeedTotal(true).withFailOnUnknownFields(true).build();
        List<Pair<PK, Integer>> cronJobsToAbort = new ArrayList<>();
        SearchResult<?> pks = FlexibleSearch.getInstance().search(queryOptions);
        for(List<?> cronJob : (Iterable<List<?>>)pks.getResult())
        {
            PK pk = (PK)cronJob.get(0);
            Integer runningOnClusterNodeId = (Integer)cronJob.get(1);
            cronJobsToAbort.add(Pair.of(pk, runningOnClusterNodeId));
        }
        return cronJobsToAbort;
    }


    private String createAbortedLocalCronJobLogMessage(String code)
    {
        return "CronJob " + code + " is marked as running on this node (" + Registry.getClusterID()
                        + ") but the thread has been finished. It will be marked as ABORTED. This could happen if there is a problem with updating the status of cronJob during the end of the cronJob execution (ex. due to the temporary db outage)";
    }


    @Deprecated(since = "2011", forRemoval = true)
    protected List<Pair<PK, Integer>> getCronJobsToBeAborted(Set<Integer> nodeID, int maxCount)
    {
        return getCronJobsToBeAborted(nodeID, Set.of(), maxCount);
    }


    protected List<Pair<PK, Integer>> getCronJobsToBeAborted(Set<Integer> staleNodeIds, Set<Integer> allNodeIds, int maxCount)
    {
        String query;
        Map<String, Object> values;
        if(CollectionUtils.isEmpty(staleNodeIds) && CollectionUtils.isEmpty(allNodeIds))
        {
            return List.of();
        }
        PK runningPk = getRunningStatusPk();
        PK runningRestartPk = getRunningRestartStatusPk();
        if(CollectionUtils.isEmpty(allNodeIds))
        {
            values = Map.of("staleNodeIds", staleNodeIds, "status", Set.of(runningPk, runningRestartPk));
            query = QUERY_WITH_STALE_NODES;
        }
        else if(CollectionUtils.isEmpty(staleNodeIds))
        {
            Set<Integer> allValidNodeIds = new HashSet<>(allNodeIds);
            allValidNodeIds.add(Integer.valueOf(-1));
            values = Map.of("allNodeIds", allValidNodeIds, "status", Set.of(runningPk, runningRestartPk));
            query = QUERY_WITH_ALL_NODES;
        }
        else
        {
            Set<Integer> allValidNodeIds = new HashSet<>(allNodeIds);
            allValidNodeIds.add(Integer.valueOf(-1));
            values = Map.of("staleNodeIds", staleNodeIds, "allNodeIds", allValidNodeIds, "status",
                            Set.of(runningPk, runningRestartPk));
            query = QUERY_WITH_STALE_AND_ALL_NODES;
        }
        QueryOptions.Builder queryBuilder = QueryOptions.newBuilder().withQuery(query).withResultClasses(List.of(PK.class, Integer.class)).withDontNeedTotal(true).withValues(values);
        if(maxCount > 0)
        {
            queryBuilder.withCount(maxCount);
        }
        SearchResult<List<Object>> search = getSession().getFlexibleSearch().search(queryBuilder.build());
        return (List<Pair<PK, Integer>>)search.getResult().stream().map(row -> Pair.of(row.get(0), row.get(1))).collect(Collectors.toList());
    }


    protected Set<PK> updateCronJobsStatusToAborted(List<Pair<PK, Integer>> result)
    {
        if(result.isEmpty())
        {
            return Collections.emptySet();
        }
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        Set<PK> updatedPks = new HashSet<>();
        PK cronJobStatusAbortedPK = getAbortedStatusPK();
        for(Pair<PK, Integer> row : result)
        {
            PK pk = (PK)row.getLeft();
            Integer runningOnClusterNodeId = (Integer)row.getRight();
            boolean updated = updateCronJobStatusToAborted(pk, runningOnClusterNodeId, cronJobStatusAbortedPK, jdbcTemplate);
            if(updated)
            {
                updatedPks.add(pk);
                findAndUpdateCronJobHistoryEntry(pk);
            }
        }
        return updatedPks;
    }


    private JdbcTemplate getJdbcTemplate()
    {
        return (JdbcTemplate)Registry.getApplicationContext().getBean("jdbcTemplate", JdbcTemplate.class);
    }


    protected boolean updateCronJobStatusToAborted(PK pk, Integer runningOnClusterNodeId, PK cronJobStatusAbortedPK, JdbcTemplate jdbcTemplate)
    {
        try
        {
            JDBCValueMappings jdbcValueMappings = JDBCValueMappings.getInstance();
            JDBCValueMappings.ValueWriter<PK, ?> pkWriter = jdbcValueMappings.getValueWriter(PK.class);
            JDBCValueMappings.ValueWriter<Integer, ?> integerWriter = jdbcValueMappings.getValueWriter(Integer.class);
            TypeManager typeManager = getSession().getTypeManager();
            ComposedType rootComposedType = typeManager.getRootComposedType(pk.getTypeCode());
            String table = rootComposedType.getTable();
            String runningOnClusterColumn = rootComposedType.getAttributeDescriptor("runningOnClusterNode").getDatabaseColumn();
            String status = rootComposedType.getAttributeDescriptor("status").getDatabaseColumn();
            String update = String.format("UPDATE %s SET %s=?, HJMPTS=HJMPTS + 1, %s=-1 WHERE PK=? AND %s=?", new Object[] {table, status, runningOnClusterColumn, runningOnClusterColumn});
            int updatedRows = jdbcTemplate.update(update, preparedStatement -> {
                pkWriter.setValue(preparedStatement, 1, cronJobStatusAbortedPK);
                pkWriter.setValue(preparedStatement, 2, pk);
                integerWriter.setValue(preparedStatement, 3, runningOnClusterNodeId);
            });
            return (updatedRows != 0);
        }
        catch(Exception ex)
        {
            LOG.warn(String.format("Exception has been thrown while aborting cronjob %s ", new Object[] {pk}), ex);
            return false;
        }
    }


    private PK getAbortedStatusPK()
    {
        return getSession().getEnumerationManager()
                        .getEnumerationValue(CronJobStatus.ABORTED
                                        .getType(), CronJobStatus.ABORTED
                                        .getCode())
                        .getPK();
    }


    private PK getRunningRestartStatusPk()
    {
        return getSession().getEnumerationManager()
                        .getEnumerationValue(CronJobStatus.RUNNINGRESTART.getType(), CronJobStatus.RUNNINGRESTART
                                        .getCode()).getPK();
    }


    private PK getRunningStatusPk()
    {
        return getSession().getEnumerationManager()
                        .getEnumerationValue(CronJobStatus.RUNNING
                                        .getType(), CronJobStatus.RUNNING
                                        .getCode())
                        .getPK();
    }


    protected void sendAfterCronJobCrashAbortEvent(AfterCronJobCrashAbortEvent evt)
    {
        ServicelayerManager.getInstance().publishEvent((AbstractEvent)evt);
    }


    protected AfterCronJobCrashAbortEvent prepareCrashAbortEvent(CronJob cj)
    {
        AfterCronJobCrashAbortEvent evt = new AfterCronJobCrashAbortEvent();
        evt.setCronJobPK(cj.getPK());
        evt.setCronJob(cj.getCode());
        evt.setCronJobType(cj.getComposedType().getCode());
        Job job = cj.getJob();
        evt.setJob(job.getCode());
        evt.setJobType(job.getComposedType().getCode());
        return evt;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Trigger> getActiveTriggers()
    {
        return getActiveTriggers(new Date(System.currentTimeMillis()));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Trigger> getActiveTriggers(Date date)
    {
        GenericQuery query = new GenericQuery(GeneratedCronJobConstants.TC.TRIGGER);
        query.addConditions(new GenericCondition[] {GenericCondition.equals("active", Boolean.TRUE),
                        GenericCondition.createLessOrEqualCondition(new GenericSearchField("activationTime"), date)});
        List<Trigger> triggers = getSession().search(query, getSession().createSearchContext()).getResult();
        List<Trigger> returntriggers = new ArrayList<>();
        int clusterID = MasterTenant.getInstance().getClusterID();
        for(Trigger trigger : triggers)
        {
            if(trigger.getCronJob() != null && trigger.getCronJob().getNodeIDAsPrimitive() == clusterID)
            {
                returntriggers.add(trigger);
                continue;
            }
            if(trigger.getJob() != null && trigger.getJob().getNodeIDAsPrimitive() == clusterID)
            {
                returntriggers.add(trigger);
            }
        }
        return returntriggers;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public BatchJob createBatchJob(Map params)
    {
        return super.createBatchJob(params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public BatchJob createBatchJob(SessionContext ctx, Map params)
    {
        return super.createBatchJob(ctx, params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ChangeDescriptor createChangeDescriptor(Map params)
    {
        return super.createChangeDescriptor(params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ChangeDescriptor createChangeDescriptor(SessionContext ctx, Map params)
    {
        return super.createChangeDescriptor(ctx, params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CronJob createCronJob(Map params)
    {
        return super.createCronJob(params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CronJob createCronJob(SessionContext ctx, Map params)
    {
        return super.createCronJob(ctx, params);
    }


    @Deprecated(since = "4.3", forRemoval = false)
    public CSVExportStep createCSVExportStep(Map params)
    {
        return super.createCSVExportStep(params);
    }


    @Deprecated(since = "4.3", forRemoval = false)
    public CSVExportStep createCSVExportStep(SessionContext ctx, Map params)
    {
        return super.createCSVExportStep(ctx, params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public FlexibleSearchCronJob createFlexibleSearchCronJob(Map params)
    {
        return super.createFlexibleSearchCronJob(params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public FlexibleSearchCronJob createFlexibleSearchCronJob(SessionContext ctx, Map params)
    {
        return super.createFlexibleSearchCronJob(ctx, params);
    }


    @Deprecated(since = "4.3", forRemoval = false)
    public GetURLStep createGetURLStep(Map params)
    {
        return super.createGetURLStep(params);
    }


    @Deprecated(since = "4.3", forRemoval = false)
    public GetURLStep createGetURLStep(SessionContext ctx, Map params)
    {
        return super.createGetURLStep(ctx, params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public JobLog createJobLog(Map params)
    {
        return super.createJobLog(params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public JobLog createJobLog(SessionContext ctx, Map params)
    {
        return super.createJobLog(ctx, params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public JobMedia createJobMedia(Map params)
    {
        return super.createJobMedia(params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public JobMedia createJobMedia(SessionContext ctx, Map params)
    {
        return super.createJobMedia(ctx, params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public MediaProcessCronJob createMediaProcessCronJob(Map params)
    {
        return super.createMediaProcessCronJob(params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public MediaProcessCronJob createMediaProcessCronJob(SessionContext ctx, Map params)
    {
        return super.createMediaProcessCronJob(ctx, params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Trigger createTrigger(Map params)
    {
        return super.createTrigger(params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Trigger createTrigger(SessionContext ctx, Map params)
    {
        return super.createTrigger(ctx, params);
    }


    private Map prepareTriggerTaskAttributes(Trigger trigger)
    {
        Map<Object, Object> triggerTaskMap = new HashMap<>();
        triggerTaskMap.put("trigger", trigger);
        if(trigger.getActivationTime() != null)
        {
            triggerTaskMap.put("executionTimeMillis",
                            Long.valueOf(trigger.getActivationTime().getTime() + TimeUnit.SECONDS.toMillis(10L)));
        }
        if(trigger.getCronJob() != null && trigger.getCronJob().getNodeID() != null)
        {
            triggerTaskMap.put("nodeId", trigger.getCronJob().getNodeID());
        }
        else if(trigger.getJob() != null && trigger.getJob().getNodeID() != null)
        {
            triggerTaskMap.put("nodeId", trigger.getJob().getNodeID());
        }
        else if(trigger.getCronJob() != null && trigger.getCronJob().getNodeGroup() != null)
        {
            triggerTaskMap.put("nodeGroup", trigger.getCronJob().getNodeGroup());
        }
        else if(trigger.getJob() != null && trigger.getJob().getNodeGroup() != null)
        {
            triggerTaskMap.put("nodeGroup", trigger.getJob().getNodeGroup());
        }
        return triggerTaskMap;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public TypeSystemExportJob createTypeSystemExportJob(Map params)
    {
        return super.createTypeSystemExportJob(params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public TypeSystemExportJob createTypeSystemExportJob(SessionContext ctx, Map params)
    {
        return super.createTypeSystemExportJob(ctx, params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public URLCronJob createURLCronJob(Map params)
    {
        return super.createURLCronJob(params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public URLCronJob createURLCronJob(SessionContext ctx, Map params)
    {
        return super.createURLCronJob(ctx, params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getNextConjobNumber()
    {
        return getNextCronjobNumber();
    }


    public String getNextCronjobNumber()
    {
        return (String)this.keyGenerator.generate();
    }


    public boolean isCreatorDisabled()
    {
        return false;
    }


    public void createEssentialData(Map params, JspContext jspc) throws Exception
    {
        if(MediaManager.getInstance().getMediaFolderByQualifier("cronjob").size() < 1)
        {
            MediaManager.getInstance().createMediaFolder("cronjob", "cronjob");
        }
        fixingCronJobs();
        importCSVFromResources("/cronjob/DefaultCronJobFinishNotificationTemplate.csv");
        cleanupSessionCtxReferences();
    }


    private void cleanupSessionCtxReferences()
    {
        (new SessionCtxValueJDBCCleaner()).cleanAttribute();
    }


    private void importCSVFromResources(String csv)
    {
        LOG.info("importing resource " + csv);
        InputStream inputstream = CronJobManager.class.getResourceAsStream(csv);
        try
        {
            Class<?> singletonClass = Class.forName("de.hybris.platform.impex.jalo.ImpExManager");
            Method singletonMethod = singletonClass.getMethod("getInstance", new Class[0]);
            Object importManagerInstanace = singletonMethod.invoke(null, new Object[0]);
            Method impexMethod = singletonClass.getMethod("importData", new Class[] {InputStream.class, String.class, char.class, char.class, boolean.class});
            impexMethod.invoke(importManagerInstanace, new Object[] {inputstream, "windows-1252", Character.valueOf(';'),
                            Character.valueOf('"'), Boolean.TRUE});
        }
        catch(ClassNotFoundException e)
        {
            LOG.warn("Unable to load class de.hybris.platform.impex.jalo.ImpExManager during initalization default renederTemplate ", e);
        }
        catch(SecurityException e)
        {
            LOG.warn("Can't access(security considerations) importData method for class de.hybris.platform.impex.jalo.ImpExManager during initalization default renederTemplate ", e);
        }
        catch(NoSuchMethodException e)
        {
            LOG.warn("Unable to find importData method for class de.hybris.platform.impex.jalo.ImpExManager during initalization default renederTemplate ", e);
        }
        catch(IllegalArgumentException e)
        {
            LOG.warn("Invalid parameters passed to importData method for class de.hybris.platform.impex.jalo.ImpExManager during initalization default renederTemplate ", e);
        }
        catch(IllegalAccessException e)
        {
            LOG.warn("Can't access(not visible) importData method for class de.hybris.platform.impex.jalo.ImpExManager during initalization default renederTemplate ", e);
        }
        catch(InvocationTargetException e)
        {
            LOG.warn("Can't inovke importData method for class de.hybris.platform.impex.jalo.ImpExManager during initalization default renederTemplate ", e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public RendererTemplate getDefaultCronJobFinishNotificationTemplate()
    {
        return (RendererTemplate)CommonsManager.getInstance().getFirstItemByAttribute(RendererTemplate.class, "code", "DefaultCronJobFinishNotificationTemplate");
    }


    @Deprecated(since = "ages", forRemoval = false)
    public MediaFolder getCronJobMediaFolder()
    {
        Collection<MediaFolder> folders = MediaManager.getInstance().getMediaFolderByQualifier("cronjob");
        if(folders.isEmpty())
        {
            return null;
        }
        return folders.iterator().next();
    }


    private void fixingCronJobs()
    {
        Collection<CronJob> cronJobs = getExistingCronJobs();
        if(cronJobs != null)
        {
            for(Iterator<CronJob> it = cronJobs.iterator(); it.hasNext(); )
            {
                CronJob cronjob = it.next();
                try
                {
                    cronjob.setAttribute("logToDatabase", Boolean.TRUE);
                    cronjob.setAttribute("logLevelDatabase",
                                    EnumerationManager.getInstance().getEnumerationValue(GeneratedCronJobConstants.TC.JOBLOGLEVEL, GeneratedCronJobConstants.Enumerations.JobLogLevel.INFO));
                }
                catch(JaloSecurityException e)
                {
                    throw new JaloSystemException(e, e.getMessage(), 1812);
                }
                catch(JaloBusinessException e)
                {
                    throw new JaloSystemException(e, e.getMessage(), 1812);
                }
            }
        }
    }


    private Collection<CronJob> getExistingCronJobs()
    {
        return getSession()
                        .getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {" +
                                                        TypeManager.getInstance()
                                                                        .getComposedType(CronJob.class)
                                                                        .getCode() + "} WHERE {logToDatabase} IS NULL ORDER BY {code} ASC, {" + Item.PK + "} ASC", Collections.EMPTY_MAP,
                                        Collections.singletonList(CronJob.class), true, true, 0, -1)
                        .getResult();
    }


    protected TriggerTask findTaskForTrigger(Trigger trigger)
    {
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put("disableCache", Boolean.valueOf(true));
        SessionContext.SessionContextAttributeSetter attributeSetter = getSession().getSessionContext().setSessionContextAttributesLocally(sessionAttributes);
        try
        {
            SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                            TypeManager.getInstance()
                                            .getComposedType(TriggerTask.class)
                                            .getCode() + "} WHERE {" +
                            TypeManager.getInstance()
                                            .getComposedType(Trigger.class)
                                            .getCode() + "} = " + trigger.getPK(), TriggerTask.class);
            TriggerTask triggerTask = CollectionUtils.isEmpty(res.getResult()) ? null : res.getResult().get(0);
            if(attributeSetter != null)
            {
                attributeSetter.close();
            }
            return triggerTask;
        }
        catch(Throwable throwable)
        {
            if(attributeSetter != null)
            {
                try
                {
                    attributeSetter.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static void startTimerTask()
    {
        TimerTaskUtils.getInstance().startTimerTask();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static void stopTimerTask()
    {
        try
        {
            TimerTaskUtils.getInstance().stopTimerTask();
        }
        catch(de.hybris.platform.util.SingletonCreator.SingletonCreateException e)
        {
            if(!RedeployUtilities.isShutdownInProgress())
            {
                throw e;
            }
        }
    }


    public void notifyInitializationStart(Map<String, String> params, JspContext ctx) throws Exception
    {
        super.notifyInitializationStart(params, ctx);
        stopTaskEngine();
        stopConjobEngine();
    }


    public void notifyInitializationEnd(Map params, JspContext ctx) throws Exception
    {
        super.notifyInitializationEnd(params, ctx);
        List<Trigger> triggersWithoutTask = getSession().getFlexibleSearch().search("SELECT {pk} FROM {Trigger AS tr} WHERE NOT EXISTS({{SELECT {tt.PK} FROM {TriggerTask as tt} WHERE {tr.PK} = {tt.trigger}}})", Trigger.class).getResult();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Found " + triggersWithoutTask.size() + " triggers without task item assigned. ");
            if(triggersWithoutTask.size() > 0)
            {
                LOG.debug("Creating missing tasks for triggers...");
            }
        }
        for(Trigger trigger : triggersWithoutTask)
        {
            createTriggerTask(prepareTriggerTaskAttributes(trigger));
            if(LOG.isDebugEnabled())
            {
                LOG.debug("TriggerTask created for trigger: " + trigger);
            }
        }
        startTaskEngine();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public MoveMediaJob getOrCreateMoveMediaJob()
    {
        MoveMediaJob job = (MoveMediaJob)getInstance().getJob("Move-Media");
        if(job == null)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("code", "Move-Media");
            job = createMoveMediaJob(map);
        }
        return job;
    }


    public void afterItemCreation(SessionContext ctx, ComposedType type, Item createdItem, Item.ItemAttributeMap attributes) throws JaloBusinessException
    {
        if(createdItem instanceof Task)
        {
            Date executeAt = ((Task)createdItem).getExecutionDate();
            if(executeAt == null)
            {
                LOG.debug("Skipping repoll event. Execution date not set.");
            }
            else if(executeAt.getTime() > System.currentTimeMillis())
            {
                LOG.debug("Skipping repoll event. Execution date in future.");
            }
            else
            {
                TaskEngine engine = getTaskEngine();
                if(engine != null)
                {
                    Task task = (Task)createdItem;
                    engine.triggerRepoll(task.getNodeId(), task.getNodeGroup());
                }
            }
        }
        if(createdItem instanceof Trigger)
        {
            if(findTaskForTrigger((Trigger)createdItem) == null)
            {
                createTriggerTask(prepareTriggerTaskAttributes((Trigger)createdItem));
            }
        }
    }


    protected void startTaskEngine()
    {
        TaskEngine engine = getTaskEngine();
        if(engine != null && engine.isAllowedToStart())
        {
            engine.start();
        }
        else
        {
            LOG.error("cannot start task engine since task service does not provide engine handle");
        }
    }


    protected void stopTaskEngine()
    {
        TaskEngine engine = getTaskEngine();
        if(engine != null)
        {
            engine.stop();
        }
        else
        {
            LOG.error("cannot stop task engine since task service does not provide engine handle");
        }
    }


    protected TaskEngine getTaskEngine()
    {
        ApplicationContext applicationContext = Registry.getCoreApplicationContext();
        TaskService taskService = null;
        try
        {
            taskService = (TaskService)applicationContext.getBean("taskService");
        }
        catch(NoSuchBeanDefinitionException e)
        {
            LOG.warn("Couldn't find bean + taskService. ApplicationContext already/still down?");
        }
        if(taskService != null)
        {
            return taskService.getEngine();
        }
        return null;
    }


    protected StrandedItemsRegistry createStrandedCronJobsRegistry()
    {
        return new StrandedItemsRegistry((StrandedItemsRegistry.StrandedItemsResolutionHandler)new DefaultStrandedCronJobsResolutionHandler(this));
    }


    public StrandedItemsRegistry getStrandedCronJobsRegistry()
    {
        return this.strandedCronJobsRegistry.get();
    }
}
