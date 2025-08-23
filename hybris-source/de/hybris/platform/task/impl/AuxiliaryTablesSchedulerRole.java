package de.hybris.platform.task.impl;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.base.Suppliers;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.task.impl.gateways.SchedulerState;
import de.hybris.platform.task.impl.gateways.TasksQueueGateway;
import de.hybris.platform.task.impl.gateways.WorkerStateGateway;
import de.hybris.platform.util.MessageFormatUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class AuxiliaryTablesSchedulerRole implements InitializingBean
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AuxiliaryTablesSchedulerRole.class);
    private Instant timestamp = Instant.now();
    private AuxiliaryTablesGatewayFactory gatewayFactory;
    private MetricRegistry metricRegistry;
    private TypeService typeService;
    private Timer activateWorkersTimer;
    private Timer deactivateWorkersTimer;
    private Timer lockTimer;
    private Timer schedulerTimer;
    private Timer copyTasksTimer;
    private Timer countTasksTimer;
    private WorkerHelper workerHelper;
    private final Supplier<String> conditionsQuerySupplier = (Supplier<String>)Suppliers.memoize(() -> MessageFormatUtils.format("SELECT PK, hjmpTS FROM {0} WHERE {1} < ? AND {2} IS NULL AND ({3} = 0 OR {3} IS NULL)",
                    new Object[] {getTableName("TaskCondition"), getColumnName("TaskCondition", "expirationTimeMillis"), getColumnName("TaskCondition", "task"), getColumnName("TaskCondition", "fulfilled")}));
    private final Supplier<String> triggerTaskExclusionSQLConditionSupplier = (Supplier<String>)Suppliers.memoize(
                    () -> MessageFormatUtils.format(" AND {0} NOT IN ({1})", new Object[] {"TypePkString", getPksOfTriggerTaskTypeAndSubtypes().stream().map(PK::getLongValueAsString).collect(Collectors.joining(","))}));


    public Collection<TasksProvider.VersionPK> tryToPerformSchedulerJob(RuntimeConfigHolder runtimeConfigHolder, TaskEngineParameters taskEngineParameters, int maxItemsToSchedule)
    {
        int nodeId = taskEngineParameters.getClusterNodeID();
        Duration lockingTryMinInterval = (Duration)runtimeConfigHolder.getProperty((RuntimeConfigHolder.TaskEngineProperty)AuxiliaryTablesBasedTaskProvider.Params.SCHEDULER_INTERVAL);
        boolean isScheduler = lockSchedulerRole(nodeId, lockingTryMinInterval);
        if(isScheduler)
        {
            LOGGER.debug("{} (scheduler): lock acquired - performing scheduler's jobs", Integer.valueOf(nodeId));
            Timer.Context timer = this.schedulerTimer.time();
            List<WorkerStateGateway.WorkerState> workers = this.gatewayFactory.getWorkerStateGateway().getWorkers();
            List<WorkerStateGateway.WorkerState> activeWorkers = activateWorkers(workers, nodeId, (Duration)runtimeConfigHolder
                            .getProperty((RuntimeConfigHolder.TaskEngineProperty)AuxiliaryTablesBasedTaskProvider.Params.WORKER_ACTIVATION_INTERVAL), runtimeConfigHolder);
            deactivateWorkers(workers, nodeId, (Duration)runtimeConfigHolder.getProperty((RuntimeConfigHolder.TaskEngineProperty)AuxiliaryTablesBasedTaskProvider.Params.WORKER_DEACTIVATION_INTERVAL));
            cleanLockedTasks((Duration)runtimeConfigHolder.getProperty((RuntimeConfigHolder.TaskEngineProperty)AuxiliaryTablesBasedTaskProvider.Params.WORKER_REMOVAL_INTERVAL));
            this.gatewayFactory.getTasksQueueGateway()
                            .clean((Duration)runtimeConfigHolder.getProperty((RuntimeConfigHolder.TaskEngineProperty)AuxiliaryTablesBasedTaskProvider.Params.SCHEDULER_CLEAN_QUEUE_OLD_TASKS_THRESHOLD));
            Collection<TasksProvider.VersionPK> conditions = copyTasksToAuxiliaryTable(maxItemsToSchedule, activeWorkers, taskEngineParameters, runtimeConfigHolder);
            long time = timer.stop();
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("{} (scheduler): done performing scheduler's jobs, it took {}", Integer.valueOf(nodeId),
                                DurationFormatUtils.formatDurationHMS(TimeUnit.NANOSECONDS.toMillis(time)));
            }
            return conditions;
        }
        return Collections.emptyList();
    }


    private Collection<TasksProvider.VersionPK> copyTasksToAuxiliaryTable(int maxItemsToSchedule, List<WorkerStateGateway.WorkerState> activeWorkers, TaskEngineParameters taskEngineParameters, RuntimeConfigHolder configHolder)
    {
        int nodeId = taskEngineParameters.getClusterNodeID();
        QueueWorkersStateSummary summary = checkIfWorkersWithLowTasksCountExist(activeWorkers, nodeId, maxItemsToSchedule, configHolder);
        if(!summary.isExistsWorkerWithLowTasksCount())
        {
            return Collections.emptyList();
        }
        int rangeStart = ((Integer)configHolder.getProperty((RuntimeConfigHolder.TaskEngineProperty)AuxiliaryTablesBasedTaskProvider.Params.TASKS_RANGE_START)).intValue();
        int rangeEnd = ((Integer)configHolder.getProperty((RuntimeConfigHolder.TaskEngineProperty)AuxiliaryTablesBasedTaskProvider.Params.TASKS_RANGE_END)).intValue();
        String tasksQuery = getTasksQuery(rangeStart, rangeEnd, taskEngineParameters.isProcessTriggerTask());
        String conditionsQuery = getConditionsQuery();
        String expiredTasksQuery = getExpiredTasksQuery(rangeStart, rangeEnd);
        Timer.Context timer = this.copyTasksTimer.time();
        int copiedTasksCount = 0;
        List<TasksProvider.VersionPK> conditions = new ArrayList<>();
        TasksQueueGateway tasksQueueGateway = this.gatewayFactory.getTasksQueueGateway();
        try
        {
            Instant now = Instant.now();
            conditions.addAll(tasksQueueGateway.getConditionsToSchedule(conditionsQuery, now));
            copiedTasksCount = (int)tasksQueueGateway.addTasks(tasksQuery, expiredTasksQuery, now, rangeStart, rangeEnd);
            int newNumberOfProcessableTasks = copiedTasksCount + summary.getNumberOfProcessableTasks();
            reportQueueMetrics(newNumberOfProcessableTasks, conditions.size());
        }
        catch(Exception e)
        {
            LOGGER.error("Error while retrieving tasks to schedule", e);
        }
        long time = timer.stop();
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("{} (scheduler): copied {} tasks to queue, found {} conditions, it took {}", new Object[] {Integer.valueOf(nodeId), Integer.valueOf(copiedTasksCount),
                            Integer.valueOf(conditions.size()), DurationFormatUtils.formatDurationHMS(TimeUnit.NANOSECONDS.toMillis(time))});
        }
        return conditions;
    }


    private static boolean isWorkerWithLowTasksCount(int maxItemsToSchedule, long tasksInQueueForWorker, RuntimeConfigHolder configHolder)
    {
        return
                        (tasksInQueueForWorker < (maxItemsToSchedule * ((Integer)configHolder.getProperty((RuntimeConfigHolder.TaskEngineProperty)AuxiliaryTablesBasedTaskProvider.Params.WORKER_LOW_TASKS_COUNT_THRESHOLD_MULTIPLIER)).intValue()));
    }


    private boolean lockSchedulerRole(int nodeId, Duration lockingTryMinInterval)
    {
        boolean lockAcquired, recreateTables;
        Timer.Context timer = this.lockTimer.time();
        Instant now = Instant.now();
        Duration duration = Duration.between(this.timestamp, now);
        if(duration.toMillis() < lockingTryMinInterval.toMillis())
        {
            return false;
        }
        LOGGER.debug("{}: trying to acquire lock to be a scheduler", Integer.valueOf(nodeId));
        Optional<SchedulerState> result = this.gatewayFactory.getSchedulerStateGateway().getSchedulerTimestamp();
        if(result.isPresent())
        {
            recreateTables = (((SchedulerState)result.get()).getVersion() != 4);
            lockAcquired = this.gatewayFactory.getSchedulerStateGateway().updateSchedulerRow(lockingTryMinInterval);
            if(lockAcquired)
            {
                this.timestamp = Instant.now();
            }
        }
        else
        {
            recreateTables = true;
            lockAcquired = this.gatewayFactory.getSchedulerStateGateway().insertSchedulerRow(4);
        }
        if(lockAcquired && recreateTables)
        {
            recreateAuxiliaryTables(nodeId);
            lockAcquired = this.gatewayFactory.getSchedulerStateGateway().insertSchedulerRow(4);
        }
        timer.stop();
        return lockAcquired;
    }


    private void recreateAuxiliaryTables(int nodeId)
    {
        Instant start = Instant.now();
        LOGGER.debug("{} (scheduler): recreating tables", Integer.valueOf(nodeId));
        try
        {
            this.gatewayFactory.getTasksQueueGateway().dropTable();
        }
        catch(Exception e)
        {
            LOGGER.debug("error while dropping tasks queue table", e);
        }
        try
        {
            this.gatewayFactory.getWorkerStateGateway().dropTable();
        }
        catch(Exception e)
        {
            LOGGER.debug("error while dropping workers state table", e);
        }
        try
        {
            this.gatewayFactory.getSchedulerStateGateway().dropTable();
        }
        catch(Exception e)
        {
            LOGGER.debug("error while dropping scheduler state table", e);
        }
        this.gatewayFactory.getTasksQueueGateway().createTable();
        this.gatewayFactory.getWorkerStateGateway().createTable();
        this.gatewayFactory.getSchedulerStateGateway().createTable();
        LOGGER.debug("{} (scheduler): finished recreating tables, it took {}", Integer.valueOf(nodeId), Duration.between(start, Instant.now()));
    }


    private List<WorkerStateGateway.WorkerState> activateWorkers(List<WorkerStateGateway.WorkerState> workers, int nodeId, Duration workerHealthCheckInterval, RuntimeConfigHolder configHolder)
    {
        Timer.Context timer = this.activateWorkersTimer.time();
        List<WorkerStateGateway.WorkerState> activeWorkers = (List<WorkerStateGateway.WorkerState>)workers.stream().filter(resultRow -> (resultRow.getTimeFromHealthCheck().toMillis() <= workerHealthCheckInterval.toMillis())).collect(Collectors.toList());
        int rangeStart = ((Integer)configHolder.getProperty((RuntimeConfigHolder.TaskEngineProperty)AuxiliaryTablesBasedTaskProvider.Params.TASKS_RANGE_START)).intValue();
        int rangeEnd = ((Integer)configHolder.getProperty((RuntimeConfigHolder.TaskEngineProperty)AuxiliaryTablesBasedTaskProvider.Params.TASKS_RANGE_END)).intValue();
        Map<Integer, WorkerStateGateway.WorkerRange> ranges = this.workerHelper.calculateRanges(activeWorkers, rangeStart, rangeEnd);
        if(!ranges.isEmpty())
        {
            this.gatewayFactory.getWorkerStateGateway().updateWorkersRanges(ranges);
        }
        long time = timer.stop();
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("{} (scheduler): activated {} workers (ids: {}), it took {}", new Object[] {Integer.valueOf(nodeId), Integer.valueOf(activeWorkers.size()), activeWorkers
                            .stream().map(WorkerStateGateway.WorkerState::getNodeId).collect(Collectors.toList()),
                            DurationFormatUtils.formatDurationHMS(TimeUnit.NANOSECONDS.toMillis(time))});
        }
        return activeWorkers;
    }


    private void deactivateWorkers(List<WorkerStateGateway.WorkerState> workers, int nodeId, Duration workerHealthCheckInterval)
    {
        Timer.Context timer = this.deactivateWorkersTimer.time();
        List<Integer> invalidWorkerIds = (List<Integer>)workers.stream().filter(workerState -> (workerState.getTimeFromHealthCheck().toMillis() > workerHealthCheckInterval.toMillis())).map(WorkerStateGateway.WorkerState::getNodeId).collect(Collectors.toList());
        if(!invalidWorkerIds.isEmpty())
        {
            this.gatewayFactory.getWorkerStateGateway().deactivateWorkers(invalidWorkerIds);
        }
        long time = timer.stop();
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("{} (scheduler): deactivated {} workers, it took {}", new Object[] {Integer.valueOf(nodeId), Integer.valueOf(invalidWorkerIds.size()),
                            DurationFormatUtils.formatDurationHMS(TimeUnit.NANOSECONDS.toMillis(time))});
        }
    }


    private void cleanLockedTasks(Duration workerHealthCheckInterval)
    {
        Map<Integer, Duration> r = this.gatewayFactory.getWorkerStateGateway().getWorkersHealthChecks();
        List<Integer> invalidWorkerIds = (List<Integer>)r.entrySet().stream().filter(e -> (((Duration)e.getValue()).toMillis() > workerHealthCheckInterval.toMillis())).map(Map.Entry::getKey).collect(Collectors.toList());
        if(!invalidWorkerIds.isEmpty())
        {
            this.gatewayFactory.getWorkerStateGateway().deleteWorkers(invalidWorkerIds);
            this.gatewayFactory.getTasksQueueGateway().unlockTasksForWorkers(invalidWorkerIds);
        }
    }


    private QueueWorkersStateSummary checkIfWorkersWithLowTasksCountExist(List<WorkerStateGateway.WorkerState> activeWorkers, int nodeId, int maxItemsToSchedule, RuntimeConfigHolder configHolder)
    {
        Timer.Context timer = this.countTasksTimer.time();
        Map<WorkerStateGateway.WorkerState, Long> result = (Map<WorkerStateGateway.WorkerState, Long>)activeWorkers.stream().collect(HashMap::new, (map, workerState) -> map.put(workerState, Long.valueOf(0L)), Map::putAll);
        List<TasksQueueGateway.TasksCountResult> tasksCountResults = this.gatewayFactory.getTasksQueueGateway().getTasksCount();
        for(TasksQueueGateway.TasksCountResult t : tasksCountResults)
        {
            Stream<WorkerStateGateway.WorkerState> workersToCount;
            Integer workerNodeId = t.getNodeId();
            String nodeGroup = t.getNodeGroups();
            Long count = Long.valueOf(t.getCount());
            if(nodeGroup == null && workerNodeId == null)
            {
                workersToCount = activeWorkers.stream().filter(workerState -> !workerState.isExclusiveMode());
            }
            else
            {
                workersToCount = activeWorkers.stream().filter(workerState -> workerState.isExclusiveMode() ? (
                                (Objects.equals(workerNodeId, Integer.valueOf(workerState.getNodeId())) || (workerNodeId == null && workerState.getNodeGroups().contains(nodeGroup)))) : (
                                ((workerNodeId == null || Objects.equals(workerNodeId, Integer.valueOf(workerState.getNodeId()))) && (nodeGroup == null || workerState.getNodeGroups().contains(nodeGroup)))));
            }
            workersToCount.forEach(workerState -> result.compute(workerState, ()));
        }
        long time = timer.stop();
        if(LOGGER.isDebugEnabled())
        {
            String statsString = result.entrySet().stream().map(workerState -> MessageFormatUtils.format("'{'id: {0}; exclusiveMode: {1}; nodeGroups: {2}; count: {3}'}'",
                            new Object[] {Integer.valueOf(((WorkerStateGateway.WorkerState)workerState.getKey()).getNodeId()), Boolean.valueOf(((WorkerStateGateway.WorkerState)workerState.getKey()).isExclusiveMode()), ((WorkerStateGateway.WorkerState)workerState.getKey()).getNodeGroups(),
                                            workerState.getValue()})).collect(Collectors.joining(",", "[", "]"));
            LOGGER.debug("{} (scheduler): calculated tasks count: {}, it took {}", new Object[] {Integer.valueOf(nodeId), statsString,
                            DurationFormatUtils.formatDurationHMS(TimeUnit.NANOSECONDS.toMillis(time))});
        }
        int numberOfProcessableTasks = computeTasksQueueSize(tasksCountResults);
        boolean existsWorkerWithLowTasksCount = result.entrySet().stream().anyMatch(workerStateLongEntry -> isWorkerWithLowTasksCount(maxItemsToSchedule, ((Long)workerStateLongEntry.getValue()).longValue(), configHolder));
        return new QueueWorkersStateSummary(numberOfProcessableTasks, existsWorkerWithLowTasksCount);
    }


    protected String getConditionsQuery()
    {
        return this.conditionsQuerySupplier.get();
    }


    protected String getExpiredTasksQuery(int rangeStart, int rangeEnd)
    {
        TasksQueueGateway tasksQueueGateway = this.gatewayFactory.getTasksQueueGateway();
        String randomRangeSQLExpression = tasksQueueGateway.getRangeSQLExpression(rangeStart, rangeEnd);
        String nodeGroupExpression = tasksQueueGateway.defaultIfNull("p_nodeGroup", tasksQueueGateway.getEmptyGroupValue());
        String nodeIdExpression = tasksQueueGateway.defaultIfNull("p_nodeId", Integer.valueOf(-1));
        return MessageFormatUtils.format("SELECT PK, {0} AS rangeCol, {5} AS nodeIdCol, {6} AS nodeGroupCol, {1}/1000/60 AS execTimeCol FROM {2} WHERE {3} = 0 AND {1} <= ? AND {4} = -1", new Object[] {randomRangeSQLExpression,
                        getColumnName("Task", "expirationTimeMillis"),
                        getTableName("Task"), getColumnName("Task", "failed"),
                        getColumnName("Task", "runningOnClusterNode"), nodeIdExpression, nodeGroupExpression});
    }


    protected String getTasksQuery(int rangeStart, int rangeEnd, boolean processTriggerTask)
    {
        if(processTriggerTask)
        {
            return getTasksQuery(rangeStart, rangeEnd);
        }
        return getTasksQuery(rangeStart, rangeEnd) + getTasksQuery(rangeStart, rangeEnd);
    }


    private Set<PK> getPksOfTriggerTaskTypeAndSubtypes()
    {
        ComposedTypeModel type = this.typeService.getComposedTypeForCode("TriggerTask");
        Collection<PK> subTypes = (Collection<PK>)type.getAllSubTypes().stream().map(AbstractItemModel::getPk).collect(Collectors.toSet());
        Set<PK> result = new HashSet<>(subTypes.size() + 1);
        result.add(type.getPk());
        result.addAll(subTypes);
        return result;
    }


    protected String getTasksQuery(int rangeStart, int rangeEnd)
    {
        TasksQueueGateway tasksQueueGateway = this.gatewayFactory.getTasksQueueGateway();
        String randomRangeSQLExpression = tasksQueueGateway.getRangeSQLExpression(rangeStart, rangeEnd);
        String nodeGroupExpression = tasksQueueGateway.defaultIfNull("p_nodeGroup", tasksQueueGateway.getEmptyGroupValue());
        String nodeIdExpression = tasksQueueGateway.defaultIfNull("p_nodeId", Integer.valueOf(-1));
        String conditionSubQuery = MessageFormatUtils.format("SELECT p_task FROM {0} WHERE {1} =t.PK AND {2} = 0", new Object[] {getTableName("TaskCondition"),
                        getColumnName("TaskCondition", "task"),
                        getColumnName("TaskCondition", "fulfilled")});
        return MessageFormatUtils.format("SELECT PK, {0} AS rangeCol, {6} AS nodeIdCol, {7} AS nodeGroupCol, {1}/1000/60 AS execTimeCol FROM {2} t WHERE {3} = 0  AND {1} <= ? AND {4} = -1 AND NOT EXISTS ({5})", new Object[] {randomRangeSQLExpression,
                        getColumnName("Task", "executionTimeMillis"),
                        getTableName("Task"), getColumnName("Task", "failed"),
                        getColumnName("Task", "runningOnClusterNode"), conditionSubQuery, nodeIdExpression, nodeGroupExpression});
    }


    private String getTableName(String code)
    {
        ComposedTypeModel type = this.typeService.getComposedTypeForCode(code);
        return type.getTable();
    }


    private String getColumnName(String code, String column)
    {
        ComposedTypeModel type = this.typeService.getComposedTypeForCode(code);
        return ((AttributeDescriptorModel)type.getDeclaredattributedescriptors().stream().filter(s -> s.getQualifier().equalsIgnoreCase(column)).findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("type " + code + " is not recognizable"))).getDatabaseColumn();
    }


    private int computeTasksQueueSize(List<TasksQueueGateway.TasksCountResult> tasksCountResult)
    {
        return tasksCountResult.stream().mapToInt(result -> (int)result.getCount()).sum();
    }


    private void reportQueueMetrics(int numberOfProcessableTasks, int numberOfConditions)
    {
        reportTasksQueueSize(numberOfProcessableTasks);
        reportConditionsQueueSize(numberOfConditions);
        reportPoolingQueueSize(numberOfProcessableTasks + numberOfConditions);
    }


    private void reportTasksQueueSize(int size)
    {
        DefaultTaskService.PoolingQueueSizeGauge.getPoolingQueueSizeGauge(DefaultTaskService.TASKS_QUEUE_SIZE_METRIC, this.metricRegistry).setSize(size);
    }


    private void reportConditionsQueueSize(int size)
    {
        DefaultTaskService.PoolingQueueSizeGauge.getPoolingQueueSizeGauge(DefaultTaskService.CONDITIONS_QUEUE_SIZE_METRIC, this.metricRegistry).setSize(size);
    }


    private void reportPoolingQueueSize(int size)
    {
        DefaultTaskService.PoolingQueueSizeGauge.getPoolingQueueSizeGauge(DefaultTaskService.POOLING_QUEUE_SIZE_METRIC, this.metricRegistry).setSize(size);
    }


    public void afterPropertiesSet()
    {
        this.activateWorkersTimer = this.metricRegistry.timer(
                        RuntimeConfigHolder.metricName("pooling.scheduler.activateWorkers.time"));
        this.lockTimer = this.metricRegistry.timer(RuntimeConfigHolder.metricName("pooling.scheduler.lock.time"));
        this.schedulerTimer = this.metricRegistry.timer(RuntimeConfigHolder.metricName("pooling.scheduler.time"));
        this
                        .deactivateWorkersTimer = this.metricRegistry.timer(RuntimeConfigHolder.metricName("pooling.scheduler.deactivateWorkers.time"));
        this.copyTasksTimer = this.metricRegistry.timer(RuntimeConfigHolder.metricName("pooling.scheduler.copyTasks.time"));
        this.countTasksTimer = this.metricRegistry.timer(RuntimeConfigHolder.metricName("pooling.scheduler.countTasks.time"));
    }


    @Required
    public void setGatewayFactory(AuxiliaryTablesGatewayFactory gatewayFactory)
    {
        this.gatewayFactory = gatewayFactory;
    }


    @Required
    public void setMetricRegistry(MetricRegistry metricRegistry)
    {
        this.metricRegistry = metricRegistry;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setWorkerHelper(WorkerHelper workerHelper)
    {
        this.workerHelper = workerHelper;
    }
}
