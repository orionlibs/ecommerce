package de.hybris.platform.task.impl;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import de.hybris.platform.cluster.DefaultClusterNodeManagementService;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.InvalidTaskStateError;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.Task;
import de.hybris.platform.task.TaskCondition;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskEngine;
import de.hybris.platform.task.TaskEvent;
import de.hybris.platform.task.TaskInterruptedException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.TaskTimeoutException;
import de.hybris.platform.task.constants.GeneratedTaskConstants;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.retry.support.RetryTemplate;

public class DefaultTaskService implements TaskService
{
    private static final Logger LOG = Logger.getLogger(DefaultTaskService.class.getName());
    public static final int DEFAULT_WORKER_MAX = 10;
    public static final int DEFAULT_POLLING_INTERVAL = 30;
    public static final int DEFAULT_SHUTDOWN_WAIT = 10;
    public static final String QUEUE_SIZE_METRIC = MetricRegistry.name("queue", new String[] {"size"});
    public static final String PROCESSING_TIME_METRIC = MetricRegistry.name("execution", new String[] {"time"});
    public static final String PROCESSING_HITS_METRIC = MetricRegistry.name("execution", new String[] {"hits"});
    public static final String PROCESSING_MISSES_METRIC = MetricRegistry.name("execution", new String[] {"misses"});
    public static final String TASKS_QUEUE_SIZE_METRIC = MetricRegistry.name("pooling.queue.tasks.size", new String[0]);
    public static final String CONDITIONS_QUEUE_SIZE_METRIC = MetricRegistry.name("pooling.queue.conditions.size", new String[0]);
    public static final String POOLING_QUEUE_SIZE_METRIC = MetricRegistry.name("pooling.queue.size", new String[0]);
    private static final String EXCLUSIVE_MODE_ENABLED = "task.engine.exclusive.mode";
    private static final String POLLING_STARTUP_DELAY_ENABLED = "task.polling.startup.delay.enabled";
    private volatile TaskEngineRunningState runningState = null;
    private final AtomicReference<Runnable> startOrStopAction = new AtomicReference<>();
    private final int shutdownWait;
    private final Tenant tenant;
    private final RepollTask repollTask = new RepollTask(this);
    private final RuntimeConfigHolder runtimeConfigHolder = new RuntimeConfigHolder();
    private TaskDAO dao;
    private EventService eventService;
    private ModelService modelService;
    private RetryTemplate taskEngineRetryTemplate;
    private Map<Class<? extends TaskRunner>, TaskExecutionStrategy> taskExecutionStrategies;
    private ScheduleAndTriggerStrategy scheduleAndTriggerStrategy;
    private MetricRegistry metricRegistry;
    private TasksProvider tasksProvider;
    private final TaskEngine engine = (TaskEngine)new Object(this);


    public DefaultTaskService()
    {
        this.tenant = Registry.getCurrentTenantNoFallback();
        if(this.tenant == null)
        {
            throw new IllegalStateException("cannot create task service without tenant");
        }
        this.shutdownWait = readShutdownWaitFromConfig();
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setEventService(EventService eventService)
    {
        this.eventService = eventService;
    }


    @Required
    public void setScheduleAndTriggerStrategy(ScheduleAndTriggerStrategy scheduleAndTriggerStrategy)
    {
        this.scheduleAndTriggerStrategy = scheduleAndTriggerStrategy;
    }


    @Required
    public void setMetricRegistry(MetricRegistry metricRegistry)
    {
        this.metricRegistry = metricRegistry;
    }


    @Required
    public void setTasksProvider(TasksProvider tasksProvider)
    {
        this.tasksProvider = tasksProvider;
    }


    @Required
    public void setTaskEngineRetryTemplate(RetryTemplate taskEngineRetryTemplate)
    {
        this.taskEngineRetryTemplate = taskEngineRetryTemplate;
    }


    public TaskEngine getEngine()
    {
        return this.engine;
    }


    public boolean isRunning()
    {
        return (this.runningState != null && this.startOrStopAction.get() == null);
    }


    public void triggerRepoll(Integer nodeId, String nodeGroupId)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Publishing repoll event.");
        }
        this.eventService.publishEvent((AbstractEvent)new RepollEvent(nodeId, nodeGroupId));
    }


    public void triggerEvent(String uniqueId)
    {
        triggerEvent(TaskEvent.newEvent(uniqueId));
    }


    public void triggerEvent(String uniqueId, Date expirationDate)
    {
        TaskEvent event = TaskEvent.builder(uniqueId).withExpirationDate(expirationDate).build();
        triggerEvent(event);
    }


    public boolean triggerEvent(TaskEvent event)
    {
        boolean triggered = this.scheduleAndTriggerStrategy.triggerEvent(event);
        if(triggered)
        {
            triggerRepoll(null, null);
        }
        return triggered;
    }


    public void scheduleTask(TaskModel task)
    {
        adjustNodeAndNodeGroupAssignment(task);
        checkTask(task);
        Date scheduledDate = task.getExecutionDate();
        Integer nodeID = task.getNodeId();
        String nodeGroupId = task.getNodeGroup();
        this.scheduleAndTriggerStrategy.scheduleTask(task);
        triggerRepollAfterSchedule(scheduledDate, nodeID, nodeGroupId);
    }


    private void adjustNodeAndNodeGroupAssignment(TaskModel task)
    {
        if(task.getNodeId() != null && task.getNodeGroup() != null)
        {
            task.setNodeGroup(null);
        }
    }


    protected void triggerRepollAfterSchedule(Date scheduledDate, Integer nodeId, String nodeGroupId)
    {
        if(scheduledDate != null && scheduledDate.getTime() > System.currentTimeMillis())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Skipping repoll event. Execution date in future.");
            }
        }
        else
        {
            triggerRepoll(nodeId, nodeGroupId);
        }
    }


    protected void checkTask(TaskModel toBeScheduled)
    {
        if(toBeScheduled.getRunnerBean() == null || toBeScheduled.getRunnerBean().trim().length() == 0)
        {
            throw new IllegalArgumentException("Task " + toBeScheduled + " has no runner bean");
        }
        Date scheduled = (toBeScheduled.getExecutionDate() != null) ? toBeScheduled.getExecutionDate() : new Date();
        Date expires = toBeScheduled.getExpirationDate();
        if(expires != null && expires.before(scheduled))
        {
            throw new IllegalArgumentException("Task " + toBeScheduled + " has expiration date < execution date");
        }
        Set<TaskConditionModel> conditions = toBeScheduled.getConditions();
        if(conditions != null && !conditions.isEmpty())
        {
            for(TaskConditionModel cond : conditions)
            {
                if(cond != null)
                {
                    checkTaskConditions(toBeScheduled, cond);
                }
            }
        }
    }


    protected void checkTaskConditions(TaskModel toBeScheduled, TaskConditionModel cond)
    {
        if(cond.getTask() == null)
        {
            cond.setTask(toBeScheduled);
        }
        if(cond.getUniqueID() == null || cond.getUniqueID().trim().length() == 0)
        {
            throw new IllegalArgumentException("Task condition " + cond + " from " + toBeScheduled + " has no ID");
        }
        Date scheduled = (toBeScheduled.getExecutionDate() != null) ? toBeScheduled.getExecutionDate() : new Date();
        Date expires = cond.getExpirationDate();
        if(expires != null && expires.before(scheduled))
        {
            throw new IllegalArgumentException("Task condition " + cond + " from " + toBeScheduled + " has expiration date <  task execution date");
        }
    }


    public void repoll(Integer nodeId, String nodeGroupId)
    {
        if(isRunning())
        {
            if(nodeId == null && nodeGroupId == null && getTaskProviderParameters().isExclusiveMode())
            {
                return;
            }
            if(nodeId != null && nodeId.intValue() != getClusterNodeID())
            {
                return;
            }
            if(nodeGroupId != null && !getClusterGroupsForThisNode().contains(nodeGroupId))
            {
                return;
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Received repoll event.");
            }
            this.repollTask.schedule(this.runningState.executor);
        }
    }


    protected boolean isAllowedToStart()
    {
        return isAllowedToStart(getClusterNodeID());
    }


    protected boolean isAllowedToStart(int clusterId)
    {
        if(!isEnabledInConfig())
        {
            return false;
        }
        String clusterIdStr = Integer.toString(clusterId);
        String excluded = getTenant().getConfig().getParameter("task.excluded.cluster.ids");
        if(excluded != null && excluded.length() > 0)
        {
            if(DefaultClusterNodeManagementService.getInstance().isAutoDiscoveryEnabled())
            {
                LOG.warn("Autodiscovery cluster nodes feature is enabled - the property task.excluded.cluster.ids=" + excluded + " will be ignored. You should disable the task engine per node only via task.engine.loadonstartup=false.");
                return true;
            }
            for(String idStr : excluded.split("[,; ]"))
            {
                if(idStr != null && idStr.trim().equals(clusterIdStr))
                {
                    return false;
                }
            }
        }
        return true;
    }


    protected boolean isEnabledInConfig()
    {
        String newParameterValue = getTenant().getConfig().getParameter("task.engine.loadonstartup");
        String oldParameterValue = getTenant().getConfig().getParameter("task.processing.enabled");
        if(oldParameterValue != null && newParameterValue != null)
        {
            LOG.warn(String.format("Parameter %s (%s) overrides parameter %s (%s)", new Object[] {"task.engine.loadonstartup", newParameterValue, "task.processing.enabled", oldParameterValue}));
        }
        if(newParameterValue != null)
        {
            return getTenant().getConfig().getBoolean("task.engine.loadonstartup", true);
        }
        return getTenant().getConfig().getBoolean("task.processing.enabled", true);
    }


    private boolean checkSystemOKAndLogMsgIfVerificationFails(String logAdditionalMsg)
    {
        boolean isSystemOK = checkSystemOK();
        if(!isSystemOK)
        {
            LOG.info(logAdditionalMsg);
        }
        return isSystemOK;
    }


    protected boolean checkSystemOK()
    {
        if(isTaskEngineNotReadyToStart())
        {
            return false;
        }
        ComposedType taskType = null;
        try
        {
            taskType = TypeManager.getInstance().getComposedType(Task.class);
        }
        catch(JaloItemNotFoundException e)
        {
            LOG.warn("System needs to be updated in order to use task module - cannot find Task type.");
            return false;
        }
        try
        {
            taskType.getAttributeDescriptorIncludingPrivate("failed");
        }
        catch(JaloItemNotFoundException e)
        {
            LOG.warn("System needs to be updated in order to use task module - cannot find Task.failed attribute.");
            return false;
        }
        return true;
    }


    private boolean isTaskEngineNotReadyToStart()
    {
        if(!JaloConnection.getInstance().isSystemInitialized())
        {
            LOG.info("System not initialized or connection to DB cannot be establish.");
            return true;
        }
        if(RedeployUtilities.isShutdownInProgress())
        {
            LOG.info("System about to shutdown. Not starting task module.");
            return true;
        }
        return false;
    }


    protected <T> void refreshCurrentSessionWithRetry(Supplier<Void> failingMethodSupplier)
    {
        this.taskEngineRetryTemplate.execute(retryContext -> (Void)failingMethodSupplier.get());
    }


    public void init()
    {
        waitAndRunExclusiveAction("init", (Runnable)new Object(this));
    }


    protected TaskDAO getTaskDao()
    {
        return this.dao;
    }


    @Required
    public void setTaskDao(TaskDAO dao)
    {
        this.dao = dao;
    }


    int getMinWorkers()
    {
        return (this.runningState != null && this.runningState.executor != null) ? (
                        (ThreadPoolExecutor)this.runningState.executor).getCorePoolSize() :
                        -1;
    }


    int getMaxWorkers()
    {
        return (this.runningState != null && this.runningState.executor != null) ? (
                        (ThreadPoolExecutor)this.runningState.executor).getMaximumPoolSize() :
                        -1;
    }


    int getWorkerIdleTime()
    {
        return
                        (int)((this.runningState != null && this.runningState.executor != null) ? ((ThreadPoolExecutor)this.runningState.executor).getKeepAliveTime(TimeUnit.SECONDS) : -1L);
    }


    long getPollingInterval()
    {
        return (this.runningState != null && this.runningState.pollingThread != null) ? this.runningState.pollingThread.interval : -1L;
    }


    protected int readPollingIntervalFromConfig()
    {
        ConfigIntf cfg = getTenant().getConfig();
        int tmp = cfg.getInt("task.polling.interval", 30);
        if(tmp <= 0)
        {
            LOG.warn("invalid polling interval " + tmp + " - using default 30 sec instead");
            return 30;
        }
        return tmp;
    }


    protected int readMaxWorkersFromConfig()
    {
        ConfigIntf cfg = getTenant().getConfig();
        int wMax = cfg.getInt("task.workers.max", 10);
        if(wMax <= 0)
        {
            LOG.warn("invalid maximum worker number " + wMax + " - using 10 instead");
            return 10;
        }
        return wMax;
    }


    protected int readWorkersIdleFromConfig()
    {
        ConfigIntf cfg = getTenant().getConfig();
        int pollingInterval = readPollingIntervalFromConfig();
        int idle = cfg.getInt("task.workers.idle", 2 * pollingInterval);
        if(idle < 0)
        {
            LOG.warn("invalid worker idle time " + idle + " - using " + 2 * pollingInterval + " instead");
            idle = 2 * pollingInterval;
        }
        return idle;
    }


    protected Set<Integer> readExcludedNodesFromConfig()
    {
        Set<Integer> ret = null;
        String excluded = getTenant().getConfig().getParameter("task.excluded.cluster.ids");
        if(excluded != null && excluded.length() > 0)
        {
            for(String idStr : excluded.split("[,; ]"))
            {
                if(idStr != null)
                {
                    try
                    {
                        Integer id = Integer.valueOf(idStr);
                        if(ret == null)
                        {
                            ret = new LinkedHashSet<>();
                        }
                        ret.add(id);
                    }
                    catch(NumberFormatException e)
                    {
                        LOG.warn("invalid excluded cluster node id '" + idStr + "' found - please check your task.excluded.cluster.ids settings");
                    }
                }
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    protected int readShutdownWaitFromConfig()
    {
        ConfigIntf cfg = getTenant().getConfig();
        int shutdownWait = 10;
        try
        {
            shutdownWait = cfg.getInt("tasks.shutdown.wait", 10);
        }
        catch(NumberFormatException e)
        {
            LOG.warn("Wrong number value for tasks.shutdown.wait set in configuration. Default value =10 will be used");
        }
        return shutdownWait;
    }


    ExecutorService createExecutor()
    {
        int workers = readMaxWorkersFromConfig();
        ThreadPoolExecutor ret = new ThreadPoolExecutor(workers, workers, readWorkersIdleFromConfig(), TimeUnit.SECONDS, new LinkedBlockingQueue<>(), (ThreadFactory)new Object(this));
        ret.allowCoreThreadTimeOut(true);
        return ret;
    }


    public void destroy()
    {
        waitAndRunExclusiveAction("destroy", (Runnable)new Object(this));
    }


    protected void waitAndRunExclusiveAction(String info, Runnable action)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Thread " + Thread.currentThread() + " -> waitAndRunExclusiveAction(" + info + ")");
        }
        int waitSeconds = 30;
        long maxWaitUntil = System.currentTimeMillis() + 30000L;
        boolean assigned = false;
        try
        {
            while(!(assigned = this.startOrStopAction.compareAndSet(null, action)))
            {
                try
                {
                    Thread.sleep(100L);
                }
                catch(InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                    LOG.warn("Interrupted while waiting to run start/stop action " + info + " on " + this);
                    return;
                }
                if(System.currentTimeMillis() > maxWaitUntil)
                {
                    throw new IllegalStateException("Couldn't run exclusive start/stop action " + info + " on task service for 30 seconds - aborting");
                }
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Thread " + Thread.currentThread() + " -> runs " + info);
            }
            action.run();
        }
        finally
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Thread " + Thread.currentThread() + " -> done " + info);
            }
            if(assigned)
            {
                this.startOrStopAction.set(null);
            }
        }
    }


    int getClusterNodeID()
    {
        return MasterTenant.getInstance().getClusterID();
    }


    Collection<String> getClusterGroupsForThisNode()
    {
        return Registry.getClusterGroups();
    }


    protected void poll()
    {
        if(!isRunning() || !checkSystemOKAndLogMsgIfVerificationFails("Polling is paused") || this.repollTask.isScheduled())
        {
            return;
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Polling tasks.");
        }
        TaskEngineParameters params = getTaskProviderParameters();
        List<TasksProvider.VersionPK> tasksToSchedule = getTasksToSchedule(params);
        scheduleItemsAndRepollTaskIfNeeded(params.getMaxItemsToSchedule(), tasksToSchedule);
    }


    private void scheduleItemsAndRepollTaskIfNeeded(int maxItemsToSchedule, List<TasksProvider.VersionPK> items)
    {
        if(items == null || items.isEmpty())
        {
            return;
        }
        if(items.size() < maxItemsToSchedule)
        {
            scheduleItems(items);
        }
        else
        {
            scheduleItems(items.subList(0, maxItemsToSchedule / 2));
            repoll(null, null);
            scheduleItems(items.subList(maxItemsToSchedule / 2, maxItemsToSchedule));
        }
    }


    private List<TasksProvider.VersionPK> getTasksToSchedule(TaskEngineParameters params)
    {
        return this.tasksProvider.getTasksToSchedule(this.runtimeConfigHolder, params, params.getMaxItemsToSchedule());
    }


    protected TaskEngineParameters getTaskProviderParameters()
    {
        return (new TaskEngineParameters.ParametersBuilder()).withMaxThreads(this.runningState.getMaxThreads())
                        .withClusterNodeID(getClusterNodeID()).withTenant(getTenant())
                        .withProcessTriggerTask(
                                        Config.getBoolean("cronjob.timertask.loadonstartup", true))
                        .withExclusiveMode(Config.getBoolean("task.engine.exclusive.mode", false))
                        .withClusterGroupsForThisNode(getClusterGroupsForThisNode())
                        .withMaxItemsToSchedule(this.tasksProvider.getMaxItemsToSchedule(this.runningState, this.runtimeConfigHolder))
                        .build();
    }


    private void scheduleItems(List<TasksProvider.VersionPK> items)
    {
        for(TasksProvider.VersionPK pk : items)
        {
            scheduleTaskForExecution(pk);
        }
    }


    void scheduleTaskForExecution(TasksProvider.VersionPK versionedPK)
    {
        this.runningState.executor.execute((Runnable)new Object(this, versionedPK));
        this.metricRegistry.counter(RuntimeConfigHolder.metricName(QUEUE_SIZE_METRIC)).inc();
    }


    private Object prepareWorker(Thread t, TasksProvider.VersionPK versionPK)
    {
        ClassLoader ctxCl = t.getContextClassLoader();
        return ctxCl;
    }


    private void resetWorker(Thread t, TasksProvider.VersionPK versionPK, Object prepareCtx)
    {
        this.modelService.detachAll();
        t.setContextClassLoader((ClassLoader)prepareCtx);
    }


    private void handleErrorsOnTasksMarkedRunning()
    {
        List<Task> rs;
        if(!getTenant().getJaloConnection().isSystemInitialized())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("System is not initialized. Skipping task recovery.");
            }
            return;
        }
        int nodeId = getClusterNodeID();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Searching for task marked running on nodeId " + nodeId + " (after system crash?).");
        }
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put("disableCache", Boolean.valueOf(true));
        SessionContext.SessionContextAttributeSetter attributeSetter = FlexibleSearch.getInstance().getSession().getSessionContext().setSessionContextAttributesLocally(sessionAttributes);
        try
        {
            rs = FlexibleSearch.getInstance().search("select {" + Item.PK + "} from {" + GeneratedTaskConstants.TC.TASK + "} where {runningOnClusterNode} = ?nodeId", Collections.singletonMap("nodeId", Integer.valueOf(nodeId)), Task.class).getResult();
            if(attributeSetter != null)
            {
                attributeSetter.close();
            }
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
        for(Task task : rs)
        {
            try
            {
                LOG.error("Calling error handler of task marked running on node " + nodeId + ": " + task.getPK());
                TaskRunner<TaskModel> runner = getRunner(task);
                TaskExecutionStrategy executionStrategy = getExecutionStrategy(runner);
                TaskModel model = (TaskModel)this.modelService.get(task);
                Throwable error = executionStrategy.handleError(this, runner, (TaskModel)this.modelService.get(task), (Throwable)new TaskInterruptedException("Node has been shut down while the task was running."));
                if(error instanceof RetryLaterException)
                {
                    if(!scheduleRetryIfAllowed(task, (RetryLaterException)error))
                    {
                        int currentRetry = task.getRetryAsPrimitive();
                        markTaskFailed(task);
                        Throwable e = executionStrategy.handleError(this, runner, model, (Throwable)new InvalidTaskStateError("no more retries allowed for task " + task + " after " + currentRetry + " retries"));
                        executionStrategy.finished(this, runner, model, e);
                        this.tasksProvider.afterTaskFinished(task.getPK(), this.runtimeConfigHolder);
                    }
                    getTaskDao().unlock(task.getPK().getLong());
                    this.tasksProvider.afterTaskUnlocked(task.getPK(), this.runtimeConfigHolder);
                    continue;
                }
                markTaskFailed(task);
            }
            catch(IllegalStateException e)
            {
                LOG.error("Failed to call error handler of task '" + task + "' marked running on node " + nodeId + " on startup.", e);
            }
        }
    }


    private Item getScheduledItem(TasksProvider.VersionPK versionPK)
    {
        try
        {
            Item item = JaloSession.getCurrentSession().getItem(versionPK.pk);
            if(item.getPersistenceVersion() != versionPK.version)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Task #" + versionPK.pk + " changed since querying so it may have been processed already. Skipping it.");
                }
                return null;
            }
            return item;
        }
        catch(JaloItemNotFoundException | de.hybris.platform.util.jeeapi.YNoSuchEntityException | de.hybris.platform.jalo.JaloObjectNoLongerValidException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("scheduled item " + versionPK.pk + " does not exist any more. Skipping it.");
            }
            return null;
        }
    }


    protected void processInTenant(TasksProvider.VersionPK versionPK)
    {
        try
        {
            Registry.setCurrentTenant(getTenant());
            Timer.Context processingTimeMetricCtx = this.metricRegistry.timer(RuntimeConfigHolder.metricName(PROCESSING_TIME_METRIC)).time();
            try
            {
                process(versionPK);
            }
            finally
            {
                processingTimeMetricCtx.stop();
                this.metricRegistry.counter(RuntimeConfigHolder.metricName(QUEUE_SIZE_METRIC)).dec();
                if(JaloSession.hasCurrentSession())
                {
                    JaloSession.getCurrentSession().close();
                }
                JaloSession.deactivate();
            }
        }
        finally
        {
            Registry.unsetCurrentTenant();
        }
    }


    protected void process(TasksProvider.VersionPK versionPK)
    {
        Item item = getScheduledItem(versionPK);
        if(item == null)
        {
            this.metricRegistry.meter(RuntimeConfigHolder.metricName(PROCESSING_MISSES_METRIC)).mark();
            return;
        }
        adjustCurrentThreadName(item);
        if(item instanceof Task)
        {
            processTask((Task)item, versionPK);
        }
        else if(item instanceof TaskCondition)
        {
            processCondition((TaskCondition)item);
        }
        else
        {
            LOG.error("unexpected item in DefaultTaskManager.execute : " + item + ", class = " + item.getClass());
        }
    }


    private void adjustCurrentThreadName(Item item)
    {
        StringBuilder newName = new StringBuilder(Thread.currentThread().getName());
        if(newName.toString().endsWith("]"))
        {
            int lastSeparatorIdx = newName.toString().lastIndexOf("-");
            newName.setLength(lastSeparatorIdx);
        }
        newName.append("-").append(item.getClass().getSimpleName()).append(" [").append(item.getPK()).append("]");
        Thread.currentThread().setName(newName.toString());
    }


    protected void processCondition(TaskCondition cond)
    {
        try
        {
            if(cond.isAlive() && cond.getTask() == null)
            {
                this.metricRegistry.meter(RuntimeConfigHolder.metricName(PROCESSING_HITS_METRIC)).mark();
                LOG.warn("removing timed out task condition " + cond + " - no task has been matching yet");
                cond.remove();
            }
            else
            {
                this.metricRegistry.meter(RuntimeConfigHolder.metricName(PROCESSING_MISSES_METRIC)).mark();
            }
        }
        catch(Exception e)
        {
            this.metricRegistry.meter(RuntimeConfigHolder.metricName(PROCESSING_MISSES_METRIC)).mark();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("error removing timed out condition : " + e.getMessage(), e);
            }
        }
    }


    protected void processTask(Task task, TasksProvider.VersionPK versionPK)
    {
        Timer.Context lockTimer = this.metricRegistry.timer(RuntimeConfigHolder.metricName(MetricRegistry.name("execution", new String[] {"lock", "time"}))).time();
        boolean lock = getTaskDao().lock(versionPK.pk.getLong(), versionPK.version);
        lockTimer.stop();
        if(!lock)
        {
            this.metricRegistry.meter(RuntimeConfigHolder.metricName(PROCESSING_MISSES_METRIC)).mark();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Failed to retrieve lock on task " + versionPK + ". Skipping it.");
            }
            return;
        }
        try
        {
            this.metricRegistry.meter(RuntimeConfigHolder.metricName(PROCESSING_HITS_METRIC)).mark();
            TaskModel model = (TaskModel)this.modelService.get(task);
            if(BooleanUtils.isTrue(model.getFailed()))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Skip processing of failed task " + versionPK + ".");
                }
                return;
            }
            TaskRunner<TaskModel> runner = getRunner(task);
            TaskExecutionStrategy executionStrategy = getExecutionStrategy(runner);
            Throwable error = null;
            boolean finished = false;
            try
            {
                Date expirationDate = task.getExpirationDate();
                if(expirationDate != null && System.currentTimeMillis() > expirationDate.getTime())
                {
                    error = executionStrategy.handleError(this, runner, model, (Throwable)new TaskTimeoutException("task " + task + " timed out", expirationDate));
                    markTaskFailed(task);
                    finished = true;
                }
                else
                {
                    executionStrategy.run(this, runner, model);
                    finished = true;
                }
            }
            catch(RetryLaterException e)
            {
                if(!scheduleRetryIfAllowed(task, e))
                {
                    int currentRetry = task.getRetryAsPrimitive();
                    markTaskFailed(task);
                    error = executionStrategy.handleError(this, runner, model, (Throwable)new InvalidTaskStateError("no more retries allowed for task " + task + " after " + currentRetry + " retries"));
                    finished = true;
                }
            }
            catch(Exception e)
            {
                logExceptionInProcessing(versionPK, e);
                if(task.isAlive())
                {
                    markTaskFailed(task);
                }
                error = executionStrategy.handleError(this, runner, model, e);
                finished = true;
            }
            finally
            {
                if(finished)
                {
                    Timer.Context finishTimer = this.metricRegistry.timer(RuntimeConfigHolder.metricName(MetricRegistry.name("execution", new String[] {"finished", "time"}))).time();
                    executionStrategy.finished(this, runner, model, error);
                    this.tasksProvider.afterTaskFinished(model.getPk(), this.runtimeConfigHolder);
                    finishTimer.stop();
                }
            }
        }
        finally
        {
            if(task.isAlive())
            {
                getTaskDao().unlock(versionPK.pk.getLong());
                this.tasksProvider.afterTaskUnlocked(versionPK.pk, this.runtimeConfigHolder);
            }
        }
    }


    private void logExceptionInProcessing(TasksProvider.VersionPK versionPK, Exception e)
    {
        String message = "Failed to execute task " + versionPK + ".";
        if(this.runningState.isShuttingDown.get())
        {
            LOG.warn(message + " Task engine is shutting down");
            LOG.debug(message, e);
        }
        else
        {
            LOG.error(message, e);
        }
    }


    protected boolean scheduleRetryIfAllowed(Task task, RetryLaterException e)
    {
        TaskModel model = (TaskModel)this.modelService.get(task);
        int currentRetry = task.getRetryAsPrimitive();
        TaskRunner<TaskModel> runner = getRunner(task);
        TaskExecutionStrategy executionStrategy = getExecutionStrategy(runner);
        Date allowedRetry = executionStrategy.handleRetry(this, runner, model, e, currentRetry);
        if(allowedRetry != null)
        {
            scheduleRetry(task, allowedRetry, currentRetry + 1);
            return true;
        }
        return false;
    }


    protected void scheduleRetry(Task task, Date nextExecution, int turn)
    {
        task.setExecutionDate(nextExecution);
        task.setProperty("retry", Integer.valueOf(turn));
    }


    protected void markTaskFailed(Task task)
    {
        task.setProperty("failed", Boolean.TRUE);
    }


    protected TaskRunner getRunner(Task task)
    {
        return getRunner(task.getRunnerBean());
    }


    protected TaskRunner getRunner(String runnerBean) throws IllegalStateException
    {
        try
        {
            TaskRunner ret = (TaskRunner)Registry.getCoreApplicationContext().getBean(runnerBean, TaskRunner.class);
            if(ret == null)
            {
                throw new IllegalStateException("Unknown task runner bean id '" + runnerBean + "' !");
            }
            return ret;
        }
        catch(NoSuchBeanDefinitionException e)
        {
            throw new IllegalStateException("Unknown task runner bean id '" + runnerBean + "' !");
        }
        catch(BeanNotOfRequiredTypeException e)
        {
            throw new IllegalStateException("Invalid type of task runner bean '" + runnerBean + "'' - expected " + e
                            .getRequiredType() + " but got " + e.getActualType());
        }
        catch(Exception e)
        {
            LOG.error("error getting task runner bean", e);
            throw new IllegalStateException("error getting task runner bean '" + runnerBean + "' : " + e.getMessage());
        }
    }


    protected Tenant getTenant()
    {
        return this.tenant;
    }


    protected TaskExecutionStrategy getExecutionStrategy(TaskRunner<? extends TaskModel> runner)
    {
        TaskExecutionStrategy strategy = this.taskExecutionStrategies.get(runner.getClass());
        if(strategy == null)
        {
            strategy = this.taskExecutionStrategies.get(TaskRunner.class);
        }
        if(strategy == null)
        {
            throw new IllegalStateException("No TaskExecutionStrategy found for runner of class " + runner.getClass());
        }
        return strategy;
    }


    @Autowired
    public void setTaskExecutionStrategies(Collection<? extends TaskExecutionStrategy> taskExecutionStrategies)
    {
        this.taskExecutionStrategies = new HashMap<>();
        for(TaskExecutionStrategy executionStrategy : taskExecutionStrategies)
        {
            this.taskExecutionStrategies.put(executionStrategy.getRunnerClass(), executionStrategy);
        }
    }
}
