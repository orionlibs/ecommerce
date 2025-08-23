package de.hybris.platform.task.impl;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class QueryBasedTasksProvider implements TasksProvider
{
    public static final String PROPERTY_USE_READ_REPLICA = "task.polling.queryTaskProvider.read-replica.enabled";
    private static final Logger LOG = LoggerFactory.getLogger(QueryBasedTasksProvider.class);
    private final MetricRegistry metricRegistry;
    private final FlexibleSearchService flexibleSearchService;
    private final TypeService typeService;
    private static final List<Class> resultClasses = Arrays.asList(new Class[] {Long.class, Long.class});
    private final AtomicInteger totalQueryCounter = new AtomicInteger(0);
    private final TaskServiceQueryProvider taskServiceQueryProvider = (TaskServiceQueryProvider)new DefaultTaskServiceQueryProvider();
    private final AtomicReference<Instant> queueSizeLastReportTime = new AtomicReference<>(Instant.EPOCH);
    private static final RuntimeConfigHolder.IntTaskEngineProperty TASK_COUNT = RuntimeConfigHolder.intProperty("task.engine.query.tasks.count", Integer.valueOf(-1));
    private static final RuntimeConfigHolder.IntTaskEngineProperty FULL_QUERY_INTERVAL = RuntimeConfigHolder.intProperty("task.engine.query.full.interval",
                    Integer.valueOf(0));
    private static final RuntimeConfigHolder.DurationTaskEngineProperty FULL_QUERY_EXECUTION_TIME_THRESHOLD = RuntimeConfigHolder.durationProperty("task.engine.query.full.executiontime.threshold", ChronoUnit.HOURS,
                    Duration.ofHours(0L));
    private static final RuntimeConfigHolder.IntTaskEngineProperty CONDITION_COUNT = RuntimeConfigHolder.intProperty("task.engine.query.conditions.count",
                    Integer.valueOf(-1));
    private static final RuntimeConfigHolder.IntTaskEngineProperty QUEUE_SIZE_REPORTING_INTERVAL = RuntimeConfigHolder.intProperty("task.queue.size.reporting.interval",
                    Integer.valueOf(0));
    @Deprecated(since = "2205", forRemoval = true)
    static final String POOLING_QUEUE_SIZE_METRIC = DefaultTaskService.POOLING_QUEUE_SIZE_METRIC;
    private static final String TASKS_TABLE_SIZE_METRIC = MetricRegistry.name("db.table.tasks.size", new String[0]);
    private static final String CONDITIONS_TABLE_SIZE_METRIC = MetricRegistry.name("db.table.conditions.size", new String[0]);
    private static final String POOLING_TIME_METRIC = MetricRegistry.name("pooling.time", new String[0]);


    QueryBasedTasksProvider(MetricRegistry metricRegistry, FlexibleSearchService flexibleSearchService, TypeService typeService)
    {
        this.metricRegistry = metricRegistry;
        this.flexibleSearchService = flexibleSearchService;
        this.typeService = typeService;
    }


    public List<TasksProvider.VersionPK> getTasksToSchedule(RuntimeConfigHolder runtimeConfigHolder, TaskEngineParameters taskEngineParameters, int maxItemsToSchedule)
    {
        Timer.Context poolingTimeMetricCtx = this.metricRegistry.timer(RuntimeConfigHolder.metricName(POOLING_TIME_METRIC)).time();
        SessionContext ctx = null;
        try
        {
            ctx = JaloSession.getCurrentSession().createLocalSessionContext();
            boolean isReadReplicaEnabled = isTaskProviderReadReplicaEnabled(taskEngineParameters);
            if(isReadReplicaEnabled)
            {
                LOG.debug("Executing Task query on read-replica DB ");
            }
            ctx.setAttribute("ctx.enable.fs.on.read-replica", Boolean.valueOf(isReadReplicaEnabled));
            return queryForTasksAndConditions(runtimeConfigHolder, taskEngineParameters);
        }
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
            poolingTimeMetricCtx.stop();
        }
    }


    private boolean isFullQueryNeeded(boolean needTotal, RuntimeConfigHolder runtimeConfigHolder)
    {
        int fullQueryInterval = ((Integer)runtimeConfigHolder.getProperty((RuntimeConfigHolder.TaskEngineProperty)FULL_QUERY_INTERVAL)).intValue();
        return (this.totalQueryCounter.getAndUpdate(operand -> (operand >= fullQueryInterval) ? 0 : operand++) >= fullQueryInterval || needTotal);
    }


    private List<TasksProvider.VersionPK> queryForTasksAndConditions(RuntimeConfigHolder runtimeConfigHolder, TaskEngineParameters taskEngineParameters)
    {
        Optional<SearchResult<List<Long>>> expiredTasks, conditions;
        boolean needTotal = isTotalNeeded(runtimeConfigHolder);
        boolean shouldRunFullQuery = isFullQueryNeeded(needTotal, runtimeConfigHolder);
        SearchResult<List<Long>> tasks = queryTasks(needTotal, shouldRunFullQuery, runtimeConfigHolder, taskEngineParameters);
        if(shouldRunFullQuery)
        {
            expiredTasks = Optional.of(queryExpiredTasks(needTotal, runtimeConfigHolder, taskEngineParameters));
            conditions = Optional.of(queryTimedOutConditions(needTotal, runtimeConfigHolder, taskEngineParameters));
        }
        else
        {
            expiredTasks = Optional.empty();
            conditions = Optional.empty();
        }
        if(needTotal)
        {
            int tasksCount = tasks.getTotalCount() + ((Integer)expiredTasks.<Integer>map(SearchResult::getTotalCount).orElse(Integer.valueOf(0))).intValue();
            reportPoolingQueueSize(tasksCount + ((Integer)conditions.<Integer>map(SearchResult::getTotalCount).orElse(Integer.valueOf(0))).intValue());
            reportTasksQueueSize(tasks.getTotalCount());
            conditions.ifPresent(r -> reportConditionsQueueSize(r.getTotalCount()));
            reportDbTablesSize();
        }
        return (List<TasksProvider.VersionPK>)Stream.<Optional>of(new Optional[] {Optional.of(tasks), conditions, expiredTasks}).flatMap(o -> ((List)o.map(SearchResult::getResult).orElse(Collections.emptyList())).stream())
                        .map(QueryBasedTasksProvider::toVersionPK).collect(Collectors.toList());
    }


    private boolean isTaskProviderReadReplicaEnabled(TaskEngineParameters taskEngineParameters)
    {
        return taskEngineParameters.getTenant().getConfig().getBoolean("task.polling.queryTaskProvider.read-replica.enabled", false);
    }


    private boolean isTotalNeeded(RuntimeConfigHolder runtimeConfigHolder)
    {
        int configuredQueueSizeReportingInterval = ((Integer)runtimeConfigHolder.getProperty((RuntimeConfigHolder.TaskEngineProperty)QUEUE_SIZE_REPORTING_INTERVAL)).intValue();
        if(configuredQueueSizeReportingInterval <= 0)
        {
            return (configuredQueueSizeReportingInterval == 0);
        }
        Instant lastReportTime = this.queueSizeLastReportTime.get();
        return (!lastReportTime.plusSeconds(configuredQueueSizeReportingInterval).isAfter(Instant.now()) && this.queueSizeLastReportTime
                        .compareAndSet(lastReportTime, Instant.now()));
    }


    private void reportDbTablesSize()
    {
        FlexibleSearchQuery tasksTableSizeQuery = new FlexibleSearchQuery("SELECT count({pk}) FROM {Task}");
        tasksTableSizeQuery.setResultClassList(Collections.singletonList(Integer.class));
        tasksTableSizeQuery.setDisableCaching(true);
        SearchResult<Integer> tasksTableSizeResult = this.flexibleSearchService.search(tasksTableSizeQuery);
        int taskTableSize = ((Integer)tasksTableSizeResult.getResult().get(0)).intValue();
        DefaultTaskService.PoolingQueueSizeGauge.getPoolingQueueSizeGauge(TASKS_TABLE_SIZE_METRIC, this.metricRegistry).setSize(taskTableSize);
        FlexibleSearchQuery conditionsTableSizeQuery = new FlexibleSearchQuery("SELECT count({pk}) FROM {TaskCondition}");
        conditionsTableSizeQuery.setResultClassList(Collections.singletonList(Integer.class));
        conditionsTableSizeQuery.setDisableCaching(true);
        SearchResult<Integer> conditionsTableSizeResult = this.flexibleSearchService.search(conditionsTableSizeQuery);
        int conditionsTableSize = ((Integer)conditionsTableSizeResult.getResult().get(0)).intValue();
        DefaultTaskService.PoolingQueueSizeGauge.getPoolingQueueSizeGauge(CONDITIONS_TABLE_SIZE_METRIC, this.metricRegistry).setSize(conditionsTableSize);
    }


    private SearchResult<List<Long>> queryTasks(boolean needTotal, boolean shouldRunFullQuery, RuntimeConfigHolder runtimeConfigHolder, TaskEngineParameters taskEngineParameters)
    {
        int maxThreads = taskEngineParameters.getMaxThreads();
        Collection<String> myGroups = taskEngineParameters.getClusterGroupsForThisNode();
        int clusterNodeID = taskEngineParameters.getClusterNodeID();
        taskEngineParameters.getTenant().forceMasterDataSource();
        boolean processTriggerTasks = taskEngineParameters.isProcessTriggerTask();
        Integer nodeId = Integer.valueOf(clusterNodeID);
        Integer noNode = Integer.valueOf(-1);
        boolean isNodeExclusiveModeEnabled = (!noNode.equals(nodeId) && taskEngineParameters.isExclusiveMode());
        Map<String, Object> parameters = this.taskServiceQueryProvider.setQueryParameters(myGroups, processTriggerTasks, nodeId, noNode,
                        getPksOfTriggerTaskTypeAndSubtypes(), (Duration)runtimeConfigHolder
                                        .getProperty((RuntimeConfigHolder.TaskEngineProperty)FULL_QUERY_EXECUTION_TIME_THRESHOLD));
        String query = this.taskServiceQueryProvider.getValidTasksToExecuteQuery(myGroups, processTriggerTasks, isNodeExclusiveModeEnabled, shouldRunFullQuery);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("valid tasks query: {}", query);
        }
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
        searchQuery.addQueryParameters(parameters);
        searchQuery.setResultClassList(resultClasses);
        searchQuery.setDisableCaching(true);
        Optional<Integer> tasksCount = getTasksCount(runtimeConfigHolder, maxThreads);
        Objects.requireNonNull(searchQuery);
        tasksCount.ifPresent(searchQuery::setCount);
        searchQuery.setNeedTotal(needTotal);
        return this.flexibleSearchService.search(searchQuery);
    }


    protected Optional<Integer> getTasksCount(RuntimeConfigHolder runtimeConfigHolder, int maxThreads)
    {
        return Optional.of((Integer)runtimeConfigHolder.getProperty((RuntimeConfigHolder.TaskEngineProperty)TASK_COUNT, maxThreads));
    }


    private SearchResult<List<Long>> queryExpiredTasks(boolean needTotal, RuntimeConfigHolder runtimeConfigHolder, TaskEngineParameters taskEngineParameters)
    {
        int maxThreads = taskEngineParameters.getMaxThreads();
        Collection<String> myGroups = taskEngineParameters.getClusterGroupsForThisNode();
        taskEngineParameters.getTenant().forceMasterDataSource();
        boolean processTriggerTasks = taskEngineParameters.isProcessTriggerTask();
        Integer nodeId = Integer.valueOf(taskEngineParameters.getClusterNodeID());
        Integer noNode = Integer.valueOf(-1);
        boolean isNodeExclusiveModeEnabled = (!noNode.equals(nodeId) && taskEngineParameters.isExclusiveMode());
        Map<String, Object> parameters = this.taskServiceQueryProvider.setQueryParameters(myGroups, processTriggerTasks, nodeId, noNode,
                        getPksOfTriggerTaskTypeAndSubtypes(), (Duration)runtimeConfigHolder
                                        .getProperty((RuntimeConfigHolder.TaskEngineProperty)FULL_QUERY_EXECUTION_TIME_THRESHOLD));
        String query = this.taskServiceQueryProvider.getExpiredTasksToExecuteQuery(myGroups, processTriggerTasks, isNodeExclusiveModeEnabled);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("expired tasks query: {}", query);
        }
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
        searchQuery.addQueryParameters(parameters);
        searchQuery.setResultClassList(resultClasses);
        searchQuery.setDisableCaching(true);
        Optional<Integer> tasksCount = getTasksCount(runtimeConfigHolder, maxThreads);
        Objects.requireNonNull(searchQuery);
        tasksCount.ifPresent(searchQuery::setCount);
        searchQuery.setNeedTotal(needTotal);
        return this.flexibleSearchService.search(searchQuery);
    }


    private SearchResult<List<Long>> queryTimedOutConditions(boolean needTotal, RuntimeConfigHolder runtimeConfigHolder, TaskEngineParameters taskEngineParameters)
    {
        int maxThreads = taskEngineParameters.getMaxThreads();
        taskEngineParameters.getTenant().forceMasterDataSource();
        String query = this.taskServiceQueryProvider.getTimedOutConditionsQuery();
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("now", Long.valueOf(System.currentTimeMillis()));
        parameters.put("false", Boolean.FALSE);
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
        searchQuery.addQueryParameters(parameters);
        searchQuery.setResultClassList(resultClasses);
        searchQuery.setDisableCaching(true);
        searchQuery.setCount(((Integer)runtimeConfigHolder.getProperty((RuntimeConfigHolder.TaskEngineProperty)CONDITION_COUNT, maxThreads)).intValue());
        searchQuery.setNeedTotal(needTotal);
        return this.flexibleSearchService.search(searchQuery);
    }


    private Set<PK> getPksOfTriggerTaskTypeAndSubtypes()
    {
        ComposedTypeModel type = this.typeService.getComposedTypeForCode("TriggerTask");
        Collection<ComposedTypeModel> subTypes = type.getAllSubTypes();
        Set<PK> result = new HashSet<>(subTypes.size() + 1);
        result.add(type.getPk());
        for(ComposedTypeModel subType : subTypes)
        {
            result.add(subType.getPk());
        }
        return result;
    }


    private static TasksProvider.VersionPK toVersionPK(List<Long> record)
    {
        return new TasksProvider.VersionPK(PK.fromLong(((Long)record.get(0)).longValue()), ((Long)record.get(1)).longValue());
    }


    private void reportPoolingQueueSize(int size)
    {
        DefaultTaskService.PoolingQueueSizeGauge.getPoolingQueueSizeGauge(DefaultTaskService.POOLING_QUEUE_SIZE_METRIC, this.metricRegistry).setSize(size);
    }


    private void reportTasksQueueSize(int size)
    {
        DefaultTaskService.PoolingQueueSizeGauge.getPoolingQueueSizeGauge(DefaultTaskService.TASKS_QUEUE_SIZE_METRIC, this.metricRegistry).setSize(size);
    }


    private void reportConditionsQueueSize(int size)
    {
        DefaultTaskService.PoolingQueueSizeGauge.getPoolingQueueSizeGauge(DefaultTaskService.CONDITIONS_QUEUE_SIZE_METRIC, this.metricRegistry).setSize(size);
    }
}
